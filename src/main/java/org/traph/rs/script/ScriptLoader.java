package org.traph.rs.script;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.traph.rs.net.Client;
import org.traph.rs.net.packet.Packet;

import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import io.vertx.core.Vertx;


/**
 * We always pass a script object to the javascript file
 * 
 * The script object basically lets us interact with the game engine (e.g. access world properties / etc
 * potentially doing something like interacting the the dialogue engine (for dialogue between players and other entities).
 * 
 * We will also need to pass the client to the script that gets executed.
 * 
 * Scripts can be executed in one way currently. Incoming packets.
 * 
 * Anything that changes the game state is an action? (unless it can be done immediately)
 * 
 * Somehow we need to register the various packet listeners with a script
 * 
 * We could do it by file name....
 * 
 * We could also have a method return a list of packets to listen for
 * 
 * @author Derek
 */

public class ScriptLoader {
	
	private final Vertx vertx;
	
	private final GroovyScriptEngine scriptEngine;
	
	private final Map<Integer, List<String>> scriptListeners;
	
	public ScriptLoader(Vertx vertx, String path) throws Exception {
		this.vertx = vertx;
		this.scriptEngine = new GroovyScriptEngine(path);
		this.scriptListeners = new HashMap<Integer, List<String>>();
		this.load(path);
	}
	
	public void execute(Client client, Packet packet) {
		if(scriptListeners.containsKey(packet.getOpcode())) {
			List<String> listeners = scriptListeners.get(packet.getOpcode());
			for(String listener : listeners) {
				Binding binding = new Binding();
				binding.setVariable("client", client);
				binding.setVariable("packet", packet);
				//TODO: add binding to world or something similar so we can access other
				// parts of the game engine
				try {
					scriptEngine.run(listener, binding);
				} catch (ResourceException | ScriptException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.err.println("Script not found for packet: " + packet.getOpcode());
		}
	}
	
	private void load(String directory) {
		vertx.fileSystem().readDirBlocking(directory).forEach(file -> {
			if(new File(file).isDirectory()) {				
				load(file);
			} else {
				Class<?> clazz = getScriptClass(file);
				if(clazz == null) {
					return;
				}
				int[] packets = getPackets(clazz);
				if(packets != null) {
					for(int packet : packets) {
						List<String> listeners = scriptListeners.putIfAbsent(packet, new ArrayList<String>());
						if(listeners == null) {
							listeners = scriptListeners.get(packets);
						}
						listeners.add(file);
					}
				}
			}
		});
	}
	
	private Class<?> getScriptClass(String scriptName) {
		try {
			return scriptEngine.loadScriptByName(scriptName);
		} catch (ResourceException | ScriptException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private int[] getPackets(Class<?> clazz) {
		for(Method method : clazz.getDeclaredMethods()) {
			if(method.getName() == "packets") {
				try {
					return (int[]) method.invoke(clazz.newInstance());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| InstantiationException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}
	
}


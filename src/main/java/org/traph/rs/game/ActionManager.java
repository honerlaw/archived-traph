package org.traph.rs.game;

import java.util.HashMap;
import java.util.Map;

import org.traph.rs.World;
import org.traph.rs.net.Client;

/**
 * We maintain two lists of the action that is suppose to be executed
 * 
 * The first list (this one) is used so we can cancel actions / etc
 * 
 * Whenever we add an action to the world we add the action to a list in here
 * 
 * We then register the ActionManager with the World however many times we need
 * to so that we can
 * 
 * @author Derek
 *
 */
public class ActionManager {
	
	public static enum Type {
		BASIC, WALK
	}
	
	private final Client client;
	private final World world;
	private final Map<Type, Action> actions;
	
	public ActionManager(Client client, World world) {
		this.client = client;
		this.world = world;
		this.actions = new HashMap<Type, Action>();
	}
	
	public ActionManager add(Action action) {
		
		return this;
	}
	
	public Action getNext() {
		
		return null;
	}

}

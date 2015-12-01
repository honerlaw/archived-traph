package org.traph.rs;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.traph.fs.FileSystem;
import org.traph.rs.game.ActionManager;
import org.traph.rs.net.Client;
import org.traph.rs.net.worker.Decoder;
import org.traph.rs.net.worker.Login;
import org.traph.rs.script.ScriptLoader;
import org.traph.util.Constant;
import org.traph.util.Constant.Client.State;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class World extends AbstractVerticle {
	
	private FileSystem fileSystem;
	
	private ScriptLoader scriptLoader;
	
	private final Map<NetSocket, Client> clientMap = new ConcurrentHashMap<NetSocket, Client>();
	
	private final Client[] clients = new Client[2048];
	
	private final List<ActionManager> actions = Collections.synchronizedList(new LinkedList<ActionManager>());
	
	@Override
	public void start() {
		
		// load the cache into memory
		try {
			fileSystem = new FileSystem(getVertx(), "data/cache");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		// load all of the scripts
		try {
			scriptLoader = new ScriptLoader(this, "data/scripts");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// http
		getVertx().createHttpServer().requestHandler(req -> {
			req.response().end(fileSystem.getHttpResponse(req));
		}).listen(80);
		
		// jaggrab
		getVertx().createNetServer().connectHandler(sock -> {
			sock.handler(buf -> {
				String req = buf.getString(0, buf.length());
				req = req.substring(8, req.length());
				sock.write(fileSystem.getResource(req)).close();
			});
		}).listen(43595);
		
		// game / ondemand
		getVertx().createNetServer().connectHandler(sock -> {
			
			// handle when a player disconnects
			sock.closeHandler(han -> {
				// set / get the client for the socket
				Client client = clientMap.get(sock);
				if(client != null) {
					deregister(client);
				}
				clientMap.remove(sock);
			});
			
			// handle incoming data
			sock.handler(buf -> {
				
				buf = buf.copy();
				
				// set / get the client for the socket
				Client tempClient = clientMap.putIfAbsent(sock, new Client(sock, this));
				if(tempClient == null) {
					tempClient = clientMap.get(sock);
				}
				final Client client = tempClient;
				
				// handle the request
				switch(client.getState()) {
					case HANDSHAKE:
						switch(buf.getByte(0) & 0xFF) {
							case Constant.HandShake.LOGIN_REQUEST:
								getVertx().executeBlocking(fut -> {
									fut.complete(Constant.RANDOM.nextLong());
								}, res -> {
									client.setServerSeed((long) res.result());
									client.setState(State.LOGIN);
									sock.write(Buffer.buffer(17).appendByte((byte) 0).appendLong(0).appendLong(client.getServerSeed()));
								});
								break;
							case Constant.HandShake.ONDEMAND_REQUEST:
								client.setState(State.ONDEMAND);
								sock.write(Buffer.buffer(8).appendLong(0));
								break;
						}
						break;
					case ONDEMAND:
						Buffer[] buffers = fileSystem.getOndemandResponse(buf);
						for(Buffer b : buffers) {
							sock.write(b);
						}
						break;
					case LOGIN:
						getVertx().executeBlocking(new Login(client, buf), res -> {
							if(client.getState() == State.GAME) {
								register(client);
							}
							if(res.result() != null) {
								sock.write(res.result());
							}
						});
						break;
					case GAME:		
						getVertx().executeBlocking(new Decoder(client, buf), res -> {
							if(res.result() != null) {
								res.result().forEach(packet -> {
									getScriptLoader().execute(client, packet);
								});
							}
						});
						break;
				}
			});
			
		}).listen(43594);
		
		// update the world every 600 ms
		getVertx().setPeriodic(600, id -> {
			
			getVertx().executeBlocking(fut -> {
				
				// loop through actions and execute them
				for(ListIterator<ActionManager> it = getActionManagers().listIterator(); it.hasNext(); ) {
					Action action = it.next().getNext();
					if(!action.isCancelled()) {
						try {
							Action next = action.execute();
							if(next != null) {
								it.add(next);
							}
						} catch(Exception e) {
							e.printStackTrace();
						}
 					}
					it.remove();
				}
				
				
				List<Client> clients = getClients();
				for(Iterator<Client> it = clients.iterator(); it.hasNext(); ) {
					Client client = it.next();
					if(client == null) {
						continue;
					}
					client.getDispatcher().playerUpdate(World.this);
					client.getGameData().getPlayer().reset();
				}
				
			}, res -> {
				if(res.failed()) {
					res.cause().printStackTrace();
				}
			});
		
		});
		
	}
	
	public boolean register(Client client) {
		synchronized(clients) {
			for(int i = 0; i < clients.length; ++i) {
				if(clients[i] == null) {
					clients[i] = client;
					client.getGameData().getPlayer().setIndex(i);
					return true;
				}
			}
		}
		return false;
	}
	
	public void deregister(Client client) {
		if(client.getGameData().getPlayer() == null || client.getGameData().getPlayer().getIndex() == -1) {
			return;
		}
		synchronized(clients) {
			clients[client.getGameData().getPlayer().getIndex()] = null;
		}
	}
	
	public List<Client> getClients() {
		return Arrays.asList(clients);
	}
	
	public List<ActionManager> getActionManagers() {
		return actions;
	}
	
	public ScriptLoader getScriptLoader() {
		return scriptLoader;
	}
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle("org.traph.rs.World");
	}

}

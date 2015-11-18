package org.traph.rs;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.traph.fs.FileSystem;
import org.traph.rs.net.Client;
import org.traph.rs.net.worker.Decoder;
import org.traph.rs.net.worker.Login;
import org.traph.util.Constant;
import org.traph.util.Constant.Client.State;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

public class World extends AbstractVerticle {
	
	private FileSystem fileSystem;
	
	private final Map<NetSocket, Client> clientMap = new HashMap<NetSocket, Client>();
	
	private final Client[] clients = new Client[2048];
	
	@Override
	public void start() {
		
		// load the cache into memory
		try {
			fileSystem = new FileSystem(getVertx(), "data/cache");
		} catch (FileNotFoundException e) {
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
								
								// execute blocking because Constant.RANDOM.nextLong can take a while
								getVertx().executeBlocking(future -> {
									future.complete(Constant.RANDOM.nextLong());
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
						// handle login
						getVertx().executeBlocking(new Login(client, buf), res -> {
							if(client.getState() == State.GAME) {
								register(client);
							}
							sock.write(res.result());
						});
						break;
					case GAME:						
						// decode packets
						getVertx().executeBlocking(new Decoder(client, buf), res -> {
							Buffer result = res.result();
							if(result != null) {
								sock.write(result);
							}
						});
						break;
				}
			});
			
		}).listen(43594);
		
		// update the world every 600 ms
		getVertx().setPeriodic(600, id -> {
			getVertx().executeBlocking(fut -> {
				
				// we update each client in the world
				for(Client client : clients) {
					if(client == null) {
						continue;
					}
					
					// updates the player
					client.getSocket().write(client.getUpdateBuilder().getPlayerPacket());
				}
				
				fut.complete();
			}, res -> {
				
			});
		});
		
	}
	
	public boolean register(Client client) {
		for(int i = 0; i < clients.length; ++i) {
			if(clients[i] == null) {
				clients[i] = client;
				client.getGameData().getPlayer().setIndex(i);
				return true;
			}
		}
		return false;
	}
	
	public void deregister(Client client) {
		if(client.getGameData().getPlayer() == null || client.getGameData().getPlayer().getIndex() == -1) {
			return;
		}
		clients[client.getGameData().getPlayer().getIndex()] = null;
	}
	
	public Client[] getClients() {
		return clients;
	}
	
	public static void main(String[] args) {
		Vertx.vertx().deployVerticle("org.traph.rs.World");
	}

}

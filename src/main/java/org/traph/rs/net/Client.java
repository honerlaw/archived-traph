package org.traph.rs.net;

import org.traph.rs.model.player.Player;
import org.traph.util.Constant.Client.State;
import org.traph.util.security.IsaacRandomGen;

import io.vertx.core.net.NetSocket;

public class Client {
	
	private final NetSocket socket;
	private State state = State.HANDSHAKE;
	private long serverSeed;
	private long clientSeed;
	private IsaacRandomGen isaacDecoder;
	private IsaacRandomGen isaacEncoder;
	private Player player;
	
	public Client(NetSocket socket) {
		this.socket = socket;
	}
	
	public NetSocket getSocket() {
		return socket;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public State getState() {
		return state;
	}
	
	public void setServerSeed(long serverSeed) {
		this.serverSeed = serverSeed;
	}
	
	public long getServerSeed() {
		return serverSeed;
	}
	
	public void setClientSeed(long clientSeed) {
		this.clientSeed = clientSeed;
	}
	
	public long getClientSeed() {
		return clientSeed;
	}
	
	public void setIsaacDecoder(IsaacRandomGen isaacDecoder) {
		this.isaacDecoder = isaacDecoder;
	}
	
	public IsaacRandomGen getIsaacDecoder() {
		return isaacDecoder;
	}
	
	public void setIsaacEncoder(IsaacRandomGen isaacEncoder) {
		this.isaacEncoder = isaacEncoder;
	}
	
	public IsaacRandomGen getIsaacEncoder() {
		return isaacEncoder;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}

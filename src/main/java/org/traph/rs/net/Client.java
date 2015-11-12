package org.traph.rs.net;

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
	private int index;
	private GameData gameData;
	
	public Client(NetSocket socket) {
		this.socket = socket;
		this.gameData = new GameData();
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

	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	public GameData getGameData() {
		return gameData;
	}

}

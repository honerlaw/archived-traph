package org.traph.rs.net.packet;

import org.traph.rs.net.Client;
import org.traph.rs.net.update.EntityUpdate.UpdateFlag;
import org.traph.util.net.GameBuffer;

public class Dispatcher {
	
	private final Client client;
	
	public Dispatcher(Client client) {
		this.client = client;
	}
	
	public Dispatcher login(int response, int rights, int unknown) {
		client.getSocket().write(GameBuffer.buffer().put(response).put(rights).put(unknown).getRawBuffer());
		return this;
	}
	
	public Dispatcher mapRegion() {
		client.getGameData().getPlayer().set(UpdateFlag.TELEPORT);
		return dispatch(GameBuffer.fixed(73)
				.putShortA(client.getGameData().getPlayer().getRegion().getX() + 6)
				.putShort(client.getGameData().getPlayer().getRegion().getY() + 6));
	}
	
	private Dispatcher dispatch(GameBuffer buffer) {
		client.getSocket().write(buffer.getBuffer(client));
		return this;
	}

}

package org.traph.rs.net.packet;

import java.util.Iterator;
import java.util.List;

import org.traph.rs.World;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.rs.net.GameData;
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
		client.getGameData().getPlayer().set(UpdateFlag.TELEPORT, UpdateFlag.APPEARANCE);
		return dispatch(GameBuffer.fixed(73)
				.putShortA(client.getGameData().getPlayer().getRegion().getX() + 6)
				.putShort(client.getGameData().getPlayer().getRegion().getY() + 6));
	}
	
	public Dispatcher playerUpdate(World world) {
		
		// Get the GameData and Player
		GameData data = client.getGameData();
		Player player = data.getPlayer();
		
		// The buffer holding the packet data
		GameBuffer packet = GameBuffer.variableShort(81).startBits();
		
		// The buffer holding the attributes data
		GameBuffer buffer = GameBuffer.buffer();
		
		// Update our players movement and attributes
		player.getMovement().build(packet, true);
		if(player.isAttributesUpdate()) {
			player.getAttributes().build(buffer, false, true);
		}
		
		// Notify the client with the expected number of local players
		packet.putBits(8, data.getLocalPlayers().size());
		
		// Update all of the local players
		for(Iterator<Player> it = data.getLocalPlayers().iterator(); it.hasNext(); ) {
			Player other = it.next();
			if(other.getRegion().getLocation().isViewableFrom(player.getRegion().getLocation())) {
				// we can see each other so update them
				other.getMovement().build(packet, false);
				if(other.isAttributesUpdate()) {
					other.getAttributes().build(buffer, false, false);
				}
			} else {			
				// we can't see each other so remove them
				packet.putBit(true).putBits(2, 3);
				it.remove();
			}
		}
		
		List<Client> clients = world.getClients();
		
		// Loop through all of the players in the world and see if more have come into view
		for(Iterator<Client> it = clients.iterator(); it.hasNext(); ) {
			Client other = it.next();
			if(data.getLocalPlayers().size() >= 255) {
				break;
			}
			if(other == null || other == client) {
				continue;
			}
			Player otherPlayer = other.getGameData().getPlayer();
			
			// if they are viewable and are not in our list already
			if(!data.getLocalPlayers().contains(other.getGameData().getPlayer()) && otherPlayer.getRegion().getLocation().isViewableFrom(player.getRegion().getLocation())) {
				
				// add them and update them
				data.getLocalPlayers().add(other.getGameData().getPlayer());
				otherPlayer.getAdd().build(packet, player);
				otherPlayer.getAttributes().build(buffer, true, false);
			}
		}
		
		if(buffer.getRawBuffer().getByteBuf().writerIndex() > 0) {		
			packet
				.putBits(11, 2047)
				.endBits()
				.putBuffer(buffer.getRawBuffer());
		} else {
			packet.endBits();
		}
	
		return dispatch(packet);
	}
	
	private Dispatcher dispatch(GameBuffer buffer) {
		client.getSocket().write(buffer.getBuffer(client));
		return this;
	}

}

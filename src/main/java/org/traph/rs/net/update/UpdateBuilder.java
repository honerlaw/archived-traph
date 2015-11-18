package org.traph.rs.net.update;

import java.util.Iterator;

import org.traph.rs.World;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.rs.net.GameData;
import org.traph.util.net.GameBuffer;

import io.vertx.core.buffer.Buffer;

/**
 * Handles building the client update packet
 * 
 * @author honerlad
 */
public class UpdateBuilder {
		
	private final World world;
	private final Client client;
	
	public UpdateBuilder(World world, Client client) {
		this.client = client;
		this.world = world;
	}
	
	/**
	 * Builds the player update packet
	 * 
	 * @return The player update packet
	 */
	public Buffer getPlayerPacket() {
		
		// Get the GameData and Player
		GameData data = client.getGameData();
		Player player = data.getPlayer();
		
		// The buffer holding the packet data
		GameBuffer packet = GameBuffer.variableShort(81);
		packet.getBitBuffer().start(); // start the bit access
		
		// The buffer holding the attributes data
		GameBuffer buffer = GameBuffer.buffer();
		
		// Update our players movement and attributes
		player.getMovement().build(packet, true);
		if(player.isAttributesUpdate()) {
			player.getAttributes().build(buffer, false, true);
		}
		
		// Notify the client with the expected number of local players
		packet.getBitBuffer().put(8, data.getLocalPlayers().size());
		
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
				packet.getBitBuffer().put(true).put(2, 3);
				it.remove();
			}
		}
		
		// Loop through all of the players in the world and see if more have come into view
		for(Client other : world.getClients()) {
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
		
		// append the attributes buffer
		if(buffer.getBuffer().length() > 0) {
			packet.getBitBuffer().put(11, 2047).end();
			packet.putBuffer(buffer.getBuffer());
		} else {
			packet.getBitBuffer().end();
		}
		
		// return the built packet to send out
		return packet.getBuffer();
	}

}

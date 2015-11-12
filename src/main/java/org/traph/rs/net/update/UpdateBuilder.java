package org.traph.rs.net.update;

import java.util.Iterator;

import org.traph.rs.World;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.rs.net.update.block.UpdateBlockManager;
import org.traph.rs.net.update.player.PlayerAttributes;
import org.traph.rs.net.update.player.PlayerMovement;
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
	private final PlayerMovement playerMovement;
	private final PlayerAttributes playerAttributes;
	
	public UpdateBuilder(World world, Client client) {
		this.client = client;
		this.world = world;
		this.playerMovement = new PlayerMovement(client);
		this.playerAttributes = new PlayerAttributes(client);
	}
	
	public Buffer getPlayerPacket() {
		UpdateBlockManager manager = client.getGameData().getPlayer().getUpdateBlockManager();
		
		GameBuffer packet = GameBuffer.variableShort(81);
		packet.getBitBuffer().start(); // start the bit access
		
		getPlayerMovement().build(packet, true);
		if(manager.isAttributesUpdate()) {
			getPlayerAttributes().build(packet, false, true);
		}
		
		packet.getBitBuffer().put(8, client.getGameData().getLocalPlayers().size());
		
		for(Iterator<Player> it = client.getGameData().getLocalPlayers().iterator(); it.hasNext(); ) {
			Player other = it.next();
			
			// if viewable
			
			// otherwise
			packet.getBitBuffer().put(true).put(2, 3);
			it.remove();
		}
		
		for(Client other : world.getClients()) {
			if(client.getGameData().getLocalPlayers().size() >= 255) {
				break;
			}
			if(other == null || other == client) {
				continue;
			}
			
			// and viewable
			if(!client.getGameData().getLocalPlayers().contains(other.getGameData().getPlayer())) {
				client.getGameData().getLocalPlayers().add(other.getGameData().getPlayer());
				// add player
			}
			
			
		}
		
		//client.getUpdateBuilder().
		
		// update the player movement segment for the given client
	
		// player, block, forceAppearance, noChat
		
		// update the current player
		
		// update other players (basically send information about other players to ourselves)
		
		// add / remove players from the local list
		
		// append the attributes (state) segment to the packet
		
		return packet.getBuffer();
	}
	
	public PlayerMovement getPlayerMovement() {
		return playerMovement;
	}
	
	public PlayerAttributes getPlayerAttributes() {
		return playerAttributes;
	}

}

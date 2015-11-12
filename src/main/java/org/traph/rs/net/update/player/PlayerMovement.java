package org.traph.rs.net.update.player;

import org.traph.rs.model.MovementDirection;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.rs.net.update.UpdateSegment;
import org.traph.rs.net.update.block.player.Teleport;
import org.traph.util.net.BitBuffer;
import org.traph.util.net.GameBuffer;

/**
 * Represents the segment for updating the player's movement
 * 
 * @author Derek
 */
public class PlayerMovement implements UpdateSegment {
	
	private final Client client;
	
	public PlayerMovement(Client client) {
		this.client = client;
	}

	public void build(GameBuffer buffer, boolean local) {
		Player player = client.getGameData().getPlayer();
		if(local && player.getUpdateBlockManager().isFlagged(Teleport.class)) {
			player.getUpdateBlockManager().get(Teleport.class).build(client, buffer);
		} else {
			MovementDirection direction = player.getMovementDirection();
			BitBuffer buf = buffer.getBitBuffer();
			if(direction.isWalking()) {
				buf.put(true)
					.put(2, direction.isRunning() ? 2 : 1) // put whether we are running or walking
					.put(3, direction.getPrimary()); // put the first direction
				if(direction.isRunning()) {
					buf.put(3, direction.getSecondary()); // if running put section direction
				}
				buf.put(player.getUpdateBlockManager().isAttributesUpdate()); // put attributes update
			} else {
				if(player.getUpdateBlockManager().isAttributesUpdate()) {
					buf.put(true).put(2, 0); // we are standing
				} else {
					buf.put(false);
				}
			}
		}
	}

}

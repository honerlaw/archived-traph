package org.traph.rs.net.update.block.player;

import org.traph.rs.model.Region;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.util.net.GameBuffer;

public class Teleport extends UpdateBlock {
	
	private final boolean discardMovementQueue;
	
	public Teleport() {
		this(false);
	}
	
	public Teleport(boolean discardMovementQueue) {
		this.discardMovementQueue = discardMovementQueue;
	}
	
	@Override
	public void build(Client client, GameBuffer buffer) {
		Player player = client.getGameData().getPlayer();
		Region region = player.getRegion();
		buffer.getBitBuffer()
			.put(true) // we are updating the player movement
			.put(2, 3) // notify that we are updating placement
			.put(2, region.getLocation().getZ()) // send z coordinate
			.put(discardMovementQueue) // discard the movement queue?
			.put(player.getUpdateBlockManager().isAttributesUpdate()) // are we updating attributes as well (appearance blocks)
			.put(7, region.getLocalLocation().getY()) // send the region y coordinate
			.put(7, region.getLocalLocation().getZ()); // send the region x coordinate
	}

}

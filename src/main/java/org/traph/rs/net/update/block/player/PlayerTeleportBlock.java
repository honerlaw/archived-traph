package org.traph.rs.net.update.block.player;

import org.traph.rs.model.Entity;
import org.traph.rs.model.Region;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.util.net.GameBuffer;

public class PlayerTeleportBlock extends UpdateBlock {
	
	private boolean discardMovementQueue;
	
	public PlayerTeleportBlock(Entity entity) {
		super(entity);
	}

	@Override
	public void build(GameBuffer buffer) {
		Region region = getPlayer().getRegion();
		buffer
			.putBit(true) // we are updating the player movement
			.putBits(2, 3) // notify that we are updating placement
			.putBits(2, region.getLocation().getZ()) // send z coordinate
			.putBit(discardMovementQueue) // discard the movement queue?
			.putBit(getPlayer().isAttributesUpdate()) // are we updating attributes as well (appearance blocks)
			.putBits(7, region.getLocalLocation().getY()) // send the region y coordinate
			.putBits(7, region.getLocalLocation().getX()); // send the region x coordinate
	}

}

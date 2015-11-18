package org.traph.rs.net.update.block;

import org.traph.rs.model.Entity;
import org.traph.rs.model.player.Player;
import org.traph.util.net.GameBuffer;

/**
 * Represents an Update Block that needs to be appended somewhere
 * in the update packet
 * 
 * @author Derek
 */
public abstract class UpdateBlock {
	
	private final Entity entity;
	private int mask;
	private UpdateBlockData blockData;
	
	public UpdateBlock(Entity entity) {
		this(entity, -1);
	}
	
	public UpdateBlock(Entity entity, int mask) {
		this.entity = entity;
		this.mask = mask;
	}
	
	public Player getPlayer() {
		return (Player) entity;
	}
	
	public int getMask() {
		return mask;
	}
	
	public void setBlockData(UpdateBlockData blockData) {
		this.blockData = blockData;
	}
	
	public UpdateBlockData getBlockData() {
		return blockData;
	}
	
	public void reset() {
		this.blockData = null;
	}

	public abstract void build(GameBuffer buffer);
	
}

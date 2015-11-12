package org.traph.rs.model;

import org.traph.rs.net.update.block.UpdateBlockManager;

public abstract class Entity {
	
	private final UpdateBlockManager updateBlockManager;
	private Region region;
	private MovementDirection movementDirection;
	
	public Entity() {
		this.updateBlockManager = new UpdateBlockManager(this);
		this.region = new Region(3222, 3222, 0);
		this.movementDirection = MovementDirection.STANDING;
	}
	
	public UpdateBlockManager getUpdateBlockManager() {
		return updateBlockManager;
	}
	
	public void setRegion(Region region) {
		this.region = region;
	}
	
	public Region getRegion() {
		return region;
	}
	
	public void setMovementDirection(MovementDirection movementDirection) {
		this.movementDirection = movementDirection;
	}
	
	public MovementDirection getMovementDirection() {
		return movementDirection;
	}

}

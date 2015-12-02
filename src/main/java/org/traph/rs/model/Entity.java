package org.traph.rs.model;

import org.traph.rs.model.path.PathQueue;
import org.traph.rs.net.update.EntityUpdate;

public abstract class Entity extends EntityUpdate {
	
	private Region region;
	private MovementDirection movementDirection;
	private final PathQueue pathQueue;
	
	public Entity() {
		this.region = new Region(3222, 3222, 0);
		this.movementDirection = MovementDirection.STANDING;
		this.pathQueue = new PathQueue(this);
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
	
	public PathQueue getPathQueue() {
		return pathQueue;
	}

}

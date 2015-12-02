package org.traph.rs.model.path;

import org.traph.rs.model.Location;

public class PathPoint extends Location {
	
	private final int direction;

	public PathPoint(int x, int y, int direction) {
		super(x, y);
		this.direction = direction;
	}
	
	public PathPoint(Location loc, int direction) {
		this(loc.getX(), loc.getY(), direction);
	}
	
	public int getDirection() {
		return direction;
	}

}

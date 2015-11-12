package org.traph.rs.model;

public class Region {
	
	private final Location location;
	
	public Region(int x, int y, int z) {
		this.location = new Location(x, y, z);
	}
	
	public int getX() {
		return (location.getX() >> 3) - 6;
	}
	
	public int getY() {
		return (location.getY() >> 3) - 6;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public Location getLocalLocation() {
		return new Location(location.getX() - 8 * getX(), location.getY() - 8 * getY(), location.getZ());
	}

}

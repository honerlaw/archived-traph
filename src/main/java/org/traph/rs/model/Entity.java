package org.traph.rs.model;

public abstract class Entity {
	
	private Location location;
	
	public Entity() {
		location = new Location(3222, 3222, 0);
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Location getLocation() {
		return location;
	}

}

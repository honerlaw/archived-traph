package org.traph.rs.model;

public class Location {
	
	private final int x;
	
	private final int y;
	
	private final int z;
	
	public Location(int x, int y) {
		this(x, y, 0);
	}
	
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public int getX() {
		return x;
	}
	
	public int getZ() {
		return z;
	}
	
	public int getY() {
		return y;
	}
	
	public boolean isViewableFrom(Location other) {
		if(other.getZ() != getZ()) {
			return false;
		}
		int difX = other.getX() - getX();
		int difY = other.getY() - getY();
		return difX >= -15 && difX <= 14 && difY >= -15 && difY <= 14;
	}

}

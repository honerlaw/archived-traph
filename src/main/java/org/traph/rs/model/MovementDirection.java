package org.traph.rs.model;

public final class MovementDirection {
	
	public static final MovementDirection STANDING = new MovementDirection(-1, -1);
	
	private final int primary;
	private final int secondary;
	
	public MovementDirection(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}
	
	public int getPrimary() {
		return primary;
	}
	
	public int getSecondary() {
		return secondary;
	}
	
	public boolean isWalking() {
		return getPrimary() != -1;
	}
	
	public boolean isRunning() {
		return isWalking() && getSecondary() != -1;
	}

}

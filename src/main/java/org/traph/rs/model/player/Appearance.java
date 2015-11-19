package org.traph.rs.model.player;

public class Appearance {
	
	public static final int CHEST = 0;
	public static final int ARMS = 1;
	public static final int LEGS = 2;
	public static final int HEAD = 3;
	public static final int HANDS = 4;
	public static final int FEET = 5;
	public static final int BEARD = 6;
	
	private final int[] colors;
	
	private final int[] clothing;
	
	public Appearance() {
		this.colors = new int[] { 7, 8, 9, 5, 0 };
		this.clothing = new int[] { 18, 26, 36, 0, 33, 42, 10 };
	}
	
	public int[] getColors() {
		return colors;
	}
	
	public int[] getClothing() {
		return clothing;
	}
	
	public int getColor(int index) {
		return colors[index];
	}
	
	public int getClothing(int index) {
		return clothing[index];
	}

}

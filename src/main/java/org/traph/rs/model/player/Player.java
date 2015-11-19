package org.traph.rs.model.player;

import org.traph.rs.model.Entity;
import org.traph.rs.model.item.container.Equipment;
import org.traph.rs.model.item.container.Inventory;

public class Player extends Entity {
	
	private final int uid;
	
	private final String username;
	
	private final String password;
	
	private final Inventory inventory;
	
	private final Equipment equipment;
	
	private final Appearance appearance;
	
	private int index;
	
	public Player(int uid, String username, String password) {
		this.uid = uid;
		this.username = username;
		this.password = password;
		this.inventory = new Inventory();
		this.equipment = new Equipment();
		this.appearance = new Appearance();
	}
	
	public int getUid() {
		return uid;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public Inventory getInventory() {
		return inventory;
	}
	
	public Equipment getEquipment() {
		return equipment;
	}
	
	public Appearance getAppearance() {
		return appearance;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}

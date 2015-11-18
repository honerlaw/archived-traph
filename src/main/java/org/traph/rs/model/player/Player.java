package org.traph.rs.model.player;

import org.traph.rs.model.Entity;

public class Player extends Entity {
	
	private final int uid;
	
	private final String username;
	
	private final String password;
	
	private int index;
	
	public Player(int uid, String username, String password) {
		this.uid = uid;
		this.username = username;
		this.password = password;
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
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}

}

package org.traph.rs.net;

import java.util.ArrayList;
import java.util.List;

import org.traph.rs.model.player.Player;

public class GameData {
	
	private Player player;
	private List<Player> localPlayers;
	
	public GameData() {
		this.localPlayers = new ArrayList<Player>();
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public List<Player> getLocalPlayers() {
		return localPlayers;
	}

}

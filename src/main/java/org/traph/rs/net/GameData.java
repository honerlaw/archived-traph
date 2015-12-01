package org.traph.rs.net;

import java.util.ArrayList;
import java.util.List;

import org.traph.rs.World;
import org.traph.rs.game.ActionManager;
import org.traph.rs.model.player.Player;

public class GameData {
	
	private final List<Player> localPlayers;
	private final ActionManager actionManager;
	private Player player;
	
	public GameData(World world) {
		this.localPlayers = new ArrayList<Player>();
		this.actionManager = new ActionManager(world);
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
	
	public ActionManager getActionManager() {
		return actionManager;
	}

}

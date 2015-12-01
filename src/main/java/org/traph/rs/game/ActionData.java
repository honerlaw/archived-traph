package org.traph.rs.game;

import org.traph.rs.World;
import org.traph.rs.net.Client;

public final class ActionData {
	
	private final World world;
	
	private final Client client;
	
	public ActionData(World world, Client client) {
		this.world = world;
		this.client = client;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Client getClient() {
		return client;
	}
	
}
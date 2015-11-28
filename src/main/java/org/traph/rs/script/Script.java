package org.traph.rs.script;

import org.traph.rs.World;

/**
 * Injected into a script that is being executed
 * 
 * This class mainly allows us to interact with the game engine
 * 
 * @author Derek
 */
public class Script {
	
	private final World world;
	
	public Script(World world) {
		this.world = world;
	}
	
	public World getWorld() {
		return world;
	}

}

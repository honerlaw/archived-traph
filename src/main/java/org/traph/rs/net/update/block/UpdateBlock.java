package org.traph.rs.net.update.block;

import org.traph.rs.net.Client;
import org.traph.util.net.GameBuffer;

/**
 * Represents an Update Block that needs to be appended somewhere
 * in the update packet
 * 
 * @author Derek
 */
public abstract class UpdateBlock {
	
	public abstract void build(Client client, GameBuffer buffer);

}

package org.traph.rs.net.worker;

import org.traph.rs.World;
import org.traph.rs.net.Client;

public class Update {
	
	public static final void update(World world) {
		
		// all this does is send current data to the client
		// no data is updated here
		
		// we update each client in the world
		for(Client client : world.getClients()) {
			if(client == null) {
				continue;
			}
			
			// initialize the packet
			
			// update the current player
			
			// update other players (basically send information about other players to ourselves)
			
			// add / remove players from the local list
			
			// append the attributes (state) segment to the packet
			
		}
	}

}

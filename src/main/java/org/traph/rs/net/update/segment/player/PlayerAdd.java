package org.traph.rs.net.update.segment.player;

import org.traph.rs.model.Entity;
import org.traph.rs.net.update.segment.SegmentAdd;
import org.traph.util.net.GameBuffer;

public class PlayerAdd extends SegmentAdd {

	public PlayerAdd(Entity entity) {
		super(entity);
	}

	@Override
	public void build(GameBuffer buffer, Entity other) {

		// calculate the relative position
		int x = getPlayer().getRegion().getLocation().getX() - other.getRegion().getLocation().getX();
		int y = getPlayer().getRegion().getLocation().getY() - other.getRegion().getLocation().getY();
		
		// write the data to the buffer
		buffer.getBitBuffer()
			.put(11, getPlayer().getIndex()) // The client / server index
			.put(true) // Yes, an update is required.
			.put(true) // Discard walking queue(?)
			.put(5, y) // write the relative y coordinate
			.put(5, x); // write the relative x coordinate
	}

}

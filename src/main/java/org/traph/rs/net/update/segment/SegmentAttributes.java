package org.traph.rs.net.update.segment;

import org.traph.rs.model.Entity;
import org.traph.rs.model.player.Player;
import org.traph.util.net.GameBuffer;

public abstract class SegmentAttributes {

	private final Entity entity;
	
	public SegmentAttributes(Entity entity) {
		this.entity = entity;
	}
	
	public Player getPlayer() {
		return (Player) entity;
	}
	
	public abstract void build(GameBuffer buffer, boolean forceAppearance, boolean noChat);

}

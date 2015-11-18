package org.traph.rs.net.update.segment;

import org.traph.rs.model.Entity;
import org.traph.rs.model.player.Player;
import org.traph.util.net.GameBuffer;

public abstract class SegmentAdd {
	
	private final Entity entity;
	
	public SegmentAdd(Entity entity) {
		this.entity = entity;
	}
	
	public Player getPlayer() {
		return (Player) entity;
	}
	
	public abstract void build(GameBuffer buffer, Entity other);
	
}

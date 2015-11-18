package org.traph.rs.net.update.block.player;

import org.traph.rs.model.Entity;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.util.net.GameBuffer;

public class PlayerAppearanceBlock extends UpdateBlock {

	public PlayerAppearanceBlock(Entity entity) {
		super(entity, 0x10);
	}

	@Override
	public void build(GameBuffer buffer) {
		
	}

}

package org.traph.rs.net.update.block.player;

import org.traph.rs.model.Entity;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.util.net.GameBuffer;

public class PlayerChatBlock extends UpdateBlock {

	public PlayerChatBlock(Entity entity) {
		super(entity, 0x80);
	}

	@Override
	public void build(GameBuffer buffer) {
		
	}

}

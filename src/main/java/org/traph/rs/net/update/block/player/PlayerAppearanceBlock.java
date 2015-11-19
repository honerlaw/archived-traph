package org.traph.rs.net.update.block.player;

import org.traph.rs.model.Entity;
import org.traph.rs.model.player.Appearance;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.util.StringUtil;
import org.traph.util.net.GameBuffer;

public class PlayerAppearanceBlock extends UpdateBlock {

	public PlayerAppearanceBlock(Entity entity) {
		super(entity, 0x10);
	}

	@Override
	public void build(GameBuffer buffer) {

		GameBuffer block = GameBuffer.buffer();
		
		// write head icons
		block
			.put(0) // gender
			.put(0); // skull
		
		// here we would check equipment and see if we are wearing something
		block
			.put(0) // head
			.put(0) // cape
			.put(0) // amulet
			.put(0) // weapon
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.CHEST)) // chest
			.put(0) // shield
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.ARMS)) // arms
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.LEGS)) // legs
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.HEAD)) // head with hat
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.HANDS)) // hands
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.FEET)) // feet
			.putShort(0x100 + getPlayer().getAppearance().getClothing(Appearance.BEARD)); // beard
		
		// write out the colors
		for(int color : getPlayer().getAppearance().getColors()) {
			block.put(color);
		}
		
		// movement animations (changes when equipment changes)
		block
			.putShort(0x328) // stand
			.putShort(0x337) // stand turn
			.putShort(0x333) // walk
			.putShort(0x334) // turn 180
			.putShort(0x335) // turn 90 cw
			.putShort(0x336) // turn 90 ccw
			.putShort(0x338); // run
		
		block
			.putLong(StringUtil.getStringAsLong(getPlayer().getUsername())) // username
			.put(3) // combat level
			.put(0); // total level
		
		buffer.put(block.getRawBuffer().getByteBuf().writerIndex());
		buffer.putBuffer(block.getRawBuffer());
	}

}

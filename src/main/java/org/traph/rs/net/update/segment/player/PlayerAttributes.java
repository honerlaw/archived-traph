package org.traph.rs.net.update.segment.player;

import java.util.Iterator;

import org.traph.rs.model.Entity;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.update.EntityUpdate.UpdateFlag;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.rs.net.update.segment.SegmentAttributes;
import org.traph.util.net.GameBuffer;

public class PlayerAttributes extends SegmentAttributes {

	public PlayerAttributes(Entity entity) {
		super(entity);
	}

	@Override
	public void build(GameBuffer buffer, boolean forceAppearance, boolean noChat) {
		Player player = getPlayer();
		
		// build the update block
		GameBuffer temp = GameBuffer.buffer();
		
		// build the mask and write the blocks to the buffer
		int mask = 0;
		for(Iterator<UpdateFlag> it = player.getFlags().iterator(); it.hasNext(); ) {
			UpdateFlag flag = it.next();
			UpdateBlock block = player.getBlock(flag);
			if(block.getMask() != -1) {
				
				// we do not want to display chat
				if(noChat) {
					
					// so if the chat flag is set ignore it
					if(flag != UpdateFlag.CHAT) {
						mask |= block.getMask();
						block.build(temp);
					}
				} else {
					
					// otherwise update the chat flag
					mask |= block.getMask();
					block.build(temp);
				}
			}
		}
		
		// The player did not have an appearance update but we want to force one
		if(!player.isSet(UpdateFlag.APPEARANCE) && forceAppearance) {
			UpdateBlock block = player.getBlock(UpdateFlag.APPEARANCE);
			mask |= block.getMask();
			block.build(temp);
		}
		
		// write the mask value
		if (mask >= 0x100) {
			mask |= 0x40;
			buffer.putShort(mask);
		} else {
			buffer.put(mask);
		}
		
		// write the attributes buffer to the main buffer
		buffer.putBuffer(temp.getRawBuffer());
	}

}

package org.traph.rs.net.update.player;

import java.util.Iterator;

import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.rs.net.update.UpdateSegment;
import org.traph.rs.net.update.block.AttributeBlock;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.rs.net.update.block.UpdateBlockManager;
import org.traph.rs.net.update.block.player.attr.Appearance;
import org.traph.rs.net.update.block.player.attr.Chat;
import org.traph.util.net.GameBuffer;

/**
 * Basically handles building the attributes segment
 * Since this instance is unique to each client we can
 * cache certain blocks here without an issue (since we
 * can't cache the actual block).
 * 
 * @author Derek
 */
public class PlayerAttributes implements UpdateSegment {
	
	private final Client client;
	
	public PlayerAttributes(Client client) {
		this.client = client;
	}
	
	// so what if i treat everything as an individual instead of parts as a whole?

	public void build(GameBuffer buffer, boolean forceAppearance, boolean noChat) {
		Player player = client.getGameData().getPlayer();
		UpdateBlockManager manager = player.getUpdateBlockManager();
		
		// build the update block
		GameBuffer temp = GameBuffer.buffer();
		
		// build the mask value
		int mask = 0x0;
		for(Iterator<UpdateBlock> it = manager.getUpdateBlocks().iterator(); it.hasNext();) {
			UpdateBlock block = it.next();
			if(block instanceof AttributeBlock) {
				AttributeBlock b = ((AttributeBlock) block);
				if(b instanceof Chat && !noChat) {
					mask |= b.getMask();
					b.build(client, temp);
				} else if(b instanceof Appearance || forceAppearance) {
					mask |= b.getMask();
					b.build(client, temp);
				} else {
					mask |= b.getMask();
					b.build(client, temp);
				}
			}
		}
		
		// write the mask value
		if (mask >= 0x100) {
			mask |= 0x40;
			buffer.putShort(mask);
		} else {
			buffer.put(mask);
		}
		buffer.putBuffer(temp.getBuffer(true));
		
		// Graphics
		// Animation
		// Forced chat
		// Face entity
		// Appearance
	}

}

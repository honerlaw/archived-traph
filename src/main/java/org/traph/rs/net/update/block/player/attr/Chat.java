package org.traph.rs.net.update.block.player.attr;

import org.traph.rs.net.Client;
import org.traph.rs.net.update.block.AttributeBlock;
import org.traph.util.net.GameBuffer;

public class Chat extends AttributeBlock {

	public Chat() {
		super(0x80);
	}

	@Override
	public void build(Client client, GameBuffer buffer) {

	}

}

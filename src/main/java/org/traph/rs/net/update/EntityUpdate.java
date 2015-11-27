package org.traph.rs.net.update;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.traph.rs.model.Entity;
import org.traph.rs.model.player.Player;
import org.traph.rs.net.update.block.UpdateBlock;
import org.traph.rs.net.update.block.UpdateBlockData;
import org.traph.rs.net.update.block.player.PlayerAppearanceBlock;
import org.traph.rs.net.update.block.player.PlayerChatBlock;
import org.traph.rs.net.update.block.player.PlayerTeleportBlock;
import org.traph.rs.net.update.segment.SegmentAdd;
import org.traph.rs.net.update.segment.SegmentAttributes;
import org.traph.rs.net.update.segment.SegmentMovement;
import org.traph.rs.net.update.segment.player.PlayerAdd;
import org.traph.rs.net.update.segment.player.PlayerAttributes;
import org.traph.rs.net.update.segment.player.PlayerMovement;

public abstract class EntityUpdate {
	
	public static enum UpdateFlag {
		TELEPORT, APPEARANCE, CHAT
	}
	
	private final EnumSet<UpdateFlag> flags;
	private final Map<UpdateFlag, UpdateBlock> blocks;
	private SegmentAttributes attributes;
	private SegmentMovement movement;
	private SegmentAdd add;
	
	public EntityUpdate() {
		this.flags = EnumSet.noneOf(UpdateFlag.class);
		this.blocks = new HashMap<UpdateFlag, UpdateBlock>();
		if(this instanceof Player) {
			this.attributes = new PlayerAttributes((Entity) this);
			this.movement = new PlayerMovement((Entity) this);
			this.add = new PlayerAdd((Entity) this);
			this.blocks.put(UpdateFlag.APPEARANCE, new PlayerAppearanceBlock((Entity) this));
			this.blocks.put(UpdateFlag.CHAT, new PlayerChatBlock((Entity) this));
			this.blocks.put(UpdateFlag.TELEPORT, new PlayerTeleportBlock((Entity) this));
		}
	}
	
	public EnumSet<UpdateFlag> getFlags() {
		return flags;
	}
	
	public UpdateBlock getBlock(UpdateFlag flag) {
		return blocks.get(flag);
	}
	
	public void set(UpdateFlag... updateFlags) {
		for(UpdateFlag flag : updateFlags) {
			set(flag, null);
		}
	}
	
	public void set(UpdateFlag flag, UpdateBlockData blockData) {
		if(!flags.contains(flag)) {
			flags.add(flag);
			getBlock(flag).setBlockData(blockData);
		}
	}
	
	public boolean isSet(UpdateFlag flag) {
		return flags.contains(flag);
	}
	
	public void reset() {
		flags.clear();
	}
	
	public boolean isAttributesUpdate() {
		return isSet(UpdateFlag.APPEARANCE) || isSet(UpdateFlag.CHAT);
	}
	
	public SegmentAttributes getAttributes() {
		return attributes;
	}
	
	public SegmentMovement getMovement() {
		return movement;
	}
	
	public SegmentAdd getAdd() {
		return add;
	}

}

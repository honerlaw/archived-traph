package org.traph.rs.net.update.block;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class UpdateBlockManager {
	
	private final Map<Class<? extends UpdateBlock>, UpdateBlock> updateBlocks;
	private boolean attributesUpdate;
	
	public UpdateBlockManager() {
		this.updateBlocks = new HashMap<Class<? extends UpdateBlock>, UpdateBlock>();
	}
	
	public UpdateBlock get(Class<? extends UpdateBlock> block) {
		return updateBlocks.get(block);
	}

	public UpdateBlockManager set(UpdateBlock block) {
		if(block instanceof AttributeBlock) {
			attributesUpdate = true;
		}
		updateBlocks.put(block.getClass(), block);
		return this;
	}
	
	public boolean isFlagged(Class<? extends UpdateBlock> block) {
		return get(block) != null;
	}
	
	public boolean isAttributesUpdate() {
		return attributesUpdate;
	}
	
	public Collection<UpdateBlock> getUpdateBlocks() {
		return updateBlocks.values();
	}

}

package org.traph.rs.net.update.block;

/**
 * Represents an AttributeBlock
 * 
 * @author Derek
 */
public abstract class AttributeBlock extends UpdateBlock {
	
	private final int mask;
	
	public AttributeBlock(int mask) {
		this.mask = mask;
	}
	
	public int getMask() {
		return mask;
	}

}

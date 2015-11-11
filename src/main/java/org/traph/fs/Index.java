package org.traph.fs;

import org.traph.fs.archive.Archive;

public final class Index {

	private final int size;
	
	private final int blockPosition;
	
	private final Archive archive;
	
	public Index(int size, int blockPosition, Archive archive) {
		this.size = size;
		this.blockPosition = blockPosition;
		this.archive = archive;
	}
	
	public int getSize() {
		return size;
	}
	
	public int getBlockPosition() {
		return blockPosition;
	}
	
	public Archive getArchive() {
		return archive;
	}

}
package org.traph.fs.archive;

import io.vertx.core.buffer.Buffer;

public final class ArchiveEntry {
	
	private final int hash;
	
	public final Buffer buffer;
	
	public ArchiveEntry(int hash, Buffer buffer) {
		this.hash = hash;
		this.buffer = buffer;
	}
	
	public int getHash() {
		return hash;
	}
	
	public Buffer getBuffer() {
		return buffer;
	}

}
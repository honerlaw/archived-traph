package org.traph.fs.archive;

import java.io.FileNotFoundException;

import org.traph.util.fs.Decompressor;

public final class Archive {
	
	private final ArchiveFile archiveFile;
	
	private final ArchiveEntry[] entries;
	
	public Archive(ArchiveFile archiveFile) {
		this.archiveFile = archiveFile;
		this.entries = Decompressor.decompress(archiveFile);
	}

	public ArchiveFile getArchiveFile() {
		return archiveFile;
	}
	
	public ArchiveEntry[] getEntries() {
		return entries;
	}
	
	public ArchiveEntry getEntry(String name) throws FileNotFoundException {
		int hash = hash(name);
		for (ArchiveEntry entry : entries) {
			if (entry.getHash() == hash) {
				return entry;
			}
		}
		throw new FileNotFoundException("Could not find entry: " + name + ".");
	}
	
	private int hash(String name) {
		return name.toUpperCase().chars().reduce(0, (hash, character) -> hash * 61 + character - 32);
	}

}
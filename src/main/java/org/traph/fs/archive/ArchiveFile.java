package org.traph.fs.archive;

import java.io.IOException;

import org.traph.util.Constant;

import io.vertx.core.buffer.Buffer;

public final class ArchiveFile {
	
	private final int size;
	
	private final Buffer buffer;
	
	private ArchiveFile(int size, Buffer buffer) {
		this.size = size;
		this.buffer = buffer;
	}
	
	/**
	 * Gets the archive file information before the file is decompressed
	 * and the entries are discovered
	 * 
	 * @param index The file index
	 * @param size The size of the ArchiveFile block
	 * @param block The id of the ArchiveFile block
	 * @param buffer The data file buffer
	 * @return An ArchiveFile
	 * @throws IOException 
	 */
	public static ArchiveFile file(int indexFileId, int size, final int blockPos, final int resourceId, Buffer dataFile) {		
		Buffer file = Buffer.buffer(size);
		int position = blockPos * Constant.FileSystem.ARCHIVE_BLOCK_SIZE;
		int blocks = size / Constant.FileSystem.ARCHIVE_CHUNK_SIZE;
		if(size % Constant.FileSystem.ARCHIVE_CHUNK_SIZE != 0) {
			blocks++;
		}
		
		int remaining = size;
		for(int i = 0; i < blocks; ++i) {
			
			byte[] header = dataFile.getBytes(position, position + Constant.FileSystem.ARCHIVE_HEADER_SIZE);
			position += Constant.FileSystem.ARCHIVE_HEADER_SIZE;
			
			int nextResourceId = (header[0] & 0xFF) << 8 | header[1] & 0xFF;
			int curChunk = (header[2] & 0xFF) << 8 | header[3] & 0xFF;
			int nextBlock = (header[4] & 0xFF) << 16 | (header[5] & 0xFF) << 8 | header[6] & 0xFF;
			int nextFileId = header[7] & 0xFF;
			
			if(i != curChunk) {
				System.err.println("Chunk id mismatch");
			}
			
			int chunkSize = remaining;
			if(chunkSize > Constant.FileSystem.ARCHIVE_CHUNK_SIZE) {
				chunkSize = Constant.FileSystem.ARCHIVE_CHUNK_SIZE;
			}
			
			file.appendBytes(dataFile.getBytes(position, position + chunkSize));
			
			remaining -= chunkSize;
			position = nextBlock * Constant.FileSystem.ARCHIVE_BLOCK_SIZE;
			
			if(remaining > 0) {
				if(nextFileId != indexFileId + 1) {
					System.err.println("Index file mismatch.");
				}
				if(nextResourceId != resourceId) {
					System.err.println("Data file block id mismatch.");
				}
			}
		}
		return new ArchiveFile(size, file.copy());
	}
	
	/**
	 * 
	 * @return The size of the archive file
	 */
	public int getSize() {
		return size;
	}
	
	/**
	 *
	 * @return The buffer backing this archive file
	 */
	public Buffer getBuffer() {
		return buffer.copy();
	}

}
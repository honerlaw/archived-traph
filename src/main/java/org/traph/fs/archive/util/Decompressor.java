package org.traph.fs.archive.util;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.traph.fs.archive.ArchiveEntry;
import org.traph.fs.archive.ArchiveFile;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

/**
 * Decompresses an archive file and generates the archive entries in the 
 * given archive file.
 * 
 * Most of this class was ripped from Apollo.
 * 
 * @author Graham
 * @author honerlad
 *
 */
public class Decompressor {
	
	
	public static ArchiveEntry[] decompress(ArchiveFile file) {
		ByteBuf buffer = file.getBuffer().getByteBuf();
		if(file.getSize() == 0 || buffer.readableBytes() < 6) {
			return null;
		}
		
		int extractedSize = (buffer.readShort() & 0xFFFF) << 8 | buffer.readByte() & 0xFF;
		int size = (buffer.readShort() & 0xFFFF) << 8 | buffer.readByte() & 0xFF;
		if(size == 0) {
			return null;
		}
		
		boolean extracted = false;
		if (size != extractedSize) {
			byte[] compressed = new byte[size];
			byte[] decompressed = new byte[extractedSize];
			buffer.readBytes(compressed);
			try {
				debzip2(compressed, decompressed);
			} catch (IOException e) {
				e.printStackTrace();
			}
			buffer = Buffer.buffer(decompressed).getByteBuf();
			extracted = true;
		}

		int entryCount = buffer.readShort() & 0xFFFF;
		int[] identifiers = new int[entryCount];
		int[] extractedSizes = new int[entryCount];
		int[] sizes = new int[entryCount];

		for (int i = 0; i < entryCount; i++) {
			identifiers[i] = buffer.readInt();
			extractedSizes[i] = (buffer.readShort() & 0xFFFF) << 8 | buffer.readByte() & 0xFF;
			sizes[i] = (buffer.readShort() & 0xFFFF) << 8 | buffer.readByte() & 0xFF;
		}

		ArchiveEntry[] entries = new ArchiveEntry[entryCount];
		for (int entry = 0; entry < entryCount; entry++) {
			Buffer entryBuffer;
			if (!extracted) {
				byte[] compressed = new byte[sizes[entry]];
				byte[] decompressed = new byte[extractedSizes[entry]];
				buffer.readBytes(compressed);
				try {
					debzip2(compressed, decompressed);
				} catch (IOException e) {
					e.printStackTrace();
				}
				entryBuffer = Buffer.buffer(decompressed);
			} else {
				byte[] buf = new byte[extractedSizes[entry]];
				buffer.readBytes(buf);
				entryBuffer = Buffer.buffer(buf);
			}
			entries[entry] = new ArchiveEntry(identifiers[entry], entryBuffer);
		}
		
		return entries;
	}

	/**
	 * Debzip2s the compressed array and places the result into the decompressed array.
	 *
	 * @param compressed The compressed array, <strong>without</strong> the header.
	 * @param decompressed The decompressed array.
	 * @throws IOException If there is an error decompressing the array.
	 */
	public static void debzip2(byte[] compressed, byte[] decompressed) throws IOException {
		byte[] newCompressed = new byte[compressed.length + 4];
		newCompressed[0] = 'B';
		newCompressed[1] = 'Z';
		newCompressed[2] = 'h';
		newCompressed[3] = '1';
		System.arraycopy(compressed, 0, newCompressed, 4, compressed.length);
		try (DataInputStream is = new DataInputStream(new BZip2CompressorInputStream(new ByteArrayInputStream(newCompressed)))) {
			is.readFully(decompressed);
		}
	}

	/**
	 * Default private constructor to prevent instantiation.
	 */
	private Decompressor() {

	}

}
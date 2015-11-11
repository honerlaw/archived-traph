package org.traph.fs;

import java.util.Arrays;
import java.util.Map;
import java.util.zip.CRC32;

import org.traph.util.Constant;

import io.netty.buffer.ByteBuf;
import io.vertx.core.buffer.Buffer;

public final class CrcTable {
	
	private final Buffer buffer;
	
	private final int[] crcs;
	
	private CrcTable(Buffer buffer, int[] crcs) {
		this.buffer = buffer;
		this.crcs = crcs;
	}
	
	public static CrcTable create(Map<Integer, Index[]> indexMap) {
		Index[] indexArray = indexMap.get(0);
		int archives = indexArray.length;
		int hash = Constant.FileSystem.CRC_HASH;
		int[] crcs = new int[archives];
		CRC32 crc32 = new CRC32();
		for (int i = 1; i < crcs.length; i++) {
			crc32.reset();
			Buffer buffer = indexArray[i].getArchive().getArchiveFile().getBuffer();
			byte[] bytes = buffer.getBytes();
			crc32.update(bytes, 0, bytes.length);
			crcs[i] = (int) crc32.getValue();
		}
		
		Buffer buffer = Buffer.buffer((crcs.length + 1) * Integer.BYTES);
		for (int crc : crcs) {
			hash = (hash << 1) + crc;
			buffer.appendInt(crc);
		}
		buffer.appendInt(hash);
		return new CrcTable(buffer, getCrcsAsIntArray(buffer));
	}
	
	private static int[] getCrcsAsIntArray(Buffer buffer) {
		ByteBuf buf = buffer.copy().getByteBuf();
		int[] crcs = new int[(buf.readableBytes() / Integer.BYTES) - 1];
		Arrays.setAll(crcs, crc -> buf.readInt());
		return crcs;
	}
	
	public Buffer getBuffer() {
		return buffer.copy();
	}
	
	public int[] getCrcs() {
		return crcs;
	}

}
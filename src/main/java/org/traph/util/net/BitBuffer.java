package org.traph.util.net;

import org.traph.util.Constant;

import io.vertx.core.buffer.Buffer;

/**
 * Allows us to write bits to the 
 * 
 * @author Derek
 */
public class BitBuffer {
	
	private int position;
	private Buffer buffer;
	
	public BitBuffer(Buffer buffer) {
		this.buffer = buffer;
		position = buffer.getByteBuf().writerIndex() * 8;
	}
	
	public BitBuffer put(int amount, int value) {
		if (amount < 0 || amount > 32) {
			throw new IllegalArgumentException("Number of bits must be between 1 and 32 inclusive.");
		}

		int bytePos = position >> 3;
		int bitOffset = 8 - (position & 7);
		position = position + amount;
		
		for (; amount > bitOffset; bitOffset = 8) {
			byte tmp = buffer.getByte(bytePos);
			tmp &= ~Constant.Packet.BIT_MASK[bitOffset];
			tmp |= (value >> (amount - bitOffset)) & Constant.Packet.BIT_MASK[bitOffset];
			buffer.setByte(bytePos++, tmp);
			amount -= bitOffset;
		}
		if (amount == bitOffset) {
			byte tmp = buffer.getByte(bytePos);
			tmp &= ~Constant.Packet.BIT_MASK[bitOffset];
			tmp |= value & Constant.Packet.BIT_MASK[bitOffset];
			buffer.setByte(bytePos, tmp);
		} else {
			byte tmp = buffer.getByte(bytePos);
			tmp &= ~(Constant.Packet.BIT_MASK[amount] << (bitOffset - amount));
			tmp |= (value & Constant.Packet.BIT_MASK[amount]) << (bitOffset - amount);
			buffer.setByte(bytePos, tmp);
		}
		return this;
	}
	
	public BitBuffer put(boolean flag) {
		return put(1, flag ? 1 : 0);
	}
	
	public Buffer getBuffer() {
		buffer.getByteBuf().writerIndex((position + 7) / 8);
		return buffer;
	}

}

package org.traph.util.net;

import org.traph.util.Constant;

import io.vertx.core.buffer.Buffer;

/**
 * Allows us to write bits to the Buffer
 * 
 * @author Derek
 */
public class BitBuffer {
	
	private int position;
	private Buffer buffer;
	
	public BitBuffer(Buffer buffer) {
		this.buffer = buffer;
		this.position = -1;
	}
	
	public BitBuffer start() {
		position = buffer.getByteBuf().writerIndex() * 8;
		return this;
	}
	
	public BitBuffer put(int amount, int value) {
		if(position == -1) {
			throw new IllegalStateException("Bit access has not been started.");
		}
		if (amount < 0 || amount > 32) {
			throw new IllegalArgumentException("Number of bits must be between 1 and 32 inclusive.");
		}

		int bytePos = position >> 3;
		int bitOffset = 8 - (position & 7);
		position = position + amount;
		
		int requiredSpace = (bytePos - buffer.getByteBuf().writerIndex() + 1) + ((amount + 7) / 8);		
		if (buffer.getByteBuf().writableBytes() < requiredSpace) {
			Buffer old = buffer.copy();
			buffer = Buffer.buffer(buffer.getByteBuf().writerIndex() + requiredSpace).appendBuffer(old);
		}
		
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
	
	public BitBuffer end() {
		buffer.getByteBuf().writerIndex((position + 7) / 8);
		position = -1;
		return this;
	}

}

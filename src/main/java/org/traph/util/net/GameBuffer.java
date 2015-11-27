package org.traph.util.net;

import org.traph.rs.net.Client;
import org.traph.util.Constant;

import io.vertx.core.buffer.Buffer;

public class GameBuffer {
	
	private enum Type {
		FIXED, VARIABLE_BYTE, VARIABLE_SHORT
	}
	
	private Buffer buffer;
	private Type type;
	private int position;
	
	public static GameBuffer buffer() {
		return new GameBuffer();
	}
	
	public static GameBuffer fixed(int opcode) {
		return new GameBuffer(opcode, Type.FIXED);
	}
	
	public static GameBuffer variableByte(int opcode) {
		return new GameBuffer(opcode, Type.VARIABLE_BYTE);
	}
	
	public static GameBuffer variableShort(int opcode) {
		return new GameBuffer(opcode, Type.VARIABLE_SHORT);
	}
	
	private GameBuffer() {
		this.buffer = Buffer.buffer();
		this.type = Type.FIXED;
	}
	
	private GameBuffer(int opcode, Type type) {
		this();
		put(opcode);
		this.type = type;
		if(type == Type.VARIABLE_BYTE) {
			put(0);
		} else if(type == Type.VARIABLE_SHORT) {
			putShort(0);
		}
	}
	
	public GameBuffer startBits() {
		position = buffer.getByteBuf().writerIndex() * 8;
		return this;
	}
	
	public GameBuffer putBits(int amount, int value) {
		if(position == -1) {
			throw new IllegalStateException("Bit access has not been started.");
		}
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
	
	public GameBuffer putBit(boolean flag) {
		return putBits(1, flag ? 1 : 0);
	}
	
	public GameBuffer endBits() {
		buffer.getByteBuf().writerIndex((position + 7) / 8);
		position = -1;
		return this;
	}
	
	public GameBuffer put(int value) {
		buffer.appendByte((byte) value);
		return this;
	}
	
	public GameBuffer putC(int value) {
		return put(-value);
	}
	
	public GameBuffer putShort(int value) {
		buffer.appendShort((short) value);
		return this;
	}
	
	public GameBuffer putShortA(int value) {
		return put(value >> 8).put(value + 128);
	}
	
	public GameBuffer putInt(int value) {
		buffer.appendInt(value);
		return this;
	}
	
	public GameBuffer putLong(long value) {
		buffer.appendLong(value);
		return this;
	}
	
	public GameBuffer putString(String value) {
		return putBytes(value.getBytes()).put(10);
	}
	
	public GameBuffer putBytes(byte[] data) {
		buffer.appendBytes(data);
		return this;
	}
	
	public GameBuffer putBuffer(Buffer buffer) {
		this.buffer.appendBuffer(buffer);
		return this;
	}
	
	public Buffer getRawBuffer() {
		return getBuffer(null);
	}
	
	public Buffer getBuffer(Client client) {
		if(client != null) {
			buffer.setByte(0, (byte) (buffer.getByte(0) + client.getIsaacEncoder().nextInt()));
			if(type == Type.VARIABLE_BYTE) {
				buffer.setByte(1, (byte) (buffer.getByteBuf().writerIndex() - 2));
			} else if(type == Type.VARIABLE_SHORT) {				
				buffer.setShort(1, (short) (buffer.getByteBuf().writerIndex() - 3));				
			}
		}
		return buffer;
	}

}

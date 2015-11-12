package org.traph.util.net;

import io.vertx.core.buffer.Buffer;

public class GameBuffer {
	
	private enum Type {
		FIXED, VARIABLE_BYTE, VARIABLE_SHORT
	}
	
	private Buffer buffer;
	private Type type = Type.FIXED;
	
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
	
	public GameBuffer put(int value) {
		buffer.appendByte((byte) value);
		return this;
	}
	
	public GameBuffer putShort(int value) {
		buffer.appendShort((short) value);
		return this;
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
	
	public BitBuffer toBitBuffer() {
		return new BitBuffer(buffer);
	}
	
	public Buffer getBuffer() {
		return getBuffer(false);
	}
	
	public Buffer getBuffer(boolean raw) {
		if(!raw) {
			if(type == Type.VARIABLE_BYTE) {
				buffer.setByte(1, (byte) (buffer.getByteBuf().writerIndex() - 2));
			} else if(type == Type.VARIABLE_SHORT) {
				buffer.setShort(1, (short) (buffer.getByteBuf().writerIndex() - 2));
			}
		}
		return buffer;
	}

}

package org.traph.rs.net.packet;

import org.traph.util.net.GameBuffer;

public class Packet {
	
	private final int opcode;
	
	private final int size;
	
	private final GameBuffer payload;
	
	public Packet(int opcode, int size, GameBuffer payload) {
		this.opcode = opcode;
		this.size = size;
		this.payload = payload;
	}
	
	public int getOpcode() {
		return opcode;
	}
	
	public int getSize() {
		return size;
	}
	
	public GameBuffer getPayload() {
		return payload;
	}

}

package org.traph.rs.net.worker;

import java.util.ArrayList;
import java.util.List;

import org.traph.rs.net.Client;
import org.traph.rs.net.packet.Packet;
import org.traph.util.Constant;
import org.traph.util.net.GameBuffer;

import io.netty.buffer.ByteBuf;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class Decoder implements Handler<Future<List<Packet>>> {

	private final Client client;
	private final Buffer request;
	
	public Decoder(Client client, Buffer request) {
		this.client = client;
		this.request = request;
	}
	
	@Override
	public void handle(Future<List<Packet>> future) {
		ByteBuf buf = request.getByteBuf();
		
		
		List<Packet> packets = new ArrayList<Packet>();
		
		while(buf.isReadable()) {
			
			int opcode = -1;
			int size = -1;
			
			// read opcode
			if(opcode == -1) {
				opcode = buf.readByte() & 0xFF;
				opcode = (opcode - client.getIsaacDecoder().nextInt()) & 0xFF;
			}
			
			// read size
			if(size == -1) {
				size = Constant.Packet.PACKET_LENGTHS[opcode];
				if(size == -1) {
					if(!buf.isReadable()) {
						break;
					}
					size = buf.readByte() & 0xFF;
				}
			}
			
			
			packets.add(new Packet(opcode, size, GameBuffer.buffer(Buffer.buffer(buf.readBytes(size)))));
		}
		
		future.complete(packets);

	}

}

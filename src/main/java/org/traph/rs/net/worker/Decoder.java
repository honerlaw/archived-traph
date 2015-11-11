package org.traph.rs.net.worker;

import org.traph.rs.net.Client;
import org.traph.util.Constant;

import io.netty.buffer.ByteBuf;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class Decoder implements Handler<Future<Buffer>> {

	private final Client client;
	private final Buffer request;
	
	public Decoder(Client client, Buffer request) {
		this.client = client;
		this.request = request;
	}
	
	@Override
	public void handle(Future<Buffer> future) {
		ByteBuf buf = request.getByteBuf();
		
		int opcode = -1;
		int size = -1;
		
		while(buf.isReadable()) {
			// read opcode
			if(opcode == -1) {
				opcode = buf.readByte() & 0xFF;
				opcode = opcode - client.getIsaacDecoder().nextInt() & 0xFF;
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
			
			// dump the payload for now
			for(int i = 0; i < size; ++i) {
				buf.readByte();
			}
			
			// display the packet and its size
			System.out.println(opcode + " " + size);
			
		}
		
		future.complete();
		
	}

}

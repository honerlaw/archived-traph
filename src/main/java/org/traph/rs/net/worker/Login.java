package org.traph.rs.net.worker;

import java.math.BigInteger;

import org.traph.rs.model.player.Player;
import org.traph.rs.net.Client;
import org.traph.util.Constant;
import org.traph.util.Constant.Client.State;
import org.traph.util.StringUtil;
import org.traph.util.security.IsaacRandomGen;

import io.netty.buffer.ByteBuf;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;

public class Login implements Handler<Future<Buffer>> {
	
	private final Client client;
	private final ByteBuf buffer;
	
	public Login(Client client, Buffer buffer) {
		this.client = client;
		this.buffer = buffer.getByteBuf();
	}

	@Override
	public void handle(Future<Buffer> future) {
		
		int type = buffer.readUnsignedByte();
		if(type != Constant.Login.TYPE_RECONNECTION && type != Constant.Login.TYPE_STANDARD) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_LOGIN_SERVER_REJECTED_SESSION));
			return;
		}
		
		int loginLength = buffer.readUnsignedByte();
		if(buffer.readableBytes() < loginLength) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_LOGIN_SERVER_REJECTED_SESSION));
			return;
		}
		
		int version = 255 - buffer.readUnsignedByte(); // 0
		int release = buffer.readUnsignedShort(); // 317
		
		System.out.println(release + " " + version);
		
		int memoryStatus = buffer.readUnsignedByte();
		if(memoryStatus != 0 && memoryStatus != 1) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_LOGIN_SERVER_REJECTED_SESSION));
			return;
		}
		
		int[] crcs = new int[9];
		for(int i = 0; i < crcs.length; ++i) {
			crcs[i] = buffer.readInt();
		}
		
		int length = buffer.readUnsignedByte();
		if(length != loginLength - 41) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_LOGIN_SERVER_REJECTED_SESSION));
			return;
		}
		 
	 	ByteBuf secure = buffer.readBytes(length);
		BigInteger value = new BigInteger(secure.array()).modPow(Constant.Net.RSA_EXPONENT, Constant.Net.RSA_MODULUS);
		secure = Buffer.buffer(value.toByteArray()).getByteBuf();

		int id = secure.readUnsignedByte();
		if (id != 10) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_LOGIN_SERVER_REJECTED_SESSION));
			return;
		}
		
		// validate the seeds
		client.setClientSeed(secure.readLong());
		long reportedServerSeed = secure.readLong();
		if (reportedServerSeed != client.getServerSeed()) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_LOGIN_SERVER_REJECTED_SESSION));
			return;
		}
		
		// set the encoder / decoder
		int[] seed = new int[] {
			(int) (client.getClientSeed() >> 32),
			(int) client.getClientSeed(),
			(int) (client.getServerSeed() >> 32),
			(int) client.getServerSeed()
		};
		client.setIsaacDecoder(new IsaacRandomGen(seed));
		for(int i = 0; i < seed.length; ++i) {
			seed[i] += 50;
		}
		client.setIsaacEncoder(new IsaacRandomGen(seed));

		// get player info and save it to the client
		int uid = secure.readInt();
		String username = StringUtil.getRSString(secure);
		String password = StringUtil.getRSString(secure);
		client.getGameData().setPlayer(new Player(uid, username, password));

		// make sure that the password / username is valid
		if (password.length() < Constant.Login.MIN_PASSWORD_LENGTH || password.length() > Constant.Login.MAX_PASSWORD_LENGTH || username.isEmpty() || username.length() > Constant.Login.MAX_USERNAME_LENGTH || !username.matches("^[a-zA-Z0-9]*$")) {
			future.complete(Buffer.buffer().appendByte(Constant.Login.STATUS_INVALID_CREDENTIALS));
			return;
		}
		
		// move to the game state to start decoding incoming packets
		client.setState(State.GAME);
		
		// we successfully made it here so we technically "logged in"
		// TODO: load player data here
		future.complete(Buffer.buffer(3).appendByte((byte) 2).appendByte((byte) 0).appendByte((byte) 0));
	}

}

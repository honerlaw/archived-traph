package org.traph.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class Constant {
	
	public static final SecureRandom RANDOM = new SecureRandom();
	
	public static class Net {
		
		public static final BigInteger RSA_EXPONENT = new BigInteger("120781269726395875706098914792632665669232332199994595915842744528165392929818094820999914937201209625182416103294265703824855716602346634315606515856057982306929491077079004978822886676659021162115196537959043656834758896529920749417950230556942998802895439199033635913099551846863515416559401750771767877729");
		
		public static final BigInteger RSA_MODULUS = new BigInteger("135132254537767494172637802870832699010951044870700886568731514897355196688828183080114574410423129815551837823063596487214599059340150423769430053257395792691151302912926229717783927035717787992258769107718220891804313265050465184577451436243162938536670663769811545381783346610630542229353429221051109391757");
		
	}
	
	public static class Packet {
		
		public static final int[] PACKET_LENGTHS = {
			0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 0, 8, 0, // 50
			0, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, // 250
		};
		
		public static final int[] BIT_MASK = new int[32];
		
		static {
			for (int i = 0; i < BIT_MASK.length; i++) {
				BIT_MASK[i] = (1 << i) - 1;
			}
		}
		
	}
	
	public static class FileSystem {
		
		public static final int MAX_INDEX_FILES = 256;
		
		public static final int INDEX_BLOCK_SIZE = 6;
		
		public static final int CRC_HASH = 1234;
		
		public static final int ARCHIVE_HEADER_SIZE = 8;
		
		public static final int ARCHIVE_BLOCK_SIZE = 520;
		
		public static final int ARCHIVE_CHUNK_SIZE = ARCHIVE_BLOCK_SIZE - ARCHIVE_HEADER_SIZE;
		
		/**
		 * The ondemand request size (the size of the request block)
		 */
		public static final int ONDEMAND_REQUEST_SIZE = 4;
		
		/**
		 * The ondemand request chunk size
		 */
		public static final int ONDEMAND_CHUNK_SIZE = 500;
		
		public static final byte REQUEST_HTTP = 0;
		
		public static final byte REQUEST_JAGGRAB = 1;
		
		public static final byte REQUEST_ONDEMAND = 2;
		
	}
	
	public static class Client {
		
		/**
		 * The different states that a client context can be in
		 */
		public static enum State {
			HANDSHAKE, ONDEMAND, LOGIN, GAME
		}
		
		/**
		 * Requesting to add login request data to the client context
		 */
		public static final byte LOGIN_REQUEST_DATA = 0;
		
	}
	
	public static class HandShake {
		
		/**
		 * The request code for a login request
		 */
		public static final int LOGIN_REQUEST = 14;
		
		/**
		 * The request code for an ondemand request
		 */
		public static final int ONDEMAND_REQUEST = 15;
		
	}
	
	public static class Login {
		
		/**
		 * The minimum password length
		 */
		public static final byte MIN_PASSWORD_LENGTH = 6;
		
		/**
		 * The maximum password length
		 */
		public static final byte MAX_PASSWORD_LENGTH = 25;
		
		/**
		 * The maximum allowed user length
		 */
		public static final byte MAX_USERNAME_LENGTH = 12;
		
		/**
		 * Exchange data login status.
		 */
		public static final byte STATUS_EXCHANGE_DATA = 0;

		/**
		 * Delay for 2 seconds login status.
		 */
		public static final byte STATUS_DELAY = 1;

		/**
		 * OK login status.
		 */
		public static final byte STATUS_OK = 2;

		/**
		 * Invalid credentials login status.
		 */
		public static final byte STATUS_INVALID_CREDENTIALS = 3;

		/**
		 * Account disabled login status.
		 */
		public static final byte STATUS_ACCOUNT_DISABLED = 4;

		/**
		 * Account online login status.
		 */
		public static final byte STATUS_ACCOUNT_ONLINE = 5;

		/**
		 * Game updated login status.
		 */
		public static final byte STATUS_GAME_UPDATED = 6;

		/**
		 * Server full login status.
		 */
		public static final byte STATUS_SERVER_FULL = 7;

		/**
		 * Login server offline login status.
		 */
		public static final byte STATUS_LOGIN_SERVER_OFFLINE = 8;

		/**
		 * Too many connections login status.
		 */
		public static final byte STATUS_TOO_MANY_CONNECTIONS = 9;

		/**
		 * Bad session id login status.
		 */
		public static final byte STATUS_BAD_SESSION_ID = 10;

		/**
		 * Login server rejected session login status.
		 */
		public static final byte STATUS_LOGIN_SERVER_REJECTED_SESSION = 11;

		/**
		 * Members account required login status.
		 */
		public static final byte STATUS_MEMBERS_ACCOUNT_REQUIRED = 12;

		/**
		 * Could not complete login status.
		 */
		public static final byte STATUS_COULD_NOT_COMPLETE = 13;

		/**
		 * Server updating login status.
		 */
		public static final byte STATUS_UPDATING = 14;

		/**
		 * Reconnection OK login status.
		 */
		public static final byte STATUS_RECONNECTION_OK = 15;

		/**
		 * Too many login attempts login status.
		 */
		public static final byte STATUS_TOO_MANY_LOGINS = 16;

		/**
		 * Standing in members area on free world status.
		 */
		public static final byte STATUS_IN_MEMBERS_AREA = 17;

		/**
		 * Invalid login server status.
		 */
		public static final byte STATUS_INVALID_LOGIN_SERVER = 20;

		/**
		 * Profile transfer login status.
		 */
		public static final byte STATUS_PROFILE_TRANSFER = 21;

		/**
		 * Standard login type id.
		 */
		public static final byte TYPE_STANDARD = 16;

		/**
		 * Reconnection login type id.
		 */
		public static final byte TYPE_RECONNECTION = 18;
		
	}

}
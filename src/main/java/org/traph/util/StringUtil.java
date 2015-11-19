package org.traph.util;

import io.netty.buffer.ByteBuf;

public class StringUtil {
	
	public static final String getRSString(ByteBuf buf) {
		StringBuilder builder = new StringBuilder();
		for(byte c; (c = buf.readByte()) != 10; ) {
			builder.append((char) c);
		}
		return builder.toString();
	}
	
	public static final long getStringAsLong(String value) {
		long l = 0L;
		for (int i = 0; i < value.length() && i < 12; i++) {
			char c = value.charAt(i);
			l *= 37L;
			if (c >= 'A' && c <= 'Z')
				l += (1 + c) - 65;
			else if (c >= 'a' && c <= 'z')
				l += (1 + c) - 97;
			else if (c >= '0' && c <= '9')
				l += (27 + c) - 48;
		}
		while (l % 37L == 0L && l != 0L)
			l /= 37L;
		return l;
	}

}

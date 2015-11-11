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

}

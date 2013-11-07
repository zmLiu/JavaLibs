package com.lzm.netty.decoder;

import io.netty.buffer.ByteBuf;

import com.lzm.netty.utils.Packet;
import com.lzm.utils.LogError;

public class StringArrayDecoder extends BytesDecoder_Crossdomain {

	@Override
	protected Object decode(ByteBuf in) {
		String []msgs = null;
		try {
			int cmd = in.readShort();

			Packet packet = new Packet(in);
			
			int msgLen = packet.readShort();
			
			msgs = new String[msgLen + 1];
			
			msgs[0] = cmd+"";
			
			for (int i = 0; i < msgLen; i++) {
				msgs[i+1] = packet.readString("utf-8");
			}
		} catch (Exception e) {
			LogError.error(e);
		}
		return msgs;
	}
}

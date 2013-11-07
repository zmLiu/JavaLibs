package com.lzm.netty.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.util.ArrayList;

public class StringArrayEncoder extends MessageToByteEncoder<String[]> {

	@Override
	protected void encode(ChannelHandlerContext ctx, String[] msgs,ByteBuf out) throws Exception {
		int dataLen = 6;//消息长度(2字节) cmd(2字节) 数组长度(2字节)
		
		ArrayList<byte[]> bytesList = new ArrayList<byte[]>();
		
		int length = msgs.length;
		byte []bytes;
		for (int i = 1; i < length; i++) {
			bytes = msgs[i].getBytes("utf-8");
			bytesList.add(bytes);
			
			//字节数组长度标识 + 字节数组本身长度
			dataLen += (2 + bytes.length);
		}
		
		ByteBuf buf = Unpooled.buffer(dataLen);
		
		buf.writeShort(dataLen-2);//消息体长度(需要减去表示占用值)
		buf.writeShort(Integer.valueOf(msgs[0]));
		buf.writeShort(length-1);
		
		for (int i = 0; i < length-1; i++) {
			bytes = bytesList.get(i);
			buf.writeShort(bytes.length);
			buf.writeBytes(bytes);
		}
		
		out.writeBytes(buf);
	}

}

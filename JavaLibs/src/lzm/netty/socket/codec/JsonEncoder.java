package lzm.netty.socket.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class JsonEncoder extends MessageToByteEncoder<JSONObject> {

	@Override
	protected void encode(ChannelHandlerContext ctx, JSONObject jsonObject,ByteBuf out) throws Exception {
		byte[] bytes = JSON.toJSONBytes(jsonObject);
//		ByteBuf buf = Unpooled.buffer(2 + bytes.length);
//		buf.writeShort(bytes.length);
//		buf.writeBytes(bytes);
//		out.writeBytes(buf);
		out.writeShort(bytes.length);
		out.writeBytes(bytes);
		
	}

}

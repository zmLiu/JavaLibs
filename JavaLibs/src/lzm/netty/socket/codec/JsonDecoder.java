package lzm.netty.socket.codec;

import lzm.utils.LogError;
import io.netty.buffer.ByteBuf;

import com.alibaba.fastjson.JSONObject;

public class JsonDecoder extends BytesDecoder_Crossdomain {

	@Override
	protected Object decode(ByteBuf in) {
		JSONObject josnObject = null;
		try {
			byte[] bytes = new byte[in.readableBytes()];
			in.readBytes(bytes);
			josnObject = JSONObject.parseObject(new String(bytes,"utf-8"));
			return josnObject;
		} catch (Exception e) {
			LogError.error(e);
		}
		return josnObject;
	}

}

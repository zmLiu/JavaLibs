package lzm.netty.socket.codec;

import lzm.utils.LogError;

import com.alibaba.fastjson.JSONObject;

public class JsonDecoder extends BytesDecoder_Crossdomain {

	@Override
	protected Object decode(byte[] bytes) {
		JSONObject josnObject = null;
		try {
			josnObject = JSONObject.parseObject(new String(bytes,"utf-8"));
			return josnObject;
		} catch (Exception e) {
			LogError.error(e);
		}
		return josnObject;
	}

}

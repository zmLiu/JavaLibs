package lzm.netty.socket.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ---处理粘包---
 * */
public class BytesDecoder extends ByteToMessageDecoder {
	
	/**
	 * 当前需要读取数据的长度
	 * */
	protected int readLength = 2;
	
	/**
	 * 是否读取消息体长度
	 * */
	protected boolean readStringLength = true;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception {
		
		while(in.readableBytes() >= readLength){//需要有足够的可读字节
			//读取消息体长度
			if(readStringLength) readLength = in.readShort();
			
			//读取消息体
			if(in.readableBytes() >= readLength){
				out.add(decode(in.readBytes(readLength)));
				readLength = 2;
				readStringLength = true;
			}else{
				//消息体不全，下一次继续读取消息体
				readStringLength = false;
			}
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		ctx.close();
	}
	
	protected Object decode(ByteBuf in){
		
		return null;
	}

}

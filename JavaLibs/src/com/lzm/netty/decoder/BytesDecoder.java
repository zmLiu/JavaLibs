package com.lzm.netty.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ---����ճ��---
 * */
public class BytesDecoder extends ByteToMessageDecoder {
	
	/**
	 * ��ǰ��Ҫ��ȡ���ݵĳ���
	 * */
	private int readLength = 4;
	
	/**
	 * �Ƿ��ȡ��Ϣ�峤��
	 * */
	private boolean readStringLength = true;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception {
		//��Ϣ�岻���� ֱ�ӷ���
		if(in.readableBytes() < readLength) return;
		//��ȡ��Ϣ�峤��
		if(readStringLength) readLength = in.readInt();
		
		//��ȡ��Ϣ��
		if(in.readableBytes() >= readLength){
			out.add(in.readBytes(readLength));
			readLength = 4;
			readStringLength = true;
		}else{
			//��Ϣ�岻ȫ����һ�μ�����ȡ��Ϣ��
			readStringLength = false;
		}
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		ctx.close();
	}

}

package com.lzm.netty.decoder;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * <p>---����ճ��---</p>
 * <p>��һ���յ���Ϣ ���ز����ļ�</p>
 * */
public class BytesDecoder_Crossdomain extends ByteToMessageDecoder {
	/**
	 * ��ǰ��Ҫ��ȡ���ݵĳ���
	 * */
	private int readLength = 4;
	
	/**
	 * �Ƿ��ȡ��Ϣ�峤��
	 * */
	private boolean readStringLength = true;
	
	/**
	 * �Ƿ�Ϊ��һ�ν��ܵ���Ϣ
	 * */
	private boolean firstMessage = true;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception {
		
		if(firstMessage){//flash ����socket��Ҫ��������ļ�
			byte bytes[] = new byte[in.readableBytes()];
			in.readBytes(bytes);
			String str = new String(bytes);
			if(str.indexOf("<policy-file-request/>") != -1){
				firstMessage = false;
				
				StringBuffer xmlStr = new StringBuffer();
				xmlStr.append("<cross-domain-policy>");
				xmlStr.append("<allow-access-from domain=\"*\" to-ports=\"*\" />");
				xmlStr.append("</cross-domain-policy>\0");
				
                ByteBuf xmlBuf = ctx.alloc().buffer(xmlStr.length());
                xmlBuf.writeBytes(xmlStr.toString().getBytes());
                
                ctx.write(xmlBuf);
                ctx.flush();
			}else{
				ctx.close();
			}
		}else if(in.readableBytes() >= readLength){//��Ҫ���㹻�Ŀɶ��ֽ�
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
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception {
		ctx.close();
	}

}

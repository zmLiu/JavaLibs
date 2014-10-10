package lzm.netty.socket.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * <p>---处理粘包---</p>
 * <p>第一次收到消息 返回策略文件</p>
 * */
public class BytesDecoder_Crossdomain extends BytesDecoder {
	/**
	 * 是否为第一次接受到消息
	 * */
	private boolean firstMessage = true;
	
	/** 请求策略文件命令的长度 */
	private final int crossdomainRequestLength = 23;
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in,List<Object> out) throws Exception {
		if(firstMessage){//flash 连接socket需要请求策略文件
			if(in.readableBytes() < crossdomainRequestLength) return;//请求策略文件的 字符串长度不足。
			byte bytes[] = new byte[crossdomainRequestLength];
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
                
                ctx.channel().writeAndFlush(xmlBuf);
			}else{
				ctx.close();
			}
		}
		
		if(in.readableBytes() >= readLength){//需要有足够的可读字节
			//读取消息体长度
			if(readStringLength) readLength = in.readShort();
			
			//需要读取的消息长度小于0，表示非法
			if(readLength <= 0){
				ctx.close();
				throw new Exception("message length error!");
			}
			
			//读取消息体
			if(in.readableBytes() >= readLength){
				byte[] bytes = new byte[readLength];
				in.readBytes(bytes);
				out.add(decode(bytes));
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
	
	protected Object decode(byte[] bytes){
		
		return null;
	}

}

package com.lzm.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lzm.netty.command.ICommand;
import com.lzm.netty.utils.Packet;

/**
 * 逻辑处理分发
 * */
public class ServerHandler extends ChannelInboundHandlerAdapter {
	
	//所有命令
	private static HashMap<Integer, ICommand> commands = new HashMap<Integer, ICommand>();
	
	//新用户连接的回掉
	private static ICommand connectCommand;

	//连接关闭的回掉
	private static ICommand connectCloseCommand;
	
	//注册命令
	public static void registerCommand(int cmd,ICommand command){
		commands.put(cmd, command);
	}
	//获取命令
	public static ICommand getCommand(int cmd){
		return commands.get(cmd);
	}
	
	//设置连接关闭的回掉
	public static void registerConnectCommand(ICommand connectCommand){
		ServerHandler.connectCommand= connectCommand;
	}
	//获取用户连接命令
	public static ICommand getConnectCommand(){
		return connectCommand;
	}
	
	//设置连接关闭的回掉
	public static void registerConnectCloseCommand(ICommand connectCloseCommand){
		ServerHandler.connectCloseCommand = connectCloseCommand;
	}
	//获取连接关闭的命令
	public static ICommand getConnectCloseCommand(){
		return connectCloseCommand;
	}
	
	//发送字符串数组
	public static void sendMessages(ChannelHandlerContext ctx,int cmd,String []msgs) throws Exception{
		
		int dataLen = 6;//消息长度(2字节) cmd(2字节) 数组长度(2字节)
		
		ArrayList<byte[]> bytesList = new ArrayList<byte[]>();
		
		int length = msgs.length;
		byte []bytes;
		for (int i = 0; i < length; i++) {
			bytes = msgs[i].getBytes("utf-8");
			bytesList.add(bytes);
			
			//字节数组长度标识 + 字节数组本身长度
			dataLen += (2 + bytes.length);
		}
		
		ByteBuf buf = ctx.alloc().buffer(dataLen);
		
		buf.writeShort(dataLen-2);//消息体长度(需要减去表示占用值)
		buf.writeShort(cmd);
		buf.writeShort(length);
		
		for (int i = 0; i < length; i++) {
			bytes = bytesList.get(i);
			buf.writeShort(bytes.length);
			buf.writeBytes(bytes);
		}
		ctx.write(buf);
		ctx.flush();
		
	}
	
	//发送字符串数组
	public static void sendMessages(List<ChannelHandlerContext> ctxs,int cmd,String []msgs) throws Exception{
		int length = ctxs.size();
		for (int i = 0; i < length; i++) {
			sendMessages(ctxs.get(i), cmd, msgs);
		}
	}
	
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		try {
			ByteBuf in = (ByteBuf) msg;
			
	    	int cmd = in.readShort();
	    	ICommand command = commands.get(cmd);
	    	
	    	if(command != null) {
	    		Packet packet = new Packet(in);
	    		
	    		int msgLen = packet.readShort();
	    		String []msgs = new String[msgLen];
	    		
	    		for (int i = 0; i < msgLen; i++) {
	    			msgs[i] = packet.readString("utf-8");
	    		}
	    		
	    		executeCmd(command,ctx, msgs);
	    	}
		}finally{
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if(connectCommand != null) executeCmd(connectCommand,ctx, null);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(connectCloseCommand != null) executeCmd(connectCloseCommand,ctx, null);
	}
	
	public void executeCmd(ICommand command,ChannelHandlerContext ctx,String []msgs) throws Exception{
		CommandExecuteThread.addTask(command, ctx, msgs);
	}
	
	
}

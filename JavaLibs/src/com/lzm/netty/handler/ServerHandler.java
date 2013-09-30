package com.lzm.netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import com.lzm.netty.command.ICommand;
import com.lzm.netty.utils.Packet;

/**
 * �߼�����ַ�
 * */
public class ServerHandler extends ChannelInboundHandlerAdapter {
	
	//��������
	private static HashMap<Integer, ICommand> commands = new HashMap<Integer, ICommand>();
	
	//���û����ӵĻص�
	private static ICommand connectCommand;

	//���ӹرյĻص�
	private static ICommand connectCloseCommand;
	
	//ע������
	public static void registerCommand(int cmd,ICommand command){
		commands.put(cmd, command);
	}
	
	//�������ӹرյĻص�
	public static void registerConnectCommand(ICommand connectCommand){
		ServerHandler.connectCommand= connectCommand;
	}
	
	//�������ӹرյĻص�
	public static void registerConnectCloseCommand(ICommand connectCloseCommand){
		ServerHandler.connectCloseCommand = connectCloseCommand;
	}
		
	//�����ַ�������
	public static void sendMessages(ChannelHandlerContext ctx,int cmd,String []msgs) throws UnsupportedEncodingException{
		
		int dataLen = 12;//��Ϣ����(4�ֽ�) cmd(4�ֽ�) ���鳤��(4�ֽ�)
		
		ArrayList<byte[]> bytesList = new ArrayList<byte[]>();
		
		int length = msgs.length;
		byte []bytes;
		for (int i = 0; i < length; i++) {
			bytes = msgs[i].getBytes("utf-8");
			bytesList.add(bytes);
			
			//�ֽ����鳤�ȱ�ʶ + �ֽ����鱾����
			dataLen += (4 + bytes.length);
		}
		
		ByteBuf buf = ctx.alloc().buffer(dataLen);
		
		buf.writeInt(dataLen-4);//��Ϣ�峤��(��Ҫ��ȥ��ʾռ��ֵ)
		buf.writeInt(cmd);
		buf.writeInt(length);
		
		for (int i = 0; i < length; i++) {
			bytes = bytesList.get(i);
			buf.writeInt(bytes.length);
			buf.writeBytes(bytes);
		}
		ctx.write(buf);
		ctx.flush();
		
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		try {
			ByteBuf in = (ByteBuf) msg;
			
	    	int cmd = in.readInt();
	    	ICommand command = commands.get(cmd);
	    	
	    	if(command != null) {
	    		Packet packet = new Packet(in);
	    		
	    		int msgLen = packet.readInt();
	    		String []msgs = new String[msgLen];
	    		
	    		for (int i = 0; i < msgLen; i++) {
	    			msgs[i] = packet.readString("utf-8");
	    		}
	    		
	    		command.execute(ctx, msgs);
	    	}
		}finally{
			ReferenceCountUtil.release(msg);
		}
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if(connectCommand != null) connectCommand.execute(ctx, null);
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(connectCloseCommand != null) connectCloseCommand.execute(ctx, null);
	}
	
	
}

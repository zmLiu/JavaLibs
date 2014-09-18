package lzm.netty.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lzm.netty.socket.command.CommandManager;
import lzm.netty.socket.command.ICommand;
import lzm.netty.socket.disruptor.CommandEventProducer;

import com.alibaba.fastjson.JSONObject;

/**
 * 逻辑处理分发
 * */
public class SocketServerHandler extends SimpleChannelInboundHandler<Object> {
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if(CommandManager.getConnectCommand() != null) {
			CommandEventProducer.execute(CommandManager.getConnectCommand(), ctx, null);
		}
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(CommandManager.getConnectCloseCommand() != null) {
			CommandEventProducer.execute(CommandManager.getConnectCloseCommand(), ctx, null);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msgs)throws Exception {
		JSONObject jsonObject = (JSONObject) msgs;
		String cmd = jsonObject.getString("cmd");
		ICommand command = CommandManager.getCommand(cmd);
		if(command != null){
			CommandEventProducer.execute(command, ctx, msgs);
		}
	}
	
}

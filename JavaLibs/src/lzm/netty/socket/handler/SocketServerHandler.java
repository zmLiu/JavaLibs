package lzm.netty.socket.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lzm.netty.socket.command.CommandLogicExecutor;
import lzm.netty.socket.command.CommandManager;
import lzm.netty.socket.command.ICommand;

import com.alibaba.fastjson.JSONObject;

/**
 * 逻辑处理分发
 * */
public class SocketServerHandler extends SimpleChannelInboundHandler<Object> {
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if(CommandManager.getConnectCommand() != null) {
			CommandManager.executeCmd(new CommandLogicExecutor(CommandManager.getConnectCommand(), ctx, null));
		}
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(CommandManager.getConnectCloseCommand() != null) {
			CommandManager.executeCmd(new CommandLogicExecutor(CommandManager.getConnectCloseCommand(), ctx, null));
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msgs)throws Exception {
		JSONObject jsonObject = (JSONObject) msgs;
		String cmd = jsonObject.getString("cmd");
		ICommand command = CommandManager.getCommand(cmd);
		if(command != null){
			CommandManager.executeCmd(new CommandLogicExecutor(command, ctx, msgs));
		}
	}
	
}

package lzm.netty.socket.handler;

import lzm.netty.socket.command.CommandManager;
import lzm.netty.socket.command.ICommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import com.alibaba.fastjson.JSONObject;

/**
 * 逻辑处理分发
 * */
public class ServerHandler extends SimpleChannelInboundHandler<Object> {
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		if(CommandManager.getConnectCommand() != null) {
			CommandManager.executeCmd(CommandManager.getConnectCommand(),ctx, null);
		}
	}
	
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		if(CommandManager.getConnectCloseCommand() != null) {
			CommandManager.executeCmd(CommandManager.getConnectCloseCommand(),ctx, null);
		}
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msgs)throws Exception {
		try {
			JSONObject jsonObject = (JSONObject) msgs;
			int cmd = jsonObject.getIntValue("cmd");
			ICommand command = CommandManager.getCommand(cmd);
			if(command != null){
				CommandManager.executeCmd(command, ctx, msgs);
			}
		} finally {
			ReferenceCountUtil.release(msgs);
		}
	}
	
}

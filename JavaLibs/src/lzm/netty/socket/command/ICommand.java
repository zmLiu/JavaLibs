package lzm.netty.socket.command;

import io.netty.channel.ChannelHandlerContext;

public interface ICommand {
	/**
	 * 获取命令号
	 * */
	String getCmd();
	
	/**
	 * 接受到消息
	 * */
	void execute(ChannelHandlerContext ctx,Object msgs) throws Exception;
}

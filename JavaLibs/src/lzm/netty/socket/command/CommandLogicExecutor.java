package lzm.netty.socket.command;

import io.netty.channel.ChannelHandlerContext;
import lzm.executor.logic.ILogic;

/** 
 * @author zmliu
 *
 * @Create Date : 2014年8月28日 下午1:49:48 
 * 
 * @des 命令执行
 */
public class CommandLogicExecutor implements ILogic {
	
	private ICommand command;
	private ChannelHandlerContext ctx;
	private Object msgs;
	
	public CommandLogicExecutor(ICommand command,ChannelHandlerContext ctx,Object msgs) {
		this.command = command;
		this.ctx = ctx;
		this.msgs = msgs;
	}

	@Override
	public void execute() throws Exception {
		command.execute(ctx, msgs);
	}

}

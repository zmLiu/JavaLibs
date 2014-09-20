package lzm.netty.socket.disruptor;

import io.netty.channel.ChannelHandlerContext;
import lzm.netty.socket.command.ICommand;
import lzm.utils.LogError;

public class CommandEvent {
	
	private ICommand command;
	private ChannelHandlerContext ctx;
	private Object msgs;
	
	public void execute() throws Exception {
		try {
			command.execute(ctx, msgs);
		} catch (Exception e) {
			LogError.error(e);
		}
	}
	
	public ICommand getCommand() {
		return command;
	}
	public void setCommand(ICommand command) {
		this.command = command;
	}
	public ChannelHandlerContext getCtx() {
		return ctx;
	}
	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	public Object getMsgs() {
		return msgs;
	}
	public void setMsgs(Object msgs) {
		this.msgs = msgs;
	}
}

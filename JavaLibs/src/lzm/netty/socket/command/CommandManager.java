package lzm.netty.socket.command;

import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.List;

public class CommandManager {
	// 所有命令
	private static HashMap<Integer, ICommand> commands = new HashMap<Integer, ICommand>();

	// 新用户连接的回掉
	private static ICommand connectCommand;

	// 连接关闭的回掉
	private static ICommand connectCloseCommand;

	// 注册命令
	public static void registerCommand(int cmd, ICommand command) {
		commands.put(cmd, command);
	}

	// 获取命令
	public static ICommand getCommand(int cmd) {
		return commands.get(cmd);
	}

	// 设置新建链接的回掉
	public static void registerConnectCommand(ICommand connectCommand) {
		CommandManager.connectCommand = connectCommand;
	}

	// 获取用户连接命令
	public static ICommand getConnectCommand() {
		return connectCommand;
	}

	// 设置连接关闭的回掉
	public static void registerConnectCloseCommand(ICommand connectCloseCommand) {
		CommandManager.connectCloseCommand = connectCloseCommand;
	}

	// 获取连接关闭的命令
	public static ICommand getConnectCloseCommand() {
		return connectCloseCommand;
	}

	// 对单个用户发送消息
	public static void sendMessages(ChannelHandlerContext ctx, Object msgs) throws Exception {
		ctx.writeAndFlush(msgs);
	}

	// 对多个用户发送消息
	public static void sendMessages(List<ChannelHandlerContext> ctxs, Object msgs) throws Exception {
		int length = ctxs.size();
		for (int i = 0; i < length; i++) {
			sendMessages(ctxs.get(i), msgs);
		}
	}
	
	//添加需要执行的命令
	public static void executeCmd(ICommand command,ChannelHandlerContext ctx,Object msgs) throws Exception{
//		command.execute(ctx, msgs);
		CommandExecuteThread.addTask(command, ctx, msgs);
	}
	
}

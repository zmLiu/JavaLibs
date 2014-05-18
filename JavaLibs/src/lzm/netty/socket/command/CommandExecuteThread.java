package lzm.netty.socket.command;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.ConcurrentLinkedQueue;

import lzm.utils.LogError;

import org.apache.log4j.Logger;

/**
 * 命令执行线程
 * */
public class CommandExecuteThread extends Thread {

	private static int threadUseIndex;
	private static int threadCount;
	private static CommandExecuteThread []threads;
	
	// 初始化命令执行线程
	public static void initExecuteCommandThread(int threadCount) throws Exception {
		if(threads != null){
			throw new Exception("ExecuteCommandThread already init.");
		}
		CommandExecuteThread.threadUseIndex = 0;
		CommandExecuteThread.threadCount = threadCount;
		CommandExecuteThread.threads = new CommandExecuteThread[CommandExecuteThread.threadCount];
		for (int i = 0; i < threadCount; i++) {
			CommandExecuteThread thread = new CommandExecuteThread();
			thread.start();
			CommandExecuteThread.threads[i] = thread;
		}
		
		Logger.getRootLogger().info("ExecuteCommandThread start " + threadCount + "...");
	}

	//添加任务
	public static void addTask(ICommand command, ChannelHandlerContext ctx,Object msgs) {
		threads[threadUseIndex].taskQueue.add(new Object[] { command, ctx, msgs });
		
		threadUseIndex++;
		if(threadUseIndex == threadCount) threadUseIndex = 0;
	}
	
	private ConcurrentLinkedQueue<Object[]> taskQueue = new ConcurrentLinkedQueue<Object[]>();
	
	@Override
	public void run() {
		ICommand command;
		ChannelHandlerContext ctx;
		Object msgs;
		while (true) {
			try {
				Object[] objects = taskQueue.poll();
				if (objects == null) {
					command = null;
					ctx = null;
					msgs = null;
					Thread.sleep(30L);
				} else {
					command = (ICommand) objects[0];
					ctx = (ChannelHandlerContext) objects[1];
					msgs = objects[2];
					command.execute(ctx, msgs);
				}
			} catch (Exception e) {
				try {
					Thread.sleep(100L);
				} catch (InterruptedException e1) {
					LogError.error(e1);
				}
				LogError.error(e);
			}
		}
	}
}

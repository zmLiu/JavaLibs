package lzm.netty.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lzm.netty.socket.command.CommandExecuteThread;
import lzm.netty.socket.config.SocketServerConfig;

public class SocketServer {
	
	private ServerBootstrap boot;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;

	public void run() throws Exception {
		CommandExecuteThread.initExecuteCommandThread(SocketServerConfig.executeCommandThreads);

		boot = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(SocketServerConfig.bossGroupThreads);
		workerGroup = new NioEventLoopGroup(SocketServerConfig.workerGroupThreads);
		try {
			boot.option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.TCP_NODELAY, true);

			boot.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childOption(ChannelOption.TCP_NODELAY, false)
				.childHandler(new SocketChannelInitializer());

			ChannelFuture f = boot.bind(SocketServerConfig.port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception err) {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}

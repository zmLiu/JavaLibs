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
	
	public void run() throws Exception {
		CommandExecuteThread.initExecuteCommandThread(SocketServerConfig.executeCommandThreads);

		ServerBootstrap b = new ServerBootstrap();
		EventLoopGroup bossGroup = new NioEventLoopGroup(SocketServerConfig.bossGroupThreads);
		EventLoopGroup workerGroup = new NioEventLoopGroup(SocketServerConfig.workerGroupThreads);
		try {
			b.option(ChannelOption.TCP_NODELAY, true).childOption(ChannelOption.TCP_NODELAY, true);

			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.TCP_NODELAY, false)
				.childHandler(new SocketChannelInitializer());
			
			if(SocketServerConfig.log) b.handler(new LoggingHandler(LogLevel.INFO));

			ChannelFuture f = b.bind(SocketServerConfig.port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}

}

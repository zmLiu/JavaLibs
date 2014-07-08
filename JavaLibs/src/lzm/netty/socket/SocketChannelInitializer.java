package lzm.netty.socket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lzm.netty.socket.config.SocketServerConfig;
import lzm.netty.socket.decoder.JsonDecoder;
import lzm.netty.socket.decoder.JsonEncoder;
import lzm.netty.socket.handler.SocketServerHandler;

public class SocketChannelInitializer extends ChannelInitializer<SocketChannel> {
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder",new JsonDecoder());
		pipeline.addLast("encoder", new JsonEncoder());
		pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(SocketServerConfig.idleTimeSeconds));
		pipeline.addLast("writeTimeoutHandler", new WriteTimeoutHandler(SocketServerConfig.idleTimeSeconds));
		pipeline.addLast("handler",new SocketServerHandler());
	}

}

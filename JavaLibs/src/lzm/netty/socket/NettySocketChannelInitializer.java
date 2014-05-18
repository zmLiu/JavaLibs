package lzm.netty.socket;

import lzm.netty.socket.decoder.JsonDecoder;
import lzm.netty.socket.decoder.JsonEncoder;
import lzm.netty.socket.handler.ServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class NettySocketChannelInitializer extends ChannelInitializer<SocketChannel> {
	//连接空闲时间
	private int idleTimeSeconds = 60;
	
	public NettySocketChannelInitializer(int idleTimeSeconds) {
		this.idleTimeSeconds = idleTimeSeconds;
	}
	
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder",new JsonDecoder());
		pipeline.addLast("encoder", new JsonEncoder());
		pipeline.addLast("readTimeoutHandler", new ReadTimeoutHandler(idleTimeSeconds));
		pipeline.addLast("handler",new ServerHandler());
	}

}

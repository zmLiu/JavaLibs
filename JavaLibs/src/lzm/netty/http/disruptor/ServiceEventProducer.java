package lzm.netty.http.disruptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lzm.netty.http.config.HttpServerConfig;
import lzm.netty.http.handler.HttpServerHandler;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class ServiceEventProducer {
	private final RingBuffer<ServiceEvent> ringBuffer;
	
	private ServiceEventProducer(RingBuffer<ServiceEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	
	private void onData(HttpServerHandler httpServerHandler,ChannelHandlerContext ctx,FullHttpRequest request){
		long sequence = ringBuffer.next();
		try {
			ServiceEvent event = ringBuffer.get(sequence);
			event.setCtx(ctx);
			event.setHttpServerHandler(httpServerHandler);
			event.setRequest(request);
		} finally{
			ringBuffer.publish(sequence);
		}
	}
	
	private static ServiceEventProducer instance;
	
	@SuppressWarnings("unchecked")
	private static ServiceEventProducer instance(){
		if(instance == null){
			Executor executor = Executors.newCachedThreadPool();
			ServiceEventFactory eventFactory = new ServiceEventFactory();
			
			Disruptor<ServiceEvent> disruptor = new Disruptor<ServiceEvent>(eventFactory, HttpServerConfig.disruptor_buff_size, executor, ProducerType.MULTI, new BlockingWaitStrategy());
			disruptor.handleEventsWith(new ServiceEventHandler());
			disruptor.start();
			
			RingBuffer<ServiceEvent> ringBuffer = disruptor.getRingBuffer();
			instance = new ServiceEventProducer(ringBuffer);
		}
		return instance;
	}
	
	public static void execute(HttpServerHandler httpServerHandler,ChannelHandlerContext ctx,FullHttpRequest request){
		instance().onData(httpServerHandler, ctx, request);
	}
	
}

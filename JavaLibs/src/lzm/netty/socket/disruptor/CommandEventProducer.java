package lzm.netty.socket.disruptor;

import io.netty.channel.ChannelHandlerContext;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import lzm.netty.socket.command.ICommand;
import lzm.netty.socket.config.SocketServerConfig;
import lzm.utils.LogError;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class CommandEventProducer {
	
	private final RingBuffer<CommandEvent> ringBuffer;
	
	private CommandEventProducer(RingBuffer<CommandEvent> ringBuffer) {
		this.ringBuffer = ringBuffer;
	}
	
	private void onData(ICommand command,ChannelHandlerContext ctx,Object msgs){
		long sequence = ringBuffer.next();
		try {
			CommandEvent event = ringBuffer.get(sequence);
			event.setCommand(command);
			event.setCtx(ctx);
			event.setMsgs(msgs);
		} catch (Exception e) {
			LogError.error(e);
		}finally{
			ringBuffer.publish(sequence);
		}
	}
	
	private static CommandEventProducer instance;
	
	@SuppressWarnings("unchecked")
	private static CommandEventProducer instance(){
		if(instance == null){
			Executor executor = Executors.newCachedThreadPool();
			CommandEventFactory eventFactory = new CommandEventFactory();
			
			Disruptor<CommandEvent> disruptor = new Disruptor<CommandEvent>(eventFactory, SocketServerConfig.disruptor_buff_size, executor, ProducerType.MULTI, new BlockingWaitStrategy());
			disruptor.handleEventsWith(new CommandEventHandler());
			disruptor.start();
			
			RingBuffer<CommandEvent> ringBuffer = disruptor.getRingBuffer();
			instance = new CommandEventProducer(ringBuffer);
		}
		return instance;
	}
	
	public static void execute(ICommand command,ChannelHandlerContext ctx,Object msgs){
		instance().onData(command, ctx, msgs);
	}
	
}

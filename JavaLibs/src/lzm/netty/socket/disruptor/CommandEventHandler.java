package lzm.netty.socket.disruptor;

import com.lmax.disruptor.EventHandler;

public class CommandEventHandler implements EventHandler<CommandEvent> {

	@Override
	public void onEvent(CommandEvent event, long sequence, boolean endOfBatch) throws Exception {
		event.execute();
	}

}

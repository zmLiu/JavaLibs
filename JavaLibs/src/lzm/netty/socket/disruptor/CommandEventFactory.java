package lzm.netty.socket.disruptor;

import com.lmax.disruptor.EventFactory;

public class CommandEventFactory implements EventFactory<CommandEvent> {

	@Override
	public CommandEvent newInstance() {
		return new CommandEvent();
	}

}

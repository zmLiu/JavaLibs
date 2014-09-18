package lzm.netty.http.disruptor;

import com.lmax.disruptor.EventHandler;

public class ServiceEventHandler implements EventHandler<ServiceEvent> {

	@Override
	public void onEvent(ServiceEvent event, long sequence, boolean endOfBatch) throws Exception {
		event.execute();
	}

}

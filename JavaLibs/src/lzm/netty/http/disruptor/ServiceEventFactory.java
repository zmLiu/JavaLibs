package lzm.netty.http.disruptor;

import com.lmax.disruptor.EventFactory;

public class ServiceEventFactory implements EventFactory<ServiceEvent> {

	@Override
	public ServiceEvent newInstance() {
		return new ServiceEvent();
	}

}

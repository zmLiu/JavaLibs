package lzm.netty.http.test;

import java.util.Iterator;

import lzm.netty.http.service.AbstractHttpService;

public class TestService extends AbstractHttpService {

	@Override
	public void index() throws Exception {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Path:").append(path).append("\r\n\r\n");
		
		Iterator<String> iterator;
		
		if(getParams != null){
			iterator = getParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				builder.append("Get Param:").append(key).append("=").append(getParams.get(key)).append("\r\n");
			}
		}
		
		builder.append("\r\n");
		
		if(postParams != null){
			iterator = postParams.keySet().iterator();
			while(iterator.hasNext()){
				String key = iterator.next();
				builder.append("Post Param:").append(key).append("=").append(postParams.get(key)).append("\r\n");
			}
		}
		
		sendResponse(builder.toString());
		
	}

}

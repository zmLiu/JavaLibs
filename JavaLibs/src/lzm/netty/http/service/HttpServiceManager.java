package lzm.netty.http.service;

import java.util.HashMap;
import java.util.Map;

public class HttpServiceManager {
	
	private static Map<String, AbstractHttpService> serviceClazzs = new HashMap<String, AbstractHttpService>();
	private static Map<String, String> serviceNames = new HashMap<String, String>();
	
	public static void registerService(String path,AbstractHttpService service){
		if(serviceClazzs.get(path) != null || serviceNames.get(path) != null){
			throw new Error("service path repeat");
		}
		serviceClazzs.put("/" + path, service);
	}
	
	public static void registerService(String path,String serviceClazzName){
		if(serviceClazzs.get(path) != null || serviceNames.get(path) != null){
			throw new Error("service path repeat");
		}
		serviceNames.put("/" + path, serviceClazzName);
	}
	
	public static AbstractHttpService getService(String path) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		AbstractHttpService service = null;
		
		String serviceClazzName = serviceNames.get(path);
		if(serviceClazzName != null){
			service = (AbstractHttpService) Class.forName(serviceClazzName).newInstance();
			return service;
		}
		
		service = serviceClazzs.get(path);
		if(service != null) {
			return service.getClass().newInstance();
		}
		
		return service;
	}
	
}

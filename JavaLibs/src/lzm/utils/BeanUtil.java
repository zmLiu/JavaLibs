package lzm.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.util.HashMap;
import java.util.Map;

public class BeanUtil {
	//缓存beaninfo
	private static Map<Class<?>, BeanInfo> beanInfoCache = new HashMap<Class<?>, BeanInfo>(); 
	public static BeanInfo getBeanInfo(Class<?> clazz) throws IntrospectionException{
		BeanInfo beanInfo = beanInfoCache.get(clazz);
		if(beanInfo == null){
			beanInfo = Introspector.getBeanInfo(clazz);
			beanInfoCache.put(clazz, beanInfo);
		}
		return beanInfo;
	}
}

package lzm.bo;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class BaseBo {
	
	//缓存beaninfo
	@SuppressWarnings("rawtypes")
	private static Map<Class, BeanInfo> beanInfoCache = new HashMap<Class, BeanInfo>(); 
	
	private static BeanInfo getBeanInfo(@SuppressWarnings("rawtypes") Class clazz) throws IntrospectionException{
		BeanInfo beanInfo = beanInfoCache.get(clazz);
		if(beanInfo == null){
			beanInfo = Introspector.getBeanInfo(clazz);
			beanInfoCache.put(clazz, beanInfo);
		}
		return beanInfo;
	}
	
	/**
	 * 把json的数据解析到对象
	 * */
	public void parseJson(JSONObject data) throws Exception {
		BeanInfo beanInfo = getBeanInfo(this.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		
		int propertyLength = propertyDescriptors.length;
		PropertyDescriptor propertyDescriptor;
		Method writeMethod;
		String propertyName;
		Object propertyValue;
		Object[] methodParams = new Object[1];
		
		for (int i = 0; i < propertyLength; i++) {
			propertyDescriptor = propertyDescriptors[i];
			propertyName = propertyDescriptor.getName();
			if(propertyName.equals("class")){
				continue;
			}
			
			writeMethod = propertyDescriptor.getWriteMethod();
			if(writeMethod == null){
				continue;
			}
			propertyValue = data.get(propertyName);
			if(propertyValue == null){
				continue;
			}
			methodParams[0] = propertyValue;
			writeMethod.invoke(this, methodParams);
		}
	}

	/**
	 * 把对象转换为json
	 * */
	public JSONObject toJson() throws Exception {
		BeanInfo beanInfo = getBeanInfo(this.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		
		int propertyLength = propertyDescriptors.length;
		PropertyDescriptor propertyDescriptor;
		Method readMethod;
		String propertyName;
		Object[] methodParams = new Object[0];
		
		JSONObject json = new JSONObject();
		
		for (int i = 0; i < propertyLength; i++) {
			propertyDescriptor = propertyDescriptors[i];
			propertyName = propertyDescriptor.getName();
			if(propertyName.equals("class")){
				continue;
			}
			
			readMethod = propertyDescriptor.getReadMethod();
			if(readMethod == null){
				continue;
			}
			
			json.put(propertyName, readMethod.invoke(this, methodParams));
		}
		return json;
	}
}

package lzm.bo;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import lzm.utils.BeanUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class BaseBo {
	
	/**
	 * 把json的数据解析到对象
	 * */
	public void parseJson(JSONObject data) throws Exception {
		BeanInfo beanInfo = BeanUtil.getBeanInfo(this.getClass());
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
		BeanInfo beanInfo = BeanUtil.getBeanInfo(this.getClass());
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
	
	/**
	 * 获取对象的json字符串
	 * */
	public String toJSONString(){
		return JSON.toJSONString(this);
	}
	
	/**
	 * 获取对象的json字符串
	 * @param converChinese 是否转换中文为\\uXXXX
	 * */
	public String toJSONString(boolean converChinese){
		if(converChinese){
			return JSON.toJSONString(this,SerializerFeature.BrowserCompatible);
		}else{
			return JSON.toJSONString(this);
		}
	}
}

package com.lzm.bo;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONObject;

public class BaseBo {
	public void parseJson(JSONObject data) throws Exception {
		Class<?> clazz = this.getClass();
		Method[] methods = clazz.getMethods();
		Class<?>[] paramTypes;

		int methodsLength = methods.length;

		Method method;
		String methodName;
		String propertyValue;
		Class<?> paramType;

		Object[] paramValue = new Object[1];

		for (int i = 0; i < methodsLength; i++) {
			method = methods[i];
			methodName = method.getName();
			paramTypes = method.getParameterTypes();
			if (paramTypes.length == 1 && methodName.startsWith("set")) {

				propertyValue = data.getString(methodName.substring(3,
						methodName.length()).toLowerCase());
				paramType = paramTypes[0];

				if (paramType == int.class) {
					paramValue[0] = Integer.valueOf(propertyValue);
				} else if (paramType == float.class) {
					paramValue[0] = Float.valueOf(propertyValue);
				} else if (paramType == double.class) {
					paramValue[0] = Double.valueOf(propertyValue);
				} else if (paramType == String.class) {
					paramValue[0] = propertyValue;
				} else if (paramType == long.class) {
					paramValue[0] = Long.valueOf(propertyValue);
				} else if (paramType == boolean.class) {
					paramValue[0] = Boolean.valueOf(propertyValue);
				}
				method.invoke(this, paramValue);
			}
		}
	}

	public String toJsonString() throws Exception {
		JSONObject json = new JSONObject();

		Class<?> clazz = this.getClass();
		Method[] methods = clazz.getMethods();
		Class<?>[] paramTypes;

		int methodsLength = methods.length;

		Method method;
		String methodName;
		String propertyName;

		Object value;
		Object[] paramValue = new Object[0];

		for (int i = 0; i < methodsLength; i++) {
			method = methods[i];
			methodName = method.getName();
			paramTypes = method.getParameterTypes();

			if (methodName.equals("getClass"))
				continue;

			if (paramTypes.length == 0 && methodName.startsWith("get")) {

				propertyName = methodName.substring(3, methodName.length())
						.toLowerCase();

				value = method.invoke(this, paramValue);

				json.put(propertyName, value);
			}
		}

		return json.toJSONString();
	}
}

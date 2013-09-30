package com.lzm.memcache;

import java.io.IOException;

import com.alibaba.fastjson.JSONObject;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

public class MemcacheHelper {
	private MemcachedClientBuilder builder;
	private MemcachedClient client;
	
	public MemcacheHelper(String cacheUrl){
		builder = new XMemcachedClientBuilder(AddrUtil.getAddresses(cacheUrl));
		try {
			client = builder.build();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ���
	 * @param key
	 * @param value
	 * @param time ����ʱ��
	 * */
	public void set(String key, String value, int time){
		try {
			if(client.isShutdown()){
				client = builder.build();
			}
			client.set(key, time, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ��ȡ
	 * */
	public String get(String key){
		String reStr = null;
		try {
			if(client.isShutdown()){
				client = builder.build();
			}
			reStr = client.get(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(reStr==null||reStr.equals("[]")||reStr.equals("{}")||reStr.equalsIgnoreCase("null")||reStr.equals("")){
			return null;
		}
		return reStr;
	}
	
	/**
	 * ����json����
	 * */
	public void setJson(String key, JSONObject value, int time){
		String jsonStr = value.toJSONString();
		set(key, jsonStr, time);
	}
	
	
	/**
	 * ��ȡһ��json����
	 * */
	public JSONObject getJson(String key){
		String jsonStr = get(key);
		if(jsonStr == null){
			return null;
		}
		JSONObject json = JSONObject.parseObject(jsonStr);
		return json;
	}
	
	/**
	 * ɾ��
	 * */
	public void delete(String key){
		try {
			if(client.isShutdown()){
				client = builder.build();
			}
			this.client.delete(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public MemcachedClient getClient(){
		return client;
	}
	
	
}

package lzm.redis;

import java.util.Set;

import lzm.redis.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSONObject;

public class RedisHelper {
	
	private JedisPool pool;
	
	private String host;
	private int port;
	private int dbIndex;
	
	public RedisHelper(RedisConfig config) {
		this.host = config.host;
		this.port = config.port;
		this.dbIndex = config.dbIndex;
	}
	
	public Jedis getJedis(){
		if(pool == null){
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.maxActive = 64;
			pool = new JedisPool(poolConfig, host, port);
		}
		
		Jedis jedis = pool.getResource();
		jedis.select(dbIndex);
		
		return jedis;
	}
	
	public void returnJedis(Jedis jedis){
		pool.returnResource(jedis);
	}
	
	/**
	 * 存放字符串
	 * @param key
	 * @param value
	 * @param time 过期时间
	 * */
	public void set(String key, String value, int seconds){
		Jedis jedis = getJedis();
		try {
			jedis.set(key, value);
			jedis.expire(key, seconds);
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * 获取字符串
	 * */
	public String get(String key){
		Jedis jedis = getJedis();
		try {
			String reStr = jedis.get(key);
			return reStr;
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * 从hashmap中取值
	 * */
	public String hashGet(String key,String field){
		Jedis jedis = getJedis();
		try {
			String reStr = jedis.hget(key, field);
			return reStr;
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * 往hashmap中存值
	 * */
	public void hashSet(String key,String field,String fieldValue){
		Jedis jedis = getJedis();
		try {
			jedis.hset(key, field, fieldValue);
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * 获取一个hashMap的key集合
	 * */
	public Set<String> hashKeys(String key){
		Jedis jedis = getJedis();
		try {
			Set<String> keys = jedis.hkeys(key);
			return keys;
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * 缓存json对象
	 * */
	public void setJson(String key, JSONObject value, int seconds){
		String jsonStr = value.toJSONString();
		set(key, jsonStr, seconds);
	}
	
	/**
	 * 获取一个json对象
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
	 * 删除
	 * */
	public void delete(String key){
		Jedis jedis = getJedis();
		try {
			jedis.del(key);
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * list 从左边添加元素
	 * */
	public void lpush(String key,String value){
		Jedis jedis = getJedis();
		try {
			jedis.lpush(key, value);
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * list 从右边取值
	 * */
	public String rpop(String key){
		Jedis jedis = getJedis();
		try {
			String reStr = jedis.rpop(key);
			return reStr;
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
	 * 设置过期时间
	 * */
	public void setTimeout(String key,int seconds){
		Jedis jedis = getJedis();
		try {
			jedis.expire(key, seconds);
		} finally{
			returnJedis(jedis);
		}
	}

}

package lzm.redis;

import java.util.List;
import java.util.Map;
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
     * 清空当前选择库
     * */
    public void flushDB(){
    	Jedis jedis = getJedis();
		try {
			jedis.flushDB();
		} finally{
			returnJedis(jedis);
		}
    }

    /**
     * 清空所有库
     * */
    public void flushAll(){
    	Jedis jedis = getJedis();
		try {
			jedis.flushAll();
		} finally{
			returnJedis(jedis);
		}
    }

    /**
     * 随机返回key空间的一个key
     * @return 
     * */
    public String randomKey(){
    	Jedis jedis = getJedis();
		try {
			return jedis.randomKey();
		} finally{
			returnJedis(jedis);
		}
    }
    
    /**
     * 判断某个key是否存在
     * */
    public boolean exists(String key){
    	Jedis jedis = getJedis();
		try {
			return jedis.exists(key);
		} finally{
			returnJedis(jedis);
		}
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
			if(seconds > 0) jedis.expire(key, seconds);
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
     * 在list右边头添加元素
     * */
    public void rpush(String key,String value){
    	Jedis jedis = getJedis();
		try {
			jedis.rpush(key, value);
		} finally{
			returnJedis(jedis);
		}
    }
    
    /**
     * 在list左边头删除元素，并且返回元素
     * */
    public String lpop(String key){
    	Jedis jedis = getJedis();
		try {
			return jedis.lpop(key);
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
			return jedis.rpop(key);
		} finally{
			returnJedis(jedis);
		}
	}
	
	/**
     * 获取list长度
     * */
    public long lsize(String key){
    	Jedis jedis = getJedis();
		try {
			return jedis.llen(key);
		} finally{
			returnJedis(jedis);
		}
    }
    
    /**
     *  设置指定位置的值
     * */
    public void lset(String key,long index,String value){
    	Jedis jedis = getJedis();
		try {
			jedis.lset(key, index, value);
		} finally{
			returnJedis(jedis);
		}
    }
    
    /**
     * 获取指定位置的值
     * */
    public String lindex(String key,long index){
    	Jedis jedis = getJedis();
		try {
			return jedis.lindex(key, index);
		} finally{
			returnJedis(jedis);
		}
    }
    
    public void ltrim(String key,long start,long end){
    	Jedis jedis = getJedis();
		try {
			jedis.ltrim(key, start, end);
		} finally{
			returnJedis(jedis);
		}
    }

    public long lrem(String key,String value,long count){
    	Jedis jedis = getJedis();
		try {
			return jedis.lrem(key, count, value);
		} finally{
			returnJedis(jedis);
		}
    }

    public List<String> lrange(String key,long start,long end){
    	Jedis jedis = getJedis();
		try {
			return jedis.lrange(key, start, end);
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
			return jedis.hget(key, field);
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
			return jedis.hkeys(key);
		} finally{
			returnJedis(jedis);
		}
	}
	
	public long hashLen(String key){
		Jedis jedis = getJedis();
		try {
			return jedis.hlen(key);
		} finally{
			returnJedis(jedis);
		}
    }

    public void hashDel(String key,String ... fields){
    	Jedis jedis = getJedis();
		try {
			jedis.hdel(key, fields);
		} finally{
			returnJedis(jedis);
		}
    }

    public List<String> hashVals(String key){
    	Jedis jedis = getJedis();
		try {
			return jedis.hvals(key);
		} finally{
			returnJedis(jedis);
		}
    }

    public Map<String, String> hash_hGetAll(String key){
    	Jedis jedis = getJedis();
		try {
			return jedis.hgetAll(key);
		} finally{
			returnJedis(jedis);
		}
    }

    public boolean hash_hExists(String key,String field){
    	Jedis jedis = getJedis();
		try {
			return jedis.hexists(key, field);
		} finally{
			returnJedis(jedis);
		}
    }

    public void hashMset(String key,Map<String,String> hash){
    	Jedis jedis = getJedis();
		try {
			jedis.hmset(key, hash);
		} finally{
			returnJedis(jedis);
		}
    }

    public List<String> hashMGet(String key,String []fields){
    	Jedis jedis = getJedis();
		try {
			return jedis.hmget(key, fields);
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

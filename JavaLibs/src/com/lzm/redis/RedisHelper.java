package com.lzm.redis;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.lzm.redis.config.RedisConfig;

/**
 * 键值数据库操作对象
 * 
 * @author lzm
 * 
 */
public class RedisHelper {
	private static JedisPool pool = null;
	private Jedis _client = null; // 操作对象
	private String host = "";
	private int port = 0;
	private int dbIndex = 0;
	
	private boolean isConn = false;
	
	public RedisHelper(RedisConfig config) {
		this.host = config.host;
		this.port = config.port;
		this.dbIndex = config.dbIndex;
	}

	private Jedis getJedis(){
		if(!isConn){
			JedisPoolConfig poolConfig = new JedisPoolConfig();
			poolConfig.maxActive = 400;
			poolConfig.maxIdle = 800;
			poolConfig.maxWait = 400;
			pool = new JedisPool(poolConfig, host, port);
			isConn = true;
		}
		
		Jedis tmpclient = null;
		try {
			tmpclient = pool.getResource();
			tmpclient.select(dbIndex);
			if(!tmpclient.isConnected()){
				isConn = false;
			}
		}catch(JedisConnectionException e){
			isConn = false;
			Logger.getLogger(RedisHelper.class).error(e);
		}
		return tmpclient;
	}
	
	/**
	 * 设置过期时间
	 * @param key
	 * @param seconds
	 * @return
	 */
	public Long expire(String key, int seconds){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.expire(key, seconds);
					tran.exec();
					return rp.get();
				}
				return _client.expire(key, seconds);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 是否存在元素
	 * @param key
	 * @return 
	 */
	public Boolean exists(String key) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Boolean> rp = null;
					Transaction tran = _client.multi();
					rp = tran.exists(key);
					tran.exec();
					return rp.get();
				}
				return _client.exists(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 删除keys
	 * @param keys
	 * @return
	 */
	public Long delete(String[] keys){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.del(keys);
					tran.exec();
					return rp.get();
				}
				return _client.del(keys);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 设置元素
	 * @param keys
	 * @return
	 */
	public String set(String key, String value){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<String> rp = null;
					Transaction tran = _client.multi();
					rp = tran.set(key, value);
					tran.exec();
					return rp.get();
				}
				return _client.set(key, value);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 获取元素
	 * @param keys
	 * @return
	 */
	public String get(String key){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<String> rp = null;
					Transaction tran = _client.multi();
					rp = tran.get(key);
					tran.exec();
					return rp.get();
				}
				return _client.get(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	// ------------------------------------------list
	/**
	 * 加入一个元素(可重复)
	 * @param key
	 * @param member
	 * @return
	 */
	public Long lpush(String key, String member){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.lpush(key, member);
					tran.exec();
					return rp.get();
				}
				Long l = _client.lpush(key, member);
				_client.expire(key, 60);
				return l;
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	//不重复
	public Long lpushx(String key, String member){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.lpushx(key, member);
					tran.exec();
					return rp.get();
				}
				return _client.lpushx(key, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 获取列表长度
	 * @param key
	 * @return
	 */
	public Long llen(String key){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.llen(key);
					tran.exec();
					return rp.get();
				}
				return _client.llen(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 删除count个名称为key的list中值为value的元素。
	 * count=0	删除所有值为value的元素，
	 * count>0	从头至尾删除count个值为value的元素
	 * count<0	从尾到头删除|count|个值为value的元素
	 * @param key
	 * @return
	 */
	public Long lrem(String key, int count, String value){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.lrem(key, count, value);
					tran.exec();
					return rp.get();
				}
				return _client.lrem(key, count, value);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}	
	
	/**
	 * 从右边获取一个元素并删除
	 * @return
	 */
	public String rpop(String key){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<String> rp = null;
					Transaction tran = _client.multi();
					rp = tran.rpop(key);
					tran.exec();
					return rp.get();
				}
				return _client.rpop(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 从左边获取几个
	 * @return
	 */
	public List<String> lrange(String key, int start, int end){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<List<String>> rp = null;
					Transaction tran = _client.multi();
					rp = tran.lrange(key, start, end);
					tran.exec();
					return rp.get();
				}
				return _client.lrange(key, start, end);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	/**
	 * 设置某编号下的数据
	 * @return
	 */
	public String lset(String key, int index, String value){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<String> rp = null;
					Transaction tran = _client.multi();
					rp = tran.lset(key, index, value);
					tran.exec();
					return rp.get();
				}
				return _client.lset(key, index, value);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	// ------------------------------------------set
	public Long sadd(String key, String value){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.sadd(key, value);
					tran.exec();
					return rp.get();
				}
				return _client.sadd(key, value);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	public Long scard(String key){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.scard(key);
					tran.exec();
					return rp.get();
				}
				return _client.scard(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	public Long scard(String key, String member){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.srem(key, member);
					tran.exec();
					return rp.get();
				}
				return _client.srem(key, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	public Boolean sismember(String key, String member){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Boolean> rp = null;
					Transaction tran = _client.multi();
					rp = tran.sismember(key, member);
					tran.exec();
					return rp.get();
				}
				return _client.sismember(key, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	public Set<String> smembers(String key){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Set<String>> rp = null;
					Transaction tran = _client.multi();
					rp = tran.smembers(key);
					tran.exec();
					return rp.get();
				}
				return _client.smembers(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	public Long srem(String key, String member){
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.srem(key, member);
					tran.exec();
					return rp.get();
				}
				return _client.srem(key, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
	
	// ------------------------------------------zset
	/**
	 * 像排序列表中添加或者修改一个元素
	 * 
	 * @param key
	 *            索引
	 * @param score
	 *            排序依据
	 * @param member
	 *            对象
	 * @return 设置返回为0 , 添加返回为1
	 */
	public Long zadd(String key, double score, String member) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.zadd(key, score, member);
					tran.exec();
					return rp.get();
				}
				return _client.zadd(key, score, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 删除一个元素
	 * 
	 * @param key
	 * @param member
	 * @return 1表示成功，如果元素不存在返回0
	 */
	public Long zrem(String key, String member) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.zrem(key, member);
					tran.exec();
					return rp.get();
				}
				return _client.zrem(key, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 按照排序后列表删除元素
	 * 
	 * @param key
	 * @param start
	 *            开始id
	 * @param end
	 *            结束id
	 * @return
	 */
	public Long zremrangeByRank(String key, int start, int end) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.zremrangeByRank(key, start, end);
					tran.exec();
					return rp.get();
				}
				return _client.zremrangeByRank(key, start, end);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 获取指定区间的元素列表(有序)
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public Set<String> zrange(String key, int start, int end) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Set<String>> rp = null;
					Transaction tran = _client.multi();
					rp = tran.zrange(key, start, end);
					tran.exec();
					return rp.get();
				}
				return _client.zrange(key, start, end);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 获取列表的长度
	 * 
	 * @param key
	 * @return
	 */
	public Long zcard(String key) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.zcard(key);
					tran.exec();
					return rp.get();
				}
				return _client.zcard(key);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 获取元素的score值
	 * 
	 * @param key
	 * @param member
	 * @return
	 */
	public Double zscore(String key, String member) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Double> rp = null;
					Transaction tran = _client.multi();
					rp = tran.zscore(key, member);
					tran.exec();
					return rp.get();
				}
				return _client.zscore(key, member);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	// ------------------------------------------hash
	/**
	 * 设置或者添加某hashmap中对应的值(单个设置)
	 * 
	 * @param key
	 *            map名
	 * @param field
	 *            map中的键名
	 * @param value
	 *            对应的值
	 * @return 设置返回为0 , 添加返回为1
	 */
	public Long hset(String key, String field, String value) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.hset(key, field, value);
					tran.exec();
					return rp.get();
				}
				return _client.hset(key, field, value);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}

	/**
	 * 获取某hashmap中对应的值
	 * 
	 * @param key
	 *            map名
	 * @param field
	 *            map中的键名
	 * @return 键值
	 */
	public String hget(String key, String field) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<String> rp = null;
					Transaction tran = _client.multi();
					rp = tran.hget(key, field);
					tran.exec();
					return rp.get();
				}
				return _client.hget(key, field);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		Logger.getLogger(RedisHelper.class).info("redis can not connect!");
		return null;
	}

	/**
	 * 删除某hashmap中对应的值
	 * 
	 * @param key
	 *            map名
	 * @param field
	 *            map中的键名
	 * @return 未找到或者失败为0 , 成功为1
	 */
	public Long hdel(String key, String field) {
		try {
			_client = getJedis();
			if(_client == null || !_client.isConnected()){
				return null;
			}else{
				if(_client.getClient().isInMulti()){
					Response<Long> rp = null;
					Transaction tran = _client.multi();
					rp = tran.hdel(key, field);
					tran.exec();
					return rp.get();
				}
				return _client.hdel(key, field);
			}
		} catch (Exception e) {
			Logger.getLogger(RedisHelper.class).error(e);
		} finally{
			pool.returnResource(_client);
		}
		return null;
	}
}

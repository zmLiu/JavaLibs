package com.lzm.db;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

import com.lzm.db.config.DBConfig;
import com.lzm.db.config.PoolConfig;

public class DBUtil {

	public static DBUtil getDBUtil(DBConfig dbConfig, PoolConfig poolConfig) {

		ConnectionFactory factory = new ConnectionFactory(dbConfig);

		Config config = new Config();
		config.lifo = poolConfig.lifo;
		config.maxActive = poolConfig.maxActive;
		config.maxIdle = poolConfig.maxIdle;
		config.maxWait = poolConfig.maxWait;
		config.minEvictableIdleTimeMillis = poolConfig.minEvictableIdleTimeMillis;
		config.minIdle = poolConfig.minIdle;
		config.numTestsPerEvictionRun = poolConfig.numTestsPerEvictionRun;
		config.testOnBorrow = poolConfig.testOnBorrow;
		config.testOnReturn = poolConfig.testOnReturn;
		config.testWhileIdle = poolConfig.testWhileIdle;
		config.timeBetweenEvictionRunsMillis = poolConfig.timeBetweenEvictionRunsMillis;
		
		GenericObjectPool<Connection> connPool = new GenericObjectPool<Connection>(factory, config);

		return new DBUtil(connPool);
	}
	
	private GenericObjectPool<Connection> connPool;

	private DBUtil(GenericObjectPool<Connection> connPool) {
		this.connPool = connPool;
	}
	//查
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	public List<Object> queryClass(String sql, Object[] params, Class clazz)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Object> list = new QueryRunner().query(conn, sql, params, new BeanListHandler<Object>(clazz));
			return list;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//查
	@SuppressWarnings("deprecation")
	public List<Object[]> queryList(String sql, Object[] params)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Object[]> list = new QueryRunner().query(conn, sql, params, new ArrayListHandler());
			return list;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//查
	@SuppressWarnings("deprecation")
	public List<Map<String, Object>> queryListMap(String sql, Object[] params)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Map<String, Object>> list = new QueryRunner().query(conn, sql, params, new MapListHandler());
			return list;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//增删改
	public int update(String sql) throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			int lineCount = new QueryRunner().update(conn, sql);
			return lineCount;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//增删改
	public int update(String sql, Object[] params) throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			int lineCount = new QueryRunner().update(conn, sql, params);
			return lineCount;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//插入数据并且返回自增id
	public long insert_returnInsertId(String sql) throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			QueryRunner q = new QueryRunner();
			q.update(conn, sql);
			long id = (Long)q.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<Object>(1));
			return id;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//插入数据并且返回自增id
	public long insert_returnInsertId(String sql, Object[] params) throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			QueryRunner q = new QueryRunner();
			q.update(conn, sql, params);
			long id = (Long)q.query(conn, "SELECT LAST_INSERT_ID()", new ScalarHandler<Object>(1));
			return id;
		}finally{
			connPool.returnObject(conn);
		}
	}

}

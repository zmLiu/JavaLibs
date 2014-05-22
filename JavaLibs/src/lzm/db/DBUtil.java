package lzm.db;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lzm.db.config.DBConfig;
import lzm.db.config.PoolConfig;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;

public class DBUtil {

	public static DBUtil getDBUtil(DBConfig dbConfig, PoolConfig poolConfig) {

		ConnectionFactory factory = new ConnectionFactory(dbConfig);
		
		if(poolConfig == null) poolConfig = new PoolConfig();

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
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	public Object queryOneClass(String sql, Object[] params, Class clazz)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Object> list = new QueryRunner().query(conn, sql, params, new BeanListHandler<Object>(clazz));
			if(list.size() > 0){
				return list.get(0);
			}
			return null;
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
	public Object[] queryOneList(String sql, Object[] params)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Object[]> list = new QueryRunner().query(conn, sql, params, new ArrayListHandler());
			if(list.size() > 0){
				return list.get(0);
			}
			return null;
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
	
	/** 
	 * 获取一条查询语句  
	 * @param	table	表名
	 * @param	clazz	对应的bean类
	 * @param	where	查询条件
	 * */
	@SuppressWarnings("rawtypes")
	public static String selectSql_MySql(String table,Class clazz,Map<String, Object> where) throws Exception{
		table.trim();
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		
		BeanInfo beanInfo = getBeanInfo(clazz);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		PropertyDescriptor propertyDescriptor;
		
		int propertyLength = propertyDescriptors.length;
		String propertyName;
		for (int i = 0; i < propertyLength; i++) {
			propertyDescriptor = propertyDescriptors[i];
			propertyName = propertyDescriptor.getName();
			if(propertyName.equals("class")){
				continue;
			}
			if(propertyDescriptor.getReadMethod() != null && propertyDescriptor.getWriteMethod() != null){
				query.append(propertyName).append(",");
			}
		}
		query.deleteCharAt(query.length() - 1);
		query.append(" FROM `").append(table).append("`");
		
		if(where != null && where.size() > 0){
			query.append(" WHERE ");
			String key;
			Object value;
			for (Iterator<String> iterator = where.keySet().iterator(); iterator.hasNext();) {
				key = iterator.next();
				value = where.get(key);
				
				query.append("`").append(key).append("` = '").append(value).append("' AND ");
			}
			
			int queryLength = query.length();
			query.delete(queryLength - 5, queryLength);
		}
		
		return query.toString();
	}
	
	
	/**
	 * 获取一条插入语句
	 * @param	table	表名
	 * @param 	data	需要插入的bean对象
	 * */
	public static String insertSql_MySql(String table,Object data) throws Exception{
		StringBuilder query = new StringBuilder("INSERT IGNORE INTO `");
		query.append(table);
		query.append("` SET ");
		
		BeanInfo beanInfo = getBeanInfo(data.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		
		int propertyLength = propertyDescriptors.length;
		PropertyDescriptor propertyDescriptor;
		Method readMethod;
		String propertyName;
		Object[] methodParams = new Object[0];
		Object value;
		
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
			
			value = readMethod.invoke(data, methodParams);
			if(value != null){
				query.append("`");
				query.append(propertyName);
				query.append("` = '");
				query.append(readMethod.invoke(data, methodParams));
				query.append("', ");
			}
		}
		
		int queryLength = query.length();
		query.delete(queryLength - 2, queryLength);
		
		return query.toString();
	}
	
	/**
	 * 获取一条sql语句
	 * @param	table	表名
	 * @param	where	条件
	 * */
	public static String deleteSql_MySql(String table,Map<String, Object> where){
		StringBuilder query = new StringBuilder("DELETE FROM `");
		query.append(table);
		query.append("`");
		
		if(where != null && where.size() > 0){
			query.append(" WHERE ");
			String key;
			Object value;
			for (Iterator<String> iterator = where.keySet().iterator(); iterator.hasNext();) {
				key = iterator.next();
				value = where.get(key);
				
				query.append("`").append(key).append("` = '").append(value).append("' AND ");
			}
			
			int queryLength = query.length();
			query.delete(queryLength - 5, queryLength);
		}
		
		return query.toString();
	}
	
	/**
	 * 获取一条删除语句
	 * @param	table	表名
	 * @param	set		更新字段
	 * @param	where	更新条件
	 * */
	public static String updateSql_MySql(String table,Map<String, Object> set,Map<String, Object> where){
		if(set == null || set.size() == 0){
			return null;
		}
		
		StringBuilder query = new StringBuilder("UPDATE `");
		query.append(table);
		query.append("` SET ");
		
		String key;
		Object value;
		for (Iterator<String> iterator = set.keySet().iterator(); iterator.hasNext();) {
			key = iterator.next();
			value = set.get(key);
			
			query.append("`").append(key).append("` = '").append(value).append("', ");
		}
		
		int queryLength = query.length();
		query.delete(queryLength - 2, queryLength);
		
		if(where != null && where.size() > 0){
			query.append(" WHERE ");
			for (Iterator<String> iterator = where.keySet().iterator(); iterator.hasNext();) {
				key = iterator.next();
				value = where.get(key);
				
				query.append("`").append(key).append("` = '").append(value).append("' AND ");
			}
			
			queryLength = query.length();
			query.delete(queryLength - 5, queryLength);
		}
		
		return query.toString();
	}
	
	
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
	
	

}

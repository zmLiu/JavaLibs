package lzm.db;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import lzm.db.config.DBConfig;
import lzm.db.config.PoolConfig;
import lzm.utils.BeanUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
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
	@SuppressWarnings("deprecation")
	public <T> List<T> queryClass(String sql, Class<T> clazz)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<T> list = new QueryRunner().query(conn, sql, null, new BeanListHandler<T>(clazz));
			return list;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//查
	@SuppressWarnings("deprecation")
	public <T> List<T> queryClass(String sql, Object[] params, Class<T> clazz)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<T> list = new QueryRunner().query(conn, sql, params, new BeanListHandler<T>(clazz));
			return list;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//查
	@SuppressWarnings("deprecation")
	public <T> T queryOneClass(String sql, Class<T> clazz)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			T data = new QueryRunner().query(conn, sql, null, new BeanHandler<T>(clazz));
			return data;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//查
	@SuppressWarnings("deprecation")
	public <T> T queryOneClass(String sql, Object[] params, Class<T> clazz)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			T data = new QueryRunner().query(conn, sql, params, new BeanHandler<T>(clazz));
			return data;
		}finally{
			connPool.returnObject(conn);
		}
	}
	
	//查
	@SuppressWarnings("deprecation")
	public List<Object[]> queryList(String sql)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Object[]> list = new QueryRunner().query(conn, sql, null, new ArrayListHandler());
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
	public Object[] queryOneList(String sql)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			Object[] data = new QueryRunner().query(conn, sql, null, new ArrayHandler());
			return data;
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
	public List<Map<String, Object>> queryListMap(String sql)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			List<Map<String, Object>> list = new QueryRunner().query(conn, sql, null, new MapListHandler());
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
	//查
	@SuppressWarnings("deprecation")
	public Map<String, Object> queryOneMap(String sql)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			Map<String, Object> map = new QueryRunner().query(conn, sql, null, new MapHandler());
			return map;
		}finally{
			connPool.returnObject(conn);
		}
	}
	//查
	@SuppressWarnings("deprecation")
	public Map<String, Object> queryOneMap(String sql, Object[] params)throws Exception {
		Connection conn = connPool.borrowObject();
		try{
			Map<String, Object> map = new QueryRunner().query(conn, sql, params, new MapHandler());
			return map;
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
	public static String selectSql_MySql(String table,Class<?> clazz,Map<String, Object> where) throws Exception{
		table.trim();
		
		secureData(where);
		
		StringBuilder query = new StringBuilder();
		query.append("SELECT ");
		
		BeanInfo beanInfo = BeanUtil.getBeanInfo(clazz);
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
		
		BeanInfo beanInfo = BeanUtil.getBeanInfo(data.getClass());
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
		
		secureData(where);
		
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
		
		secureData(set);
		secureData(where);
		
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
	
	/** 防止sql注入 */
	private static void secureData(Map<String, Object> data){
		if(data == null) return;
		String key;
		Object value;
		for (Iterator<String> iterator = data.keySet().iterator(); iterator.hasNext();) {
			key = iterator.next();
			value = data.get(key);
			
			data.put(key, TransactSQLInjection(value.toString()));
		}
	}
	
	public static String TransactSQLInjection(String str){
          return str.replaceAll(".*([';]+|(--)+).*", "");
    }
	

}

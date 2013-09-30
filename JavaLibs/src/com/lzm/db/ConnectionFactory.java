package com.lzm.db;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.log4j.Logger;

import com.lzm.db.config.DBConfig;

public class ConnectionFactory implements PoolableObjectFactory<Connection> {
	
	private ConnectionTool connTool;
	
	public ConnectionFactory(DBConfig dbConfig) {
		connTool = new ConnectionTool(dbConfig);
	}

	@Override
	public void activateObject(Connection conn) throws Exception {
		//激活对象(进入非空闲状态)
	}

	@Override
	public void destroyObject(Connection conn) throws Exception {
		//销毁对象
		conn.close();
	}

	@Override
	public Connection makeObject() throws Exception {
		//新建对象
		return connTool.makeConnection();
	}

	@Override
	public void passivateObject(Connection conn) throws Exception {
		//挂起对象(进入空闲状态)
	}

	@Override
	public boolean validateObject(Connection conn) {
		//坚持对象是否可用
		try {
			return !conn.isClosed();
		} catch (SQLException e) {
			Logger logger = Logger.getLogger(ConnectionFactory.class);
			logger.error(e.getMessage());
			StackTraceElement[] error = e.getStackTrace();
		    for (StackTraceElement stackTraceElement : error) {  
		    	logger.error(stackTraceElement.toString());
		    }
		}
		return false;
	}

}

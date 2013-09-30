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
		//�������(����ǿ���״̬)
	}

	@Override
	public void destroyObject(Connection conn) throws Exception {
		//���ٶ���
		conn.close();
	}

	@Override
	public Connection makeObject() throws Exception {
		//�½�����
		return connTool.makeConnection();
	}

	@Override
	public void passivateObject(Connection conn) throws Exception {
		//�������(�������״̬)
	}

	@Override
	public boolean validateObject(Connection conn) {
		//��ֶ����Ƿ����
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

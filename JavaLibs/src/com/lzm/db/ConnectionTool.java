package com.lzm.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbutils.DbUtils;

import com.lzm.db.config.DBConfig;

public class ConnectionTool {

	private Properties config;
	private DBConfig dbConfig;
	
	public ConnectionTool(DBConfig dbConfig) {
		this.dbConfig = dbConfig;
	}

	public Connection makeConnection() throws SQLException {
		
		if(config == null){
			config = new Properties();
			config.put("user", dbConfig.user);
			config.put("password", dbConfig.password);
			config.put("useUnicode", dbConfig.useUnicode);
			config.put("characterEncoding", dbConfig.characterEncoding);
		}
		
		DbUtils.loadDriver(dbConfig.dirverClassName);
		
		Connection conn = DriverManager.getConnection(dbConfig.url,config);

		return conn;
	}

}

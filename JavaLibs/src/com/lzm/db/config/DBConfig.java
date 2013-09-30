package com.lzm.db.config;

public class DBConfig {
	/**
	 * 数据库驱动
	 * <p>
	 * 默认:com.mysql.jdbc.Driver
	 * <p>
	 * */
	public String dirverClassName = "com.mysql.jdbc.Driver";
	
	/**
	 * 数据库url
	 * */
	public String url;//mysql "jdbc:mysql://127.0.0.1:3306/test"
	
	/**
	 * 数据库用户
	 * */
	public String user;
	
	/**
	 * 数据库密码
	 * */
	public String password;
	
	/**
	 * 数据库是否启用Unicode
	 * <p>
	 * 默认:true
	 * <p>
	 * */
	public String useUnicode = "true";
	
	/**
	 * 数据库编码
	 * <p>
	 * 默认:utf8
	 * <p>
	 * */
	public String characterEncoding = "utf8";
}

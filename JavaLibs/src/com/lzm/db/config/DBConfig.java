package com.lzm.db.config;

public class DBConfig {
	/**
	 * ���ݿ�����
	 * <p>
	 * Ĭ��:com.mysql.jdbc.Driver
	 * <p>
	 * */
	public String dirverClassName = "com.mysql.jdbc.Driver";
	
	/**
	 * ���ݿ�url
	 * */
	public String url;//mysql "jdbc:mysql://127.0.0.1:3306/test"
	
	/**
	 * ���ݿ��û�
	 * */
	public String user;
	
	/**
	 * ���ݿ�����
	 * */
	public String password;
	
	/**
	 * ���ݿ��Ƿ�����Unicode
	 * <p>
	 * Ĭ��:true
	 * <p>
	 * */
	public String useUnicode = "true";
	
	/**
	 * ���ݿ����
	 * <p>
	 * Ĭ��:utf8
	 * <p>
	 * */
	public String characterEncoding = "utf8";
}

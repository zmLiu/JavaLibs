package com.lzm.db.config;

public class PoolConfig {
	/** 设置后进先出的池策略 */
	public boolean lifo = true;

	/** 最大对象数 */
	public int maxActive = 24;

	/** 最大空闲对象 */
	public int maxIdle = 6;

	/** 获取对象的等待时间 */
	public long maxWait = 120000;

	/** 被空闲对象回收器回收前在池中保持空闲状态的最小时间毫秒数 */
	public long minEvictableIdleTimeMillis = 120000;

	/** 最小空闲对象 */
	public int minIdle = 0;

	/** 设定在进行后台对象清理时，每次检查对象数 */
	public int numTestsPerEvictionRun = 1;

	/** 指明是否在从池中取出对象前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个 */
	public boolean testOnBorrow = true;

	/** 指明是否在归还到池中前进行检验 */
	public boolean testOnReturn = true;

	/** 是否测试空闲对象 */
	public boolean testWhileIdle = true;

	/** 检测线程运行时间 */
	public long timeBetweenEvictionRunsMillis = 120000;
}

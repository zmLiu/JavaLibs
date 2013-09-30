package com.lzm.db.config;

public class PoolConfig {
	/** ���ú���ȳ��ĳز��� */
	public boolean lifo = true;

	/** �������� */
	public int maxActive = 24;

	/** �����ж��� */
	public int maxIdle = 6;

	/** ��ȡ����ĵȴ�ʱ�� */
	public long maxWait = 120000;

	/** �����ж������������ǰ�ڳ��б��ֿ���״̬����Сʱ������� */
	public long minEvictableIdleTimeMillis = 120000;

	/** ��С���ж��� */
	public int minIdle = 0;

	/** �趨�ڽ��к�̨��������ʱ��ÿ�μ������� */
	public int numTestsPerEvictionRun = 1;

	/** ָ���Ƿ��ڴӳ���ȡ������ǰ���м���,�������ʧ��,��ӳ���ȥ�����Ӳ�����ȡ����һ�� */
	public boolean testOnBorrow = true;

	/** ָ���Ƿ��ڹ黹������ǰ���м��� */
	public boolean testOnReturn = true;

	/** �Ƿ���Կ��ж��� */
	public boolean testWhileIdle = true;

	/** ����߳�����ʱ�� */
	public long timeBetweenEvictionRunsMillis = 120000;
}

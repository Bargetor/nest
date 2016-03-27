/**
 * bargetorCommon
 * com.bargetor.nest.common.jedis
 * JedisConfig.java
 * 
 * 2015年4月26日-下午2:33:20
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.redis;

/**
 *
 * JedisConfig
 * 
 * kin kin 2015年4月26日 下午2:33:20
 * 
 * @version 1.0.0
 *
 */
public class JedisConfig {
	private String ip;
	private int port;

	// 访问密码
	private String auth;

	// 可用连接实例的最大数目，默认值为8；
	// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
	private int maxActive = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private int maxIDLE = 200;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
	private int maxWait = 10000;

	private int timeout = 10000;
	
	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private boolean testOnBorrow = true;

	/**
	 * ip
	 *
	 * @return  the ip
	 * @since   1.0.0
	 */
	
	public String getIp() {
		return ip;
	}

	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * port
	 *
	 * @return  the port
	 * @since   1.0.0
	 */
	
	public int getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * auth
	 *
	 * @return  the auth
	 * @since   1.0.0
	 */
	
	public String getAuth() {
		return auth;
	}

	/**
	 * @param auth the auth to set
	 */
	public void setAuth(String auth) {
		this.auth = auth;
	}

	/**
	 * maxActive
	 *
	 * @return  the maxActive
	 * @since   1.0.0
	 */
	
	public int getMaxActive() {
		return maxActive;
	}

	/**
	 * @param maxActive the maxActive to set
	 */
	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	/**
	 * maxIDLE
	 *
	 * @return  the maxIDLE
	 * @since   1.0.0
	 */
	
	public int getMaxIDLE() {
		return maxIDLE;
	}

	/**
	 * @param maxIDLE the maxIDLE to set
	 */
	public void setMaxIDLE(int maxIDLE) {
		this.maxIDLE = maxIDLE;
	}

	/**
	 * maxWait
	 *
	 * @return  the maxWait
	 * @since   1.0.0
	 */
	
	public int getMaxWait() {
		return maxWait;
	}

	/**
	 * @param maxWait the maxWait to set
	 */
	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

	/**
	 * timeout
	 *
	 * @return  the timeout
	 * @since   1.0.0
	 */
	
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * testOnBorrow
	 *
	 * @return  the testOnBorrow
	 * @since   1.0.0
	 */
	
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	/**
	 * @param testOnBorrow the testOnBorrow to set
	 */
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	
	
}

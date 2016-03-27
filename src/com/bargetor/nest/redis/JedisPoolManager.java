/**
 * bargetorCommon
 * com.bargetor.nest.common.jedis
 * JedisPoolManager.java
 * 
 * 2015年4月26日-上午11:31:58
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.redis;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * JedisPoolManager
 * 
 * kin kin 2015年4月26日 上午11:31:58
 * 
 * @version 1.0.0
 *
 */
public class JedisPoolManager {
	protected static final Logger logger = Logger.getLogger(JedisPoolManager.class);
	
	/**
	 * RETRY:获取实例重试次数
	 *
	 * @since 1.0.0
	 */
	private static int RETRY = 10;

	private static JedisPoolManager instance;
	private Map<String, JedisPool> poolMaps = new HashMap<String, JedisPool>();

	public static JedisPoolManager getInstance() {
		if (instance == null) {
			instance = new JedisPoolManager();
		}
		return instance;
	}
	
	public Jedis getJedis(JedisConfig config){
		Jedis jedis = null;
		int count = 0;
		JedisPool pool = this.getPool(config);
		do {
			try {
				jedis = pool.getResource();
				// log.info("get redis master1!");
			} catch (Exception e) {
				logger.error("get redis master1 failed!", e);
				// 销毁对象
				this.closeJedis(pool, jedis);
			}
			count++;
		} while (jedis == null && count < RETRY);
		return jedis;
	}

	public Jedis getJedis(String ip, int port) {
		JedisConfig config = new JedisConfig();
		config.setIp(ip);
		config.setPort(port);
		return this.getJedis(config);
	}

	/**
	 * closeJedis(释放redis实例到连接池.) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param jedis
	 * @param ip
	 * @param port
	 *            void
	 * @exception
	 * @since 1.0.0
	 */
	public void closeJedis(Jedis jedis, String ip, int port) {
		if (jedis != null) {
			JedisPool pool = this.getPool(ip, port);
			this.closeJedis(pool, jedis);
		}
	}
	
	public void closeJedis(JedisPool pool, Jedis jedis){
		if (jedis != null && pool != null) {
			pool.returnResourceObject(jedis);
		}
	}

	protected JedisPoolManager() {

	}
	
	/**
	 * getPool(获取连接池)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param config
	 * @return
	 *JedisPool
	 * @exception
	 * @since  1.0.0
	*/
	private JedisPool getPool(JedisConfig config){
		String key = config.getIp() + ":" + config.getPort();
		JedisPool pool = this.poolMaps.get(key);
		if (pool == null) {
			pool = this.buildJedisPool(config);
		}
		return pool;
	}

	/**
	 * getPool(获取连接池) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param ip
	 * @param port
	 * @return JedisPool
	 * @exception
	 * @since 1.0.0
	 */
	private JedisPool getPool(String ip, int port) {
		JedisConfig config = new JedisConfig();
		config.setIp(ip);
		config.setPort(port);
		return this.getPool(config);
	}
	
	/**
	 * buildJedisPool(创建连接池)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param config
	 * @return
	 *JedisPool
	 * @exception
	 * @since  1.0.0
	*/
	private JedisPool buildJedisPool(JedisConfig config){
		String key = config.getIp() + ":" + config.getPort();
		JedisPool pool = null;
		// 这里的同步代码块防止多个线程同时产生多个相同的ip线程池
		synchronized (JedisPoolManager.class) {
			if (!poolMaps.containsKey(key)) {
				JedisPoolConfig poolConfig = new JedisPoolConfig();
				poolConfig.setMaxTotal(config.getMaxActive());
				poolConfig.setMaxIdle(config.getMaxIDLE());
				poolConfig.setMaxWaitMillis(config.getMaxWait());
				poolConfig.setTestOnBorrow(config.isTestOnBorrow());
				poolConfig.setTestOnReturn(true);
				try {
					/**
					 * 如果你遇到 java.net.SocketTimeoutException: Read timed out
					 * exception的异常信息 请尝试在构造JedisPool的时候设置自己的超时值.
					 * JedisPool默认的超时时间是2秒(单位毫秒)
					 */
					pool = new JedisPool(poolConfig, config.getIp(), config.getPort(), config.getTimeout(), config.getAuth());
					poolMaps.put(key, pool);
				} catch (Exception e) {
					logger.error("build jedis pool error", e);
				}
			} else {
				pool = poolMaps.get(key);
			}
		}
		return pool;
	}
}

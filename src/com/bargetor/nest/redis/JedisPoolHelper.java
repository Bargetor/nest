/**
 * bargetorCommon
 * com.bargetor.nest.common.jedis
 * JedisPoolHelper.java
 * 
 * 2015年4月26日-下午12:26:10
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.redis;

import redis.clients.jedis.Jedis;


/**
 *
 * JedisPoolHelper
 * 
 * kin
 * kin
 * 2015年4月26日 下午12:26:10
 * 
 * @version 1.0.0
 *
 */
public class JedisPoolHelper {
	
	private JedisConfig config;
	
	public JedisPoolHelper(){
		
	}
	
	/**
	 * getJedis(获取jedis)
	 * (这里描述这个方法适用条件 – 可选)
	 * @return
	 *Jedis
	 * @exception
	 * @since  1.0.0
	*/
	public Jedis getJedis(){
		return JedisPoolManager.getInstance().getJedis(config);
	}

	/**
	 * config
	 *
	 * @return  the config
	 * @since   1.0.0
	 */
	
	public JedisConfig getConfig() {
		return config;
	}

	/**
	 * @param config the config to set
	 */
	public void setConfig(JedisConfig config) {
		this.config = config;
	}
	
	
}

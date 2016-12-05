package com.bargetor.nest.redis;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.bargetor.nest.common.util.StringUtil;
import org.apache.log4j.Logger;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {
	private static final Logger logger = Logger.getLogger(RedisManager.class);
	
	private String host;
	
	private int port;
	
	// 0 - never expire
	private int expire = 0;
	
	//timeout for jedis try to connect to redis server, not expire time! In milliseconds
	private int timeout = 0;
	
	private String password;

	private int database = 0;
	
	private JedisPool jedisPool = null;
	
	/**
	 * 初始化方法
	 */
	public void init(){
		if(this.jedisPool != null){
			this.jedisPool.destroy();
			this.jedisPool = null;
		}
		this.jedisPool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password, database);
	}
	
	/**
	 * getMap(获取map)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param key
	 * @return
	 * Map<String,String>
	 * @exception
	 * @since  1.0.0
	*/
	public Map<String, String> getMap(String key){
		Map<String, String> value = null;
		Jedis jedis = jedisPool.getResource();
		try{
			if(jedis.hlen(key) <= 0)return value;
			value = new HashMap<String, String>();
			Iterator<String> iter = jedis.hkeys(key).iterator();  
			while (iter.hasNext()){  
				String name = iter.next();
				List<String> v = jedis.hmget(key, name);
				if(v == null){
					value.put(name, null);
					continue;
				}
				value.put(name, v.get(0));
			}
			
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		
		return value;
	}
	
	/**
	 * setMap(保存map)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param key
	 * @param map
	 * void
	 * @exception
	 * @since  1.0.0
	*/
	public void setMap(String key, Map<String, String> map){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.hmset(key, map);
			if(this.expire != 0){
				jedis.expire(key, this.expire);
		 	}
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
	}

	public long ttl(String key){
		Jedis jedis = jedisPool.getResource();
		try{
			return jedis.ttl(key);
		}finally {
			jedisPool.returnResourceObject(jedis);
		}
	}

	public boolean exist(String key){
		Jedis jedis = jedisPool.getResource();
		try{
			return jedis.exists(key);
		}finally {
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	/**
	 * get value from redis
	 * @param key
	 * @return
	 */
	public byte[] get(byte[] key){
		byte[] value = null;
		Jedis jedis = jedisPool.getResource();
		try{
			value = jedis.get(key);
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		return value;
	}

	public String get(String key){
		Jedis jedis = jedisPool.getResource();
		try{
			return jedis.get(key);
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @return
	 */
	public byte[] set(byte[] key,byte[] value){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.set(key,value);
			if(this.expire != 0){
				jedis.expire(key, this.expire);
		 	}
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		return value;
	}
	
	/**
	 * set 
	 * @param key
	 * @param value
	 * @param expire
	 * @return
	 */
	public byte[] set(byte[] key,byte[] value,int expire){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.set(key, value);
			if(expire != 0){
				jedis.expire(key, expire);
		 	}
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		return value;
	}

	/**
	 * 原子
	 * @param key
	 * @param value
	 * @return
	 */
	public long incrBy(String key, long value){
		return this.incrBy(key, value, 0);
	}

	public long incr(String key){
		Jedis jedis = jedisPool.getResource();
		try{
			return jedis.incr(key);
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
	}

	public long incrBy(String key, long value, int expire){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.incrBy(key, value);
			if(expire != 0){
				jedis.expire(key, expire);
			}
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		return value;
	}
	
	/**
	 * del
	 * @param key
	 */
	public void del(byte[] key){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.del(key);
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	/**
	 * del
	 * @param key
	 */
	public void del(String key){
		this.del(key.getBytes());
	}
	
	/**
	 * flush
	 */
	public void flushDB(){
		Jedis jedis = jedisPool.getResource();
		try{
			jedis.flushDB();
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
	}
	
	/**
	 * size
	 */
	public Long dbSize(){
		Long dbSize = 0L;
		Jedis jedis = jedisPool.getResource();
		try{
			dbSize = jedis.dbSize();
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		return dbSize;
	}

	/**
	 * keys
	 * @param pattern
	 * @return
	 */
	public Set<byte[]> keys(String pattern){
		Set<byte[]> keys = null;
		Jedis jedis = jedisPool.getResource();
		try{
			keys = jedis.keys(pattern.getBytes());
		}finally{
			jedisPool.returnResourceObject(jedis);
		}
		return keys;
	}

	public Object getObject(String key){
		if(StringUtil.isNullStr(key))return null;
		byte[] value = this.get(key.getBytes());
		return toObject(value);
	}

	public Object putObject(String key, Object value){
		if(StringUtil.isNullStr(key) || value == null)return null;
		this.set(key.getBytes(), toByteArray(value));
		return value;
	}

	private Object toObject(byte[] bytes) {
		if(bytes == null)return null;
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException | ClassNotFoundException ex) {
			logger.error("cache to object error", ex);
		}
		return obj;
	}

	private byte[] toByteArray(Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}
	
	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}
}

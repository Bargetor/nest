package com.bargetor.nest.cache;

import com.bargetor.nest.redis.RedisManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Created by Bargetor on 16/3/30.
 */
public class RedisCache implements Cache, InitializingBean {
    private static final Logger logger = Logger.getLogger(RedisCache.class);

    private String name;
    private RedisManager redisManager;
    private int liveTime;
    private int redisDatabase;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.redisManager.setExpire(this.liveTime);
        this.redisManager.setDatabase(this.redisDatabase);
        this.redisManager.init();
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.redisManager;
    }

    @Override
    public ValueWrapper get(Object key) {
        if(key == null)return null;
        Object value = this.redisManager.getObject((String)key);
        if(value == null)return null;
        logger.info(String.format("%s cache hit -> %s", this.getName(), key));
        return new SimpleValueWrapper(value);
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        Object value = this.redisManager.getObject((String) key);
        if(value == null)return null;
        if(type.equals(value.getClass()))return (T)value;
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        String keyStr = (String)key;
        this.redisManager.putObject(keyStr, value);
        logger.info(String.format("%s cache put -> %s", this.getName(), key));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        this.put(key, value);
        return new SimpleValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        this.redisManager.del((String) key);
    }

    @Override
    public void clear() {
        this.redisManager.flushDB();
    }

    public void setName(String name) {
        this.name = name;
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    public int getLiveTime() {
        return liveTime;
    }

    public void setLiveTime(int liveTime) {
        this.liveTime = liveTime;
    }

    public int getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(int redisDatabase) {
        this.redisDatabase = redisDatabase;
    }


    /************************************ private method **********************************/

}

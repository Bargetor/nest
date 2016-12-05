package com.bargetor.nest.ratelimit;

import com.bargetor.nest.redis.RedisManager;
import com.google.common.util.concurrent.Uninterruptibles;

import java.util.concurrent.TimeUnit;

/**
 * Created by bargetor on 2016/12/4.
 * 利用redis实现的频率限制，主要用于集群场景
 */
public class RedisRateLimiter implements RateLimiter{
    private final static String redis_rate_limiter_key_prefix = "redis_rate_limiter_key";
    private String key;
    private String redisKey;
    private RedisManager redisManager;
    private long permits;
    private long period;
    private double rate;

    public RedisRateLimiter(String key, long permits, long period, RedisManager redisManager){
        this.key = key;
        this.period = period;
        this.permits = permits;
        this.rate = (1.0 * permits) / (1.0 * period);
        this.redisManager = redisManager;
        this.redisKey = this.buildRedisKey(key);
        this.setNewLimitToRedis();
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public double acquire() {
        long waitTime = this.reserveAndGetWaitTime();
        Uninterruptibles.sleepUninterruptibly(waitTime, TimeUnit.MILLISECONDS);
        if(waitTime > 0){
            waitTime += this.acquire();
        }
        return waitTime;
    }

    @Override
    public boolean tryAcquire() {
        long current = this.getCurrentCount();
        return current > 0;
    }

    private String buildRedisKey(String key){
        return redis_rate_limiter_key_prefix + "_" + key;
    }


    private void setNewLimitToRedis(){
        this.redisManager.del(this.redisKey);
        this.redisManager.incrBy(this.redisKey, 0, (int) period);
    }

    private long getCurrentCount(){
        if(!this.redisManager.exist(this.redisKey))this.setNewLimitToRedis();
        return Long.valueOf(this.redisManager.get(this.redisKey));
    }

    /**
     * 返回剩余存活时间，单位s
     * @return
     */
    private long getLimitLastTime(){
        long ttl = this.redisManager.ttl(this.redisKey);
        return ttl >= 0 ? ttl : 0;
    }

    /**
     * 保留并返回等待时间
     * @return 等待时间，单位MS
     */
    private synchronized long reserveAndGetWaitTime(){
        long currentCount = this.reserve();
        if(currentCount <= this.permits)return 0;

        long limitLastTime = this.getLimitLastTime();

        long waitTime = (long) ((currentCount - this.permits - 1) / this.rate * 1000)
                 + limitLastTime * 1000;

        return waitTime;
    }

    private synchronized long reserve(){
        if(!this.redisManager.exist(this.redisKey))this.setNewLimitToRedis();
        return this.redisManager.incr(this.redisKey);
    }

    @Override
    public void destroy() {
        this.redisManager.del(this.redisKey);
    }
}

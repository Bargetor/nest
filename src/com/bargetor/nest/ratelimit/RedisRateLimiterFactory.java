package com.bargetor.nest.ratelimit;

import com.bargetor.nest.redis.RedisManager;

/**
 * Created by bargetor on 2016/12/5.
 */
public class RedisRateLimiterFactory implements RateLimiterFactory {
    private RedisManager redisManager;

    @Override
    public RateLimiter buildRateLimiter(String key, long permits, long period) {
        return new RedisRateLimiter(key, permits, period, redisManager);
    }

    public RedisManager getRedisManager() {
        return redisManager;
    }

    public void setRedisManager(RedisManager redisManager) {
        this.redisManager = redisManager;
    }
}

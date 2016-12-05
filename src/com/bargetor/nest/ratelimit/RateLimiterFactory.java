package com.bargetor.nest.ratelimit;


/**
 * Created by bargetor on 2016/12/5.
 * 限制器创建工厂
 */
public interface RateLimiterFactory {
    public RateLimiter buildRateLimiter(String key, long permits, long period);
}

package com.bargetor.nest.ratelimit;

import com.bargetor.nest.ratelimit.annotation.RateLimit;

import java.util.concurrent.TimeUnit;

/**
 * Created by bargetor on 2016/12/4.
 */
public class GuavaRateLimiter implements RateLimiter {
    private String key;
    private com.google.common.util.concurrent.RateLimiter rateLimiter;

    public GuavaRateLimiter(String key, long permits, long period){
        this.key = key;
        this.rateLimiter = com.google.common.util.concurrent.RateLimiter.create(permits, period, TimeUnit.SECONDS);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public double acquire() {
        return this.rateLimiter.acquire();
    }

    @Override
    public boolean tryAcquire() {
        return this.rateLimiter.tryAcquire();
    }
}

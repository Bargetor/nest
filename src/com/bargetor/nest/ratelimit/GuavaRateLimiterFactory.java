package com.bargetor.nest.ratelimit;

import org.springframework.stereotype.Component;

/**
 * Created by bargetor on 2016/12/5.
 */
public class GuavaRateLimiterFactory implements RateLimiterFactory {

    @Override
    public RateLimiter buildRateLimiter(String key, long permits, long period) {
        return new GuavaRateLimiter(key, permits, period);
    }
}

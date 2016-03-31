package com.bargetor.nest.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;

/**
 * Created by Bargetor on 16/3/30.
 */
public class RedisCacheManager extends AbstractCacheManager {
    private Collection<RedisCache> caches;

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return this.caches;
    }

    public Collection<RedisCache> getCaches() {
        return caches;
    }

    public void setCaches(Collection<RedisCache> caches) {
        this.caches = caches;
    }
}

package com.bargetor.nest.cache

import com.bargetor.nest.redis.RedisManager
import org.springframework.beans.factory.InitializingBean
import org.springframework.cache.Cache

class LocalPriorityCache: Cache, InitializingBean {
    var cacheName: String = ""
    lateinit var redisManager: RedisManager
    var liveTime: Int = 0
    var redisDatabase: Int = 0

    private val redisCache = RedisCache()
    private val regexKeyCache = RegexKeyCache()

    override fun afterPropertiesSet() {
        this.redisCache.redisManager = this.redisManager
        this.redisCache.liveTime = this.liveTime
        this.redisCache.redisDatabase = this.redisDatabase
        this.redisCache.afterPropertiesSet()
    }

    override fun clear() {
        this.regexKeyCache.clear()
        this.redisCache.clear()
    }

    override fun put(key: Any?, value: Any?) {
        this.regexKeyCache.put(key, value)
        this.redisCache.put(key, value)
    }

    override fun getName(): String {
        return this.cacheName
    }

    override fun getNativeCache(): Any {
        return this.regexKeyCache.store
    }

    override fun get(key: Any?): Cache.ValueWrapper? {
        return this.regexKeyCache.get(key) ?: this.redisCache.get(key)
    }

    override fun <T : Any?> get(key: Any?, type: Class<T>?): T? {
        return this.regexKeyCache.get(key, type) ?: this.redisCache.get(key, type)
    }

    override fun evict(key: Any?) {
        this.regexKeyCache.evict(key)
        this.redisCache.evict(key)
    }

    override fun putIfAbsent(key: Any?, value: Any?): Cache.ValueWrapper? {
        this.redisCache.putIfAbsent(key, value)
        return this.regexKeyCache.putIfAbsent(key, value)
    }

}
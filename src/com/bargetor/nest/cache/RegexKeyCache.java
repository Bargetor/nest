package com.bargetor.nest.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RegexKeyCache implements Cache {
    private static final Object NULL_HOLDER = new NullHolder();

    private String name;

    final ConcurrentMap<Object, Object> store;

    private boolean allowNullValues;

    public RegexKeyCache(){
        this("default");
    }

    public RegexKeyCache(String name) {
        this(name, new ConcurrentHashMap<Object, Object>(256), true);
    }

    public RegexKeyCache(String name, boolean allowNullValues) {
        this(name, new ConcurrentHashMap<Object, Object>(256), allowNullValues);
    }

    public RegexKeyCache(String name, ConcurrentMap<Object, Object> store, boolean allowNullValues) {
        Assert.notNull(name, "Name must not be null");
        Assert.notNull(store, "Store must not be null");
        this.name = name;
        this.store = store;
        this.allowNullValues = allowNullValues;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final ConcurrentMap<Object, Object> getNativeCache() {
        return this.store;
    }

    public final boolean isAllowNullValues() {
        return this.allowNullValues;
    }

    @Override
    public ValueWrapper get(Object key) {
        Object value = this.store.get(key);
        return toWrapper(value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(Object key, Class<T> type) {
        Object value = fromStoreValue(this.store.get(key));
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        this.store.put(key, toStoreValue(value));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        Object existing = this.store.putIfAbsent(key, value);
        return toWrapper(existing);
    }

    @Override
    public void evict(Object key) {
        this.store.remove(key);
        if (key.toString().startsWith("regex:")) {
            String r = key.toString().replace("regex:", "");
            for (Object k : this.store.keySet()) {
                if (k.toString().matches(r)) {
                    this.store.remove(k);
                }
            }
        }
    }

    @Override
    public void clear() {
        this.store.clear();
    }

    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && storeValue == NULL_HOLDER) {
            return null;
        }
        return storeValue;
    }

    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NULL_HOLDER;
        }
        return userValue;
    }

    private ValueWrapper toWrapper(Object value) {
        return (value != null ? new SimpleValueWrapper(fromStoreValue(value)) : null);
    }

    @SuppressWarnings("serial")
    private static class NullHolder implements Serializable {
    }

    public void setName(String name) {
        this.name = name;
    }
}

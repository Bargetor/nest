package com.bargetor.nest.storage;

/**
 * Created by Bargetor on 16/4/3.
 */
public interface StorageManager<T> {
    public void save(T product);
    public T get();
}

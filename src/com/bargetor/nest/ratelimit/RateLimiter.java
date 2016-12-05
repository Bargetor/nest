package com.bargetor.nest.ratelimit;

/**
 * Created by bargetor on 2016/12/4.
 */
public interface RateLimiter {

    public String getKey();

    /**
     * 授权
     * 如果当前没有可用资源，则会进入休眠等待，直到被授权
     * @return 返回休眠时间
     */
    public double acquire();

    /**
     * 尝试授权
     * @return
     */
    public boolean tryAcquire();

    public default void destroy(){}
}

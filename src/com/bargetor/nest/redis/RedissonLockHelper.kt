package com.bargetor.nest.redis

import com.bargetor.nest.common.springmvc.SpringApplicationUtil
import org.redisson.api.RedissonClient
import java.util.concurrent.TimeUnit

class RedissonLockHelper{
    companion object {
//        fun lock(key: String, waitTime: Long, leaseTime: Long, onLock: () -> Unit, onLockGetFail: () -> Unit? = {}, onLockOccupied: () -> Unit? = {}): Unit {
//            lock(key, waitTime, leaseTime, onLock = {
//                onLock()
//                return@lock Any()
//            }, onLockGetFail = {
//                onLockGetFail()
//                return@lock Any()
//            }, onLockOccupied = {
//                onLockOccupied()
//                return@lock Any()
//            })
//        }

        fun <T>lock(key: String, waitTime: Long, leaseTime: Long, onLock: () -> T, onLockGetFail: () -> T? = {null}, onLockOccupied: () -> T? = {null}): T? {
            val redissonClient = SpringApplicationUtil.getBean(RedissonClient::class.java) as RedissonClient
            val lock = redissonClient.getLock(key)
            var locked = false

            try {
                locked = lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)
                if (locked) {
                    return onLock()
                } else {
                    return onLockOccupied()
                }
            } catch (e: InterruptedException) {
                //获取锁失败，直接退出
                return onLockGetFail()
            } finally {
                lock.unlock()
            }
        }
    }
}
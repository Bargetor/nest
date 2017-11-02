package com.bargetor.nest.ratelimit;

import com.bargetor.nest.ratelimit.annotation.RateLimit;
import com.bargetor.nest.ratelimit.error.RateLimiterAcquireFailedError;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by bargetor on 2016/12/4.
 */
@Aspect
@Component
public class RateLimitManager implements InitializingBean, DisposableBean {
    private final static Logger logger = Logger.getLogger(RateLimitManager.class);
    private Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();
    private Map<String, RateLimit> rateLimitMap = new HashMap<>();
    @Autowired(required = false)
    private RateLimiterFactory rateLimiterFactory;

    @Before("@annotation(com.bargetor.nest.ratelimit.annotation.RateLimit)")
    public void invokeBefore(JoinPoint joinPoint){
        MethodInvocationProceedingJoinPoint methodJoinPoint = (MethodInvocationProceedingJoinPoint) joinPoint;
        String key = this.getRateLimiterKey(methodJoinPoint);
        RateLimiter rateLimiter = this.getRateLimiter(key);

        RateLimit rateLimit = this.rateLimitMap.get(key);
        if(rateLimit == null){
            rateLimit = this.getAnnotation(methodJoinPoint, RateLimit.class);
            this.rateLimitMap.put(key, rateLimit);
        }

        if(rateLimiter == null){
            rateLimiter = this.buildRateLimiter(key, rateLimit.permits(), rateLimit.period());
            this.rateLimiterMap.put(key, rateLimiter);
        }

        if(!rateLimit.needWait()){
            if(!rateLimiter.tryAcquire()){
                throw new RateLimiterAcquireFailedError();
            }
        }

        double acquireWaitTime = rateLimiter.acquire();
        logger.info(String.format("[RateLimiter] %s acquire wait time: %s", key, acquireWaitTime));
    }

    private RateLimiter getRateLimiter(String key){
        RateLimiter rateLimiter = this.rateLimiterMap.get(key);
        return rateLimiter;
    }

    private RateLimiter buildRateLimiter(String key, long permits, long period){
        return this.rateLimiterFactory.buildRateLimiter(key, permits, period);
    }

    private String getRateLimiterKey(JoinPoint joinPoint){
        return joinPoint.getSignature().toLongString();
    }

    private <T extends Annotation> T getAnnotation(MethodInvocationProceedingJoinPoint joinPoint, Class<T> clazz) {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        return method.getAnnotation(clazz);
    }

    @Override
    public void destroy() throws Exception {
        this.rateLimiterMap.forEach((key, rateLimiter) -> {
            rateLimiter.destroy();
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(this.rateLimiterFactory == null){
            this.rateLimiterFactory = new GuavaRateLimiterFactory();
        }
    }
}

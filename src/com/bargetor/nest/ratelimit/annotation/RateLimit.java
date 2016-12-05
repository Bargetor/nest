package com.bargetor.nest.ratelimit.annotation;

import java.lang.annotation.*;

/**
 * Created by bargetor on 2016/12/4.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface RateLimit {
    String key() default "";
    //允许调用次数
    long permits();
    //时间，单位s
    long period() default 1;

    /**
     * 请求是否需要等待资源
     * @return
     */
    boolean needWait() default true;
}

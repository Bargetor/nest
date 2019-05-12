package com.bargetor.nest.bpc.annotation;

import java.lang.annotation.*;

/**
 * Created by Bargetor on 16/3/20.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BPCMethod {
    String name();
    //集群锁key，spEl 表达式，参数为方法调用参数，填写即代表锁定
    String lockKey() default "";
    boolean isTest() default false;

    /**
     * 请求验证，如果表达式为否，则抛出 auth error
     * @return
     */
    String requestValidateEL() default "";
    boolean isNonUserTokenMethod() default false;
}

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
    boolean isTest() default false;
}

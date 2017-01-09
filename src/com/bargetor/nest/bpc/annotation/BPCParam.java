package com.bargetor.nest.bpc.annotation;

import java.lang.annotation.*;

/**
 * Created by bargetor on 2017/1/10.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BPCParam {
    boolean isRequired() default false;
}

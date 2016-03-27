package com.bargetor.nest.common.check.param;

import java.lang.annotation.*;

/**
 * Created by Bargetor on 16/3/15.
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ParamCheck {
    boolean isRequired() default false;
}

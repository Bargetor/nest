package com.bargetor.nest.common.bpc.method;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

/**
 * Created by Bargetor on 16/1/28.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
@Deprecated
public @interface BPCMethod {
    String value();
}

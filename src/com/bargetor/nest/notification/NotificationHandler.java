package com.bargetor.nest.notification;

import org.springframework.context.ApplicationEvent;

import java.lang.annotation.*;

/**
 * Created by bargetor on 2017/6/4.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotificationHandler {
    Class<? extends ApplicationEvent> event();
}

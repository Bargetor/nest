package com.bargetor.nest.common.springmvc;

import org.springframework.context.ApplicationEvent;

/**
 * Created by bargetor on 2017/6/3.
 */
public class NotificationCenter {
    public static void notify(ApplicationEvent event){
        SpringApplicationUtil.applicationContext.publishEvent(event);
    }
}

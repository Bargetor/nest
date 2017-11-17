package com.bargetor.nest.common.springmvc;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

/**
 * Created by bargetor on 2017/11/11.
 */
@Component
public class WebContextListener implements ApplicationListener {
    private static final Logger logger = Logger.getLogger(WebContextListener.class);



    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ContextStartedEvent){
            logger.debug("It was Started.");
        }

        if (applicationEvent instanceof ContextClosedEvent){
            ContextClosedEvent event = (ContextClosedEvent) applicationEvent;
            if(event.getApplicationContext().getParent()==null){
                logger.debug("App context was Closed.");
                synchronized (Object.class) {
                    try {
                        Object.class.notifyAll();
                    } catch (Throwable e) {}
                }
                Runtime.getRuntime().exit(0);
            }else{
                logger.debug("Web context was Closed.");
            }
            event = null;
        }
    }
}

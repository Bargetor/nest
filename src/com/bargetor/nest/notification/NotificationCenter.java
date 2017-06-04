package com.bargetor.nest.notification;

import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.ArrayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by bargetor on 2017/6/3.
 */
@Component
public class NotificationCenter implements BeanFactoryPostProcessor, ApplicationContextAware, ApplicationListener<ApplicationEvent> {
    private static final Logger logger = Logger.getLogger(NotificationCenter.class);
    private ApplicationContext applicationContext;

    private Map<Class<? extends ApplicationEvent>, Set<SpringApplicationUtil.MethodAnnotationInfo>> eventMapping = new HashMap<>();

    public static void notify(ApplicationEvent event){
        SpringApplicationUtil.applicationContext.publishEvent(event);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        try {
            List<SpringApplicationUtil.MethodAnnotationInfo> notificationAnnotationHandlers
                    = SpringApplicationUtil.scanMethodAnnotation(this.applicationContext, NotificationHandler.class, "");

            if(ArrayUtil.isNull(notificationAnnotationHandlers))return;

            notificationAnnotationHandlers.forEach(handler -> {
                Class<? extends ApplicationEvent> eventClass = (Class<? extends ApplicationEvent>) handler.getAnnotationAttributes().get("event");
                this.putEventMapping(eventClass, handler);
            });
        } catch (IOException e) {
            logger.error("notification center init error", e);
        }
    }

    private void putEventMapping(Class<? extends ApplicationEvent> eventClass, SpringApplicationUtil.MethodAnnotationInfo annotationInfo){
        Set<SpringApplicationUtil.MethodAnnotationInfo> eventMappingSet = this.eventMapping.get(eventClass);
        if(eventMappingSet == null){
            eventMappingSet = new HashSet<>();
            this.eventMapping.put(eventClass, eventMappingSet);
        }
        eventMappingSet.add(annotationInfo);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        Set<SpringApplicationUtil.MethodAnnotationInfo> handlerAnnotationInfoSet = this.eventMapping.get(event.getClass());
        if (ArrayUtil.isNull(handlerAnnotationInfoSet))return;

        handlerAnnotationInfoSet.forEach(handlerAnnotationInfo -> {
            try {
                Class<?> handlerDeclaringClass = Class.forName(handlerAnnotationInfo.getMethodMetadata().getDeclaringClassName());
                Method handlerMethod = handlerDeclaringClass.getMethod(handlerAnnotationInfo.getMethodMetadata().getMethodName(), event.getClass());
                Object handlerDeclaring = SpringApplicationUtil.getBean(handlerDeclaringClass);
                handlerMethod.invoke(handlerDeclaring, event);
            }catch (Exception  e){
                logger.error(
                        String.format(
                                "notify event %s to %s.%s error",
                                event.getClass().getName(),
                                handlerAnnotationInfo.getMethodMetadata().getDeclaringClassName(),
                                handlerAnnotationInfo.getMethodMetadata().getMethodName()
                        ), e);
            }
        });

        logger.info("notify event: " + event.getClass().getName());
    }
}

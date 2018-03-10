package com.bargetor.nest.bpc.bean;

import com.bargetor.nest.bpc.annotation.BPCMethod;
import com.bargetor.nest.bpc.manager.BPCDispatchManager;
import com.bargetor.nest.bpc.manager.BPCServiceMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Method;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCServiceProxyBean implements InitializingBean, ApplicationContextAware {
    private static final Logger logger = Logger.getLogger(BPCServiceProxyBean.class);
    private ApplicationContext applicationContext;

    private Object target;
    private String targetClassName;
    private String url;


    @Override
    public void afterPropertiesSet() throws Exception {
        //register bpc nest
        Class<?> targetClass = applicationContext.getClassLoader().loadClass(this.targetClassName);
        for(Method method : targetClass.getMethods()){
            BPCMethod methodAnnotation = AnnotationUtils.findAnnotation(method, BPCMethod.class);
            if(methodAnnotation != null){
                BPCServiceMethod methodBean = new BPCServiceMethod(
                        methodAnnotation.name(),
                        this.target,
                        method
                );
                methodBean.setTest(methodAnnotation.isTest());
                methodBean.setLockKey(methodAnnotation.lockKey());
                methodBean.setRequestValidateEL(methodAnnotation.requestValidateEL());

                BPCDispatchManager.getInstance().registerMethod(this.url, methodBean);
            }
        }
    }



    /********************************************** getter and setter ********************************************/

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTargetClassName() {
        return targetClassName;
    }

    public void setTargetClassName(String targetClassName) {
        this.targetClassName = targetClassName;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}

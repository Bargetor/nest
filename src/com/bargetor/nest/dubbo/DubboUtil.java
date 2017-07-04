package com.bargetor.nest.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.dubbo.error.NoDubboApplicationError;
import com.bargetor.nest.dubbo.error.NoDubboProtocolError;
import com.bargetor.nest.dubbo.error.NoDubboRegistryError;

/**
 * Created by bargetor on 2017/5/30.
 */
public class DubboUtil {
    public static <T>ReferenceConfig<T> createReferenceConfig(Class<T> interfaceClass){
        return DubboUtil.createReferenceConfig(DubboUtil.getAppConfig(), DubboUtil.getRegistryConfig(), interfaceClass);
    }

    public static <T>ReferenceConfig<T> createReferenceConfig(ApplicationConfig appConfig, RegistryConfig registryConfig, Class<T> interfaceClass){
        ReferenceConfig<T> config = new ReferenceConfig<>();
        config.setApplication(appConfig);
        config.setRegistry(registryConfig);
        config.setInterface(interfaceClass);
        config.setCheck(true);
        return config;
    }

    public static ApplicationConfig getAppConfig() {
        ApplicationConfig appConfig = (ApplicationConfig) SpringApplicationUtil.getBean(ApplicationConfig.class);
        if(appConfig == null)throw new NoDubboApplicationError();
        return appConfig;
    }

    public static RegistryConfig getRegistryConfig() {
        RegistryConfig registryConfig = (RegistryConfig) SpringApplicationUtil.getBean(RegistryConfig.class);
        if(registryConfig == null)throw new NoDubboRegistryError();
        return registryConfig;
    }


    public static ProtocolConfig getProtocolConfig() {
        ProtocolConfig protocolConfig = (ProtocolConfig) SpringApplicationUtil.getBean(ProtocolConfig.class);
        if(protocolConfig == null)throw new NoDubboProtocolError();
        return protocolConfig;
    }
}

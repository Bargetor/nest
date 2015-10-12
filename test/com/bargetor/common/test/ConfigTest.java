package com.bargetor.common.test;

import com.bargetor.service.common.config.AllCommonConfigsLoader;
import com.bargetor.service.common.config.configBeans.CommonConfig;
import com.bargetor.service.common.util.JsonUtil;

public class ConfigTest {
	public static void main(String[] args){
		
		CommonConfig config = (CommonConfig) AllCommonConfigsLoader.getInstance().getConfigBean(CommonConfig.CONFIG_ID);
		
		System.out.println(JsonUtil.beanToJson(config).toString());
	}
}

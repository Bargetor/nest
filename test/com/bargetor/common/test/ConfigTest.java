package com.bargetor.common.test;

import com.bargetor.nest.common.config.AllCommonConfigsLoader;
import com.bargetor.nest.common.config.configBeans.CommonConfig;
import com.bargetor.nest.common.util.JsonUtil;

public class ConfigTest {
	public static void main(String[] args){
		
		CommonConfig config = (CommonConfig) AllCommonConfigsLoader.getInstance().getConfigBean(CommonConfig.CONFIG_ID);
		
		System.out.println(JsonUtil.beanToJson(config).toString());
	}
}

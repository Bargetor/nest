/**
 * bargetorCommon
 * com.bargetor.service.common.springmvc
 * SpringApplicationUtil.java
 * 
 * 2015年4月26日-下午12:54:23
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.springmvc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * SpringApplicationUtil
 * 
 * kin kin 2015年4月26日 下午12:54:23
 * 
 * @version 1.0.0
 *
 */
public class SpringApplicationUtil implements ApplicationContextAware{

	private static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringApplicationUtil.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}
	
	public static Object getBean(Class<?> clazz) {
		return applicationContext.getBean(clazz);
	}

}

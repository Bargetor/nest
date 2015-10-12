/**
 * bargetorCommon
 * com.bargetor.service.common.bcp.version
 * BCPApiVersionConfig.java
 * 
 * 2015年6月17日-下午10:53:52
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.bargetor.service.common.bcp.controller.BCPHandlerMethodArgumentResolver;
import com.bargetor.service.common.bcp.controller.BCPHandlerMethodReturnValueHandler;
import com.bargetor.service.common.bcp.version.BCPAPIVersionRequestMappingHandlerMapping;

/**
 *
 * BCPApiVersionConfig
 * 
 * kin
 * kin
 * 2015年6月17日 下午10:53:52
 * 
 * @version 1.0.0
 *
 */
@Configuration
//自定义错误处理
@ComponentScan(basePackageClasses = {BCPMVCConfig.class} , includeFilters = {@Filter(type = FilterType.ANNOTATION, value = {ControllerAdvice.class})})
public class BCPMVCConfig extends WebMvcConfigurationSupport{
	@Bean
	public BCPAPIVersionRequestMappingHandlerMapping requestMappingHandlerMapping() {
		//自定义request mapping handler mapping
		BCPAPIVersionRequestMappingHandlerMapping handlerMapping = new BCPAPIVersionRequestMappingHandlerMapping();
		handlerMapping.setOrder(0);
		handlerMapping.setInterceptors(getInterceptors());
		return handlerMapping;
	}
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
		RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
		//自定义参数解析
		adapter.getCustomArgumentResolvers().add(new BCPHandlerMethodArgumentResolver());
		//自定义返回值
		adapter.getCustomReturnValueHandlers().add(new BCPHandlerMethodReturnValueHandler());
		
		return adapter;
	}

}

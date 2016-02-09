/**
 * bargetorCommon
 * com.bargetor.service.common.bpc.version
 * BCPApiVersionConfig.java
 * 
 * 2015年6月17日-下午10:53:52
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bpc;

import com.bargetor.service.common.bpc.controller.BPCHandlerMethodReturnValueHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.bargetor.service.common.bpc.controller.BPCHandlerMethodArgumentResolver;

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
@ComponentScan(basePackageClasses = {BPCMVCConfig.class} , includeFilters = {@Filter(type = FilterType.ANNOTATION, value = {ControllerAdvice.class})})
public class BPCMVCConfig extends WebMvcConfigurationSupport{

	@Bean
	public BPCRequestMappingHandlerMapping requestMappingHandlerMapping() {
		//自定义request mapping handler mapping for api version
		BPCRequestMappingHandlerMapping handlerMapping = new BPCRequestMappingHandlerMapping();
		handlerMapping.setOrder(0);
		handlerMapping.setInterceptors(getInterceptors());
		return handlerMapping;
	}
	
	@Bean
	public RequestMappingHandlerAdapter requestMappingHandlerAdapter(){
		RequestMappingHandlerAdapter adapter = super.requestMappingHandlerAdapter();
		//自定义参数解析
		adapter.getCustomArgumentResolvers().add(new BPCHandlerMethodArgumentResolver());
		//自定义返回值
		adapter.getCustomReturnValueHandlers().add(new BPCHandlerMethodReturnValueHandler());
		
		return adapter;
	}

}

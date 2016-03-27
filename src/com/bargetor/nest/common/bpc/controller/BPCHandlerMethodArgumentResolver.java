/**
 * Migrant
 * com.bargetor.migrant.springmvc
 * MigrantHandlerMethodArgumentResolver.java
 * 
 * 2015年5月12日-上午12:05:39
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc.controller;

import com.bargetor.nest.common.bpc.bean.BPCBaseRequestBody;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bargetor.nest.common.bpc.BPCUtil;
import com.bargetor.nest.common.bpc.servlet.AbstractServletProcessor;



/**
 *
 * MigrantHandlerMethodArgumentResolver
 * 将json转换成object直接作为方法的参数传递
 * kin
 * kin
 * 2015年5月12日 上午12:05:39
 * 
 * @version 1.0.0
 *
 */
public class BPCHandlerMethodArgumentResolver extends AbstractServletProcessor implements HandlerMethodArgumentResolver{

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#supportsParameter(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(BPCRequestParams.class);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodArgumentResolver#resolveArgument(org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest, org.springframework.web.bind.support.WebDataBinderFactory)
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		
//		HttpInputMessage input =  this.getHttpInputMessage(webRequest);
//		MediaType contentType = input.getHeaders().getContentType();
		//非json数据不处理
//		if(!MediaType.APPLICATION_JSON.isCompatibleWith(contentType))return null;
		
		BPCBaseRequestBody requestBody = BPCUtil.buildBaseRequestBody(webRequest);
		//TODO 是否校验bcp版本？
		Object params = BPCUtil.buildRequestParams(requestBody, parameter.getParameterType());
		return params;
	}
	

}

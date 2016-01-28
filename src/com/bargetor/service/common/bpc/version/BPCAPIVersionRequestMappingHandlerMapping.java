/**
 * bargetorCommon
 * com.bargetor.service.common.bpc.version
 * BPCAPIVersionRequestMappingHandlerMapping.java
 * 
 * 2015年6月16日-下午10:37:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bpc.version;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * BPCAPIVersionRequestMappingHandlerMapping
 * 
 * kin
 * kin
 * 2015年6月16日 下午10:37:45
 * 
 * @version 1.0.0
 *
 */
public class BPCAPIVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping{

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getCustomMethodCondition(java.lang.reflect.Method)
	 */
	@Override
	protected RequestCondition<BPCAPIVersionCondition> getCustomMethodCondition(Method method) {
		BPCAPIVersion apiVersion = AnnotationUtils.findAnnotation(method, BPCAPIVersion.class);
		return createCondition(apiVersion);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getCustomTypeCondition(java.lang.Class)
	 */
	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		BPCAPIVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, BPCAPIVersion.class);
		return createCondition(apiVersion);
	}
	
	private RequestCondition<BPCAPIVersionCondition> createCondition(BPCAPIVersion apiVersion) {
        return apiVersion == null ? null : new BPCAPIVersionCondition(apiVersion.value());
    }
}

/**
 * bargetorCommon
 * com.bargetor.service.common.bcp.version
 * BCPAPIVersionRequestMappingHandlerMapping.java
 * 
 * 2015年6月16日-下午10:37:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.version;

import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * BCPAPIVersionRequestMappingHandlerMapping
 * 
 * kin
 * kin
 * 2015年6月16日 下午10:37:45
 * 
 * @version 1.0.0
 *
 */
public class BCPAPIVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping{

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getCustomMethodCondition(java.lang.reflect.Method)
	 */
	@Override
	protected RequestCondition<BCPAPIVesrsionCondition> getCustomMethodCondition(Method method) {
		BCPAPIVersion apiVersion = AnnotationUtils.findAnnotation(method, BCPAPIVersion.class);
		return createCondition(apiVersion);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getCustomTypeCondition(java.lang.Class)
	 */
	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		BCPAPIVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, BCPAPIVersion.class);
		return createCondition(apiVersion);
	}
	
	private RequestCondition<BCPAPIVesrsionCondition> createCondition(BCPAPIVersion apiVersion) {
        return apiVersion == null ? null : new BCPAPIVesrsionCondition(apiVersion.value());
    }
}

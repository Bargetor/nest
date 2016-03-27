/**
 * bargetorCommon
 * com.bargetor.nest.common.bpc.version
 * BPCRequestMappingHandlerMapping.java
 * 
 * 2015年6月16日-下午10:37:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc;

import java.lang.reflect.Method;

import com.bargetor.nest.common.bpc.method.BPCMethod;
import com.bargetor.nest.common.bpc.method.BPCMethodCondition;
import com.bargetor.nest.common.bpc.version.BPCAPIVersion;
import com.bargetor.nest.common.bpc.version.BPCAPIVersionCondition;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.condition.*;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *
 * BPCRequestMappingHandlerMapping
 * 
 * kin
 * kin
 * 2015年6月16日 下午10:37:45
 * 
 * @version 1.0.0
 *
 */
public class BPCRequestMappingHandlerMapping extends RequestMappingHandlerMapping{

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getCustomMethodCondition(java.lang.reflect.Method)
	 */
	@Override
	protected RequestCondition<?> getCustomMethodCondition(Method method) {

		BPCAPIVersion apiVersion = AnnotationUtils.findAnnotation(method, BPCAPIVersion.class);
		if(apiVersion != null){
			return new BPCAPIVersionCondition(apiVersion);
		}

		//根据spring mvc 的机制,此段是无效代码
//		BPCMethod bpcMethod = AnnotationUtils.findAnnotation(method, BPCMethod.class);
//		if(bpcMethod != null){
//			System.out.println("____________________________ method _________________________");
//			return new BPCMethodCondition(bpcMethod);
//		}

		return null;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping#getCustomTypeCondition(java.lang.Class)
	 */
	@Override
	protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerType) {
		BPCAPIVersion apiVersion = AnnotationUtils.findAnnotation(handlerType, BPCAPIVersion.class);
		if(apiVersion != null)return new BPCAPIVersionCondition(apiVersion);

		BPCMethod bpcMethod = AnnotationUtils.findAnnotation(handlerType, BPCMethod.class);
		if(bpcMethod != null)return new BPCMethodCondition(bpcMethod);

		return null;
	}


	@Override
	protected RequestMappingInfo createRequestMappingInfo(RequestMapping annotation, RequestCondition<?> customCondition) {
		String[] patterns = resolveEmbeddedValuesInPatterns(annotation.value());
		RequestMethod[] methods = annotation.method();

		//BPC的默认方法为POST
		if(methods == null || methods.length <= 0){
			methods = new RequestMethod[]{RequestMethod.POST};
		}

		return new RequestMappingInfo(
				annotation.name(),
				new PatternsRequestCondition(patterns, getUrlPathHelper(), getPathMatcher(),
						this.useSuffixPatternMatch(), this.useTrailingSlashMatch(), this.getFileExtensions()),
				new RequestMethodsRequestCondition(methods),
				new ParamsRequestCondition(annotation.params()),
				new HeadersRequestCondition(annotation.headers()),
				new ConsumesRequestCondition(annotation.consumes(), annotation.headers()),
				new ProducesRequestCondition(annotation.produces(), annotation.headers(), this.getContentNegotiationManager()),
				customCondition);
	}
}

/**
 * bargetorCommon
 * com.bargetor.service.common.bcp
 * BCPHandlerMethodReturnValueHandler.java
 * 
 * 2015年5月13日-下午10:13:14
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.controller;

import com.bargetor.service.common.bcp.BCPUtil;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bargetor.service.common.bcp.bean.BCPBaseRequestBody;
import com.bargetor.service.common.bcp.bean.BCPBaseResponseBody;
import com.bargetor.service.common.bcp.bean.BCPResponseError;
import com.bargetor.service.common.bcp.bean.BCPResponseResult;
import com.bargetor.service.common.bcp.bean.BCPResponseResultAndError;
import com.bargetor.service.common.bcp.servlet.AbstractServletProcessor;

/**
 *
 * BCPHandlerMethodReturnValueHandler
 * spring mvc controller 的BCP返回值处理
 * kin
 * kin
 * 2015年5月13日 下午10:13:14
 * 
 * @version 1.0.0
 *
 */
public class BCPHandlerMethodReturnValueHandler extends AbstractServletProcessor implements HandlerMethodReturnValueHandler{

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodReturnValueHandler#supportsReturnType(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return BCPResponseResult.class.isAssignableFrom(returnType.getParameterType()) 
				|| BCPResponseError.class.isAssignableFrom(returnType.getParameterType())
				|| BCPResponseResultAndError.class.isAssignableFrom(returnType.getParameterType());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodReturnValueHandler#handleReturnValue(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest)
	 */
	@Override
	public void handleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		
		mavContainer.setRequestHandled(true);
		
		BCPBaseRequestBody requestBody = BCPUtil.buildBaseRequestBody(webRequest);
		BCPBaseResponseBody responseBody = new BCPBaseResponseBody();
		responseBody.setId(requestBody.getId());
		if(returnType == null){
			this.writeResponse(webRequest, responseBody);
			return;
		}
		if(BCPResponseResult.class.isAssignableFrom(returnType.getParameterType())){
			responseBody.setResult((BCPResponseResult) returnValue);
		}else if(BCPResponseError.class.isAssignableFrom(returnType.getParameterType())){
			responseBody.setError((BCPResponseError) returnValue);
		}else if(BCPResponseResultAndError.class.isAssignableFrom(returnType.getParameterType())){
			BCPResponseResultAndError rae = (BCPResponseResultAndError) returnValue;
			responseBody.setError(rae.getError());
			responseBody.setResult(rae.getResult());
		}
		
		this.writeResponse(webRequest, responseBody);
	}
}

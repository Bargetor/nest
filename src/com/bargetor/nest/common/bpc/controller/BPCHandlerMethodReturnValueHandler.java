/**
 * bargetorCommon
 * com.bargetor.nest.common.bpc
 * BPCHandlerMethodReturnValueHandler.java
 * 
 * 2015年5月13日-下午10:13:14
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc.controller;

import com.bargetor.nest.common.bpc.BPCUtil;
import com.bargetor.nest.common.bpc.bean.*;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.bargetor.nest.common.bpc.bean.BPCBaseRequestBody;
import com.bargetor.nest.common.bpc.servlet.AbstractServletProcessor;

/**
 *
 * BPCHandlerMethodReturnValueHandler
 * spring mvc controller 的BCP返回值处理
 * kin
 * kin
 * 2015年5月13日 下午10:13:14
 * 
 * @version 1.0.0
 *
 */
public class BPCHandlerMethodReturnValueHandler extends AbstractServletProcessor implements HandlerMethodReturnValueHandler{

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodReturnValueHandler#supportsReturnType(org.springframework.core.MethodParameter)
	 */
	@Override
	public boolean supportsReturnType(MethodParameter returnType) {
		return BPCResponseResult.class.isAssignableFrom(returnType.getParameterType())
				|| BPCResponseError.class.isAssignableFrom(returnType.getParameterType())
				|| BPCResponseResultAndError.class.isAssignableFrom(returnType.getParameterType());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.method.support.HandlerMethodReturnValueHandler#handleReturnValue(java.lang.Object, org.springframework.core.MethodParameter, org.springframework.web.method.support.ModelAndViewContainer, org.springframework.web.context.request.NativeWebRequest)
	 */
	@Override
	public void handleReturnValue(Object returnValue,
			MethodParameter returnType, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest) throws Exception {
		
		mavContainer.setRequestHandled(true);
		
		BPCBaseRequestBody requestBody = BPCUtil.findBCPBaseRequestBody(webRequest);
		BPCBaseResponseBody responseBody = new BPCBaseResponseBody();
		responseBody.setId(requestBody.getId());
		if(returnType == null){
			this.writeResponse(webRequest, responseBody);
			return;
		}
		if(BPCResponseResult.class.isAssignableFrom(returnType.getParameterType())){
			responseBody.setResult((BPCResponseResult) returnValue);
		}else if(BPCResponseError.class.isAssignableFrom(returnType.getParameterType())){
			responseBody.setError((BPCResponseError) returnValue);
		}else if(BPCResponseResultAndError.class.isAssignableFrom(returnType.getParameterType())){
			BPCResponseResultAndError rae = (BPCResponseResultAndError) returnValue;
			responseBody.setError(rae.getError());
			responseBody.setResult(rae.getResult());
		}
		
		this.writeResponse(webRequest, responseBody);
	}
}

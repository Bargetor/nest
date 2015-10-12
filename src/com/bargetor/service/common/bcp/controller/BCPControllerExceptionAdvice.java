/**
 * bargetorCommon
 * com.bargetor.service.common.bcp
 * BCPControllerExceptionAdvice.java
 * 定义在spring mvc controller 中直接返回 bcp error 时，向客户端返回JSON
 * 2015年6月8日-下午11:05:34
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

import com.bargetor.service.common.bcp.bean.BCPBaseRequestBody;
import com.bargetor.service.common.bcp.bean.BCPBaseResponseBody;
import com.bargetor.service.common.bcp.bean.BCPResponseError;
import com.bargetor.service.common.bcp.servlet.AbstractServletProcessor;

/**
 *
 * BCPControllerExceptionAdvice
 * 
 * kin
 * kin
 * 2015年6月8日 下午11:05:34
 * 
 * @version 1.0.0
 *
 */
@ControllerAdvice
public class BCPControllerExceptionAdvice extends AbstractServletProcessor {

	@ExceptionHandler(BCPResponseError.class)
	public void globalExceptionProcess(NativeWebRequest webRequest, BCPResponseError e){
		
		BCPBaseRequestBody requestBody = this.buildBaseRequestBody(webRequest);
		BCPBaseResponseBody responseBody = new BCPBaseResponseBody();
		
		responseBody.setId(requestBody.getId());
		
		responseBody.setError(e);

		this.writeResponse(webRequest, responseBody);
		
	}
	
}

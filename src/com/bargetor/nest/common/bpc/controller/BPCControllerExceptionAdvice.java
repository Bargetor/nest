/**
 * bargetorCommon
 * com.bargetor.nest.common.bpc
 * BCPControllerExceptionAdvice.java
 * 定义在spring mvc controller 中直接返回 bpc error 时，向客户端返回JSON
 * 2015年6月8日-下午11:05:34
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc.controller;

import com.bargetor.nest.common.bpc.BPCUtil;
import com.bargetor.nest.common.bpc.bean.BPCBaseRequestBody;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.NativeWebRequest;

import com.bargetor.nest.common.bpc.bean.BPCBaseResponseBody;
import com.bargetor.nest.common.bpc.bean.BPCResponseError;
import com.bargetor.nest.common.bpc.servlet.AbstractServletProcessor;

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
public class BPCControllerExceptionAdvice extends AbstractServletProcessor {
	private final static Logger logger = Logger.getLogger(BPCControllerExceptionAdvice.class);

	@ExceptionHandler(BPCResponseError.class)
	public void globalExceptionProcess(NativeWebRequest webRequest, BPCResponseError e){

		BPCBaseRequestBody requestBody = BPCUtil.findBCPBaseRequestBody(webRequest);
		BPCBaseResponseBody responseBody = new BPCBaseResponseBody();
		responseBody.setId(requestBody.getId());
		
		responseBody.setError(e);

		this.writeResponse(webRequest, responseBody);

		logger.error(e.getMessage(), e);
	}
	
}

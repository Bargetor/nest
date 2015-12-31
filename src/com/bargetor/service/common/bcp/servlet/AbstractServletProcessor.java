/**
 * bargetorCommon
 * com.bargetor.service.common.bcp
 * AbstractServletProcessor.java
 * 
 * 2015年5月13日-下午11:42:56
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.servlet;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.context.request.NativeWebRequest;

import com.bargetor.service.common.bcp.BCPUtil;
import com.bargetor.service.common.bcp.bean.BCPBaseRequestBody;
import com.bargetor.service.common.bcp.bean.BCPBaseResponseBody;

/**
 *
 * AbstractServletProcessor
 * 定义基于spring mvc 的基础servlet input output 处理
 * kin
 * kin
 * 2015年5月13日 下午11:42:56
 * 
 * @version 1.0.0
 *
 */
public abstract class AbstractServletProcessor {

	protected static final Logger logger = Logger.getLogger(AbstractServletProcessor.class);
	
	/**
	 * writeResponse(写入 response body)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param webRequest
	 * @param responseBody
	 * void
	 * @exception
	 * @since  1.0.0
	*/
	protected void writeResponse(NativeWebRequest webRequest, BCPBaseResponseBody responseBody){
		HttpServletResponse servletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
		BCPUtil.writeResponse(servletResponse, responseBody);
	}
	
	/**
	 * getOutputMessage(获取output)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param webRequest
	 * @return
	 *ServletServerHttpResponse
	 * @exception
	 * @since  1.0.0
	*/
	protected ServletServerHttpResponse getOutputMessage(NativeWebRequest webRequest){
		HttpServletResponse servletResponse = webRequest.getNativeResponse(HttpServletResponse.class);
		ServletServerHttpResponse outputMessage = new ServletServerHttpResponse(servletResponse);
		return outputMessage;
	}

	
	/**
	 * getHttpInputMessage(获取到request input message)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param webRequest
	 * @return
	 * @throws IOException
	 *HttpInputMessage
	 * @exception
	 * @since  1.0.0
	*/
	protected HttpInputMessage getHttpInputMessage(NativeWebRequest webRequest) throws IOException{
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);
		return inputMessage;
//		InputStream inputStream = inputMessage.getBody();
//		if (inputStream == null) {
//			return null;
//		}else {
//			final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
//			int b = pushbackInputStream.read();
//			if (b == -1) {
//				return null;
//			}
//			else {
//				pushbackInputStream.unread(b);
//			}
//			inputMessage = new ServletServerHttpRequest(servletRequest) {
//				@Override
//				public InputStream getBody() {
//					// Form POST should not get here
//					return pushbackInputStream;
//				}
//			};
//			return inputMessage;
//		}
	}
	

}

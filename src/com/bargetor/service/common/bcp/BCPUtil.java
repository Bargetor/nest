/**
 * bargetorCommon
 * com.bargetor.service.common.bcp
 * BCPUtil.java
 * 
 * 2015年5月21日-下午4:31:21
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.util.StreamUtils;

import com.bargetor.service.common.bcp.bean.BCPBaseRequestBody;
import com.bargetor.service.common.bcp.bean.BCPBaseResponseBody;
import com.bargetor.service.common.util.JsonUtil;
import com.bargetor.service.common.util.StringUtil;

/**
 *
 * BCPUtil
 * 
 * kin
 * kin
 * 2015年5月21日 下午4:31:21
 * 
 * @version 1.0.0
 *
 */
public class BCPUtil {
	protected static final Logger logger = Logger.getLogger(BCPUtil.class);
	
	/**
	 * buildRequestParams(创建请求参数bean)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param requestBody
	 * @param paramsClass
	 * @return
	 * T
	 * @exception
	 * @since  1.0.0
	*/
	public static <T>T buildRequestParams(BCPBaseRequestBody requestBody, Class<T> paramsClass){
		if(requestBody == null || StringUtil.isNullStr(requestBody.getParams()))return null;
		return JsonUtil.jsonStrToBean(requestBody.getParams(), paramsClass);
	}
	
	/**
	 * buildBaseRequestBody(获取request body)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param request
	 * @return
	 * BCPBaseRequestBody
	 * @exception
	 * @since  1.0.0
	*/
	public static BCPBaseRequestBody buildBaseRequestBody(ServletRequest request){
		InputStream input;
		BCPBaseRequestBody requestBody = null;
		try {
			input = request.getInputStream();
			JSONObject requestBodyJson = getRequestBodyJson(input, request.getCharacterEncoding());
			return JsonUtil.jsonToBean(BCPBaseRequestBody.class, requestBodyJson);
		} catch (IOException e) {
			logger.error("miss request body", e);
		}
		return requestBody;
	}
	
	/**
	 * getRequestBody(获取request body string)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param request
	 * @return
	 * String
	 * @exception
	 * @since  1.0.0
	*/
	public static String getRequestBodyString(ServletRequest request){
		InputStream input;
		try {
			input = request.getInputStream();
			String requestBodyString = getRequestBodyString(input, request.getCharacterEncoding());
			return requestBodyString;
		} catch (IOException e) {
			logger.error("miss request body", e);
			return null;
		}
	}
	
	/**
	 * writeResponse(写入)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param response
	 * @param responseBody
	 * void
	 * @exception
	 * @since  1.0.0
	*/
	public static void writeResponse(ServletResponse response, BCPBaseResponseBody responseBody){
		writeResponse(response, responseBody.toJsonString());
	}
	
	/**
	 * writeResponse(写入)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param response
	 * @param data
	 * void
	 * @exception
	 * @since  1.0.0
	*/
	private static void writeResponse(ServletResponse response, String data){
		try {
			response.setContentType("application/json;charset=utf-8");
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(data.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			logger.error("output erroe", e);
		}
	}
	
	/**
	 * getRequestBodyString(获取request body string)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param input
	 * @param charsetName
	 * @return
	 * @throws IOException
	 * String
	 * @exception
	 * @since  1.0.0
	*/
	private static String getRequestBodyString(InputStream input, String charsetName) throws IOException{
		Charset charset = Charset.forName(charsetName);
		return getRequestBodyString(input, charset);
	}
	
	/**
	 * getRequestBodyString(获取request body string)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param input
	 * @param charset
	 * @return
	 * @throws IOException
	 * String
	 * @exception
	 * @since  1.0.0
	*/
	private static String getRequestBodyString(InputStream input, Charset charset) throws IOException{
		String body = StreamUtils.copyToString(input, charset);
		return body;
	}
	
	/**
	 * getRequestBodyJson(获取requestbody的json形态)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param input
	 * @param charsetName
	 * @return
	 * @throws IOException
	 * JSONObject
	 * @exception
	 * @since  1.0.0
	*/
	private static JSONObject getRequestBodyJson(InputStream input, String charsetName) throws IOException{
		Charset charset = Charset.forName(charsetName);
		return getRequestBodyJson(input, charset);
	}
	
	/**
	 * getRequestBodyJson(获取requestbody的json形态)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param input
	 * @param charset
	 * @return
	 * @throws IOException
	 * JSONObject
	 * @exception
	 * @since  1.0.0
	*/
	private static JSONObject getRequestBodyJson(InputStream input, Charset charset) throws IOException{
		String body = getRequestBodyString(input, charset);
		if(StringUtil.isNullStr(body))return null;
		return new JSONObject(body);
	}

}

/**
 * bargetorCommon
 * com.bargetor.nest.common.bpc
 * BPCUtil.java
 * 
 * 2015年5月21日-下午4:31:21
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.bargetor.nest.common.bpc.servlet.BPCServletRequest;
import org.apache.log4j.Logger;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.json.JSONObject;
import org.springframework.util.StreamUtils;

import com.bargetor.nest.common.bpc.bean.BPCBaseRequestBody;
import com.bargetor.nest.common.bpc.bean.BPCBaseResponseBody;
import com.bargetor.nest.common.util.JsonUtil;
import com.bargetor.nest.common.util.StringUtil;
import org.springframework.web.context.request.NativeWebRequest;

/**
 *
 * BPCUtil
 * 
 * kin
 * kin
 * 2015年5月21日 下午4:31:21
 * 
 * @version 1.0.0
 *
 */
public class BPCUtil {
	protected static final Logger logger = Logger.getLogger(BPCUtil.class);
	
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
	public static <T>T buildRequestParams(BPCBaseRequestBody requestBody, Class<T> paramsClass){
		if(requestBody == null || StringUtil.isNullStr(requestBody.getParams()))return null;
		return JsonUtil.jsonStrToBean(requestBody.getParams(), paramsClass);
	}
	
	/**
	 * buildBaseRequestBody(获取request body)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param request
	 * @return
	 * BPCBaseRequestBody
	 * @exception
	 * @since  1.0.0
	*/
	public static BPCBaseRequestBody buildBaseRequestBody(ServletRequest request){
		InputStream input;
		BPCBaseRequestBody requestBody = null;
		try {
			input = request.getInputStream();
			JSONObject requestBodyJson = getRequestBodyJson(input, request.getCharacterEncoding());
			requestBody = JsonUtil.jsonToBean(BPCBaseRequestBody.class, requestBodyJson);

			if(requestBody == null){
				requestBody = new BPCBaseRequestBody();
			}

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
	public static void writeResponse(ServletResponse response, BPCBaseResponseBody responseBody){
		writeResponse(response, responseBody.toJsonString());
	}

	public static BPCBaseRequestBody findBCPBaseRequestBody(NativeWebRequest webRequest){
		BPCServletRequest request = findBCPServletRequest(webRequest);
		if(request == null){
			return buildBaseRequestBody(webRequest);
		}
		return request.getRequestBody();
	}

	/**
	 * buildBaseRequestBody(获取request body)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param webRequest
	 * @return
	 *BPCBaseRequestBody
	 * @exception
	 * @since  1.0.0
	 */
	public static BPCBaseRequestBody buildBaseRequestBody(NativeWebRequest webRequest){
		HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
		BPCBaseRequestBody requestBody = BPCUtil.buildBaseRequestBody(servletRequest);
		return requestBody;
	}

	/**
	 * 查找bcp servlet request
	 * @param webRequest
	 * @return
     */
	public static BPCServletRequest findBCPServletRequest(NativeWebRequest webRequest){
		if(webRequest == null)return null;
		ServletRequest nativeRequest = (ServletRequest) webRequest.getNativeRequest();

		if(nativeRequest == null)return null;

		if(isBCPServletRequest(nativeRequest)){
			return (BPCServletRequest) nativeRequest;
		}

		//对shiro的支持
		if(ShiroHttpServletRequest.class.isAssignableFrom(nativeRequest.getClass())){
			ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) webRequest.getNativeRequest();
			if(isBCPServletRequest(shiroRequest.getRequest())){
				return (BPCServletRequest) shiroRequest.getRequest();
			}
		}

		return null;
	}

	/**
	 * 判断是否为bcp servlet request
	 * @param request
	 * @return
     */
	private static boolean isBCPServletRequest(ServletRequest request){
		if(request == null)return false;
		return BPCServletRequest.class.isAssignableFrom(request.getClass());
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
	public static void writeResponse(ServletResponse response, String data){
		try {
			response.setContentType("application/json;charset=utf-8");
//			response.setCharacterEncoding("utf-8");
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
		if(StringUtil.isNullStr(charsetName))charsetName = "UTF-8";
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

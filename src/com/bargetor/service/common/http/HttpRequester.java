package com.bargetor.service.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bargetor.service.common.util.StringUtil;
 

/**
 * <p>description: HTTP请求对象</p>
 * <p>Date: 2013-9-23 上午09:34:08</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class HttpRequester {
	private static final Logger logger = Logger.getLogger(HttpRequester.class);
	
	private static CloseableHttpClient httpClient = HttpClients.createDefault();
	
	/**
	 * defaultContentEncoding:默认内容编码
	 */
	private String defaultContentEncoding;
 
	public HttpRequester() {
		this.defaultContentEncoding = Charset.defaultCharset().name();
	}
 
	/**
	 *<p>Title: sendGet</p>
	 *<p>Description:发送GET请求</p>
	 * @param @param urlString URL地址
	 * @param @return 响应对象
	 * @param @throws IOException 设定文件
	 * @return  HttpRespons 返回类型
	 * @throws
	*/
	public HttpResponse sendGet(String urlString) throws IOException {
		return this.sendGet(urlString, null, null);
	}
 
	/**
	 *<p>Title: sendGet</p>
	 *<p>Description:发送GET请求</p>
	 * @param @param urlString URL地址
	 * @param @param params 参数集合
	 * @param @return 响应对象
	 * @param @throws IOException 设定文件
	 * @return  HttpRespons 返回类型
	 * @throws
	*/
	public HttpResponse sendGet(String urlString, Map<String, String> params)
			throws IOException {
		return this.sendGet(urlString, params, null);
	}

	/**
	 *<p>Title: sendGet</p>
	 *<p>Description:发送GET请求</p>
	 * @param @param urlString URL地址
	 * @param @param params 参数集合
	 * @param @param propertys 请求属性
	 * @param @return 响应对象
	 * @param @throws IOException 设定文件
	 * @return  HttpRespons 返回类型
	 * @throws
	*/
	public HttpResponse sendGet(String urlString, Map<String, String> params,
			Map<String, String> propertys) throws IOException {
		StringBuffer paramBuffer = new StringBuffer();
		String newUrl = urlString;
 
		//拼接GET参数
		if (params != null) {
			int i = 0;
			for (String key : params.keySet()) {
				if (i == 0)
					paramBuffer.append("?");
				else
					paramBuffer.append("&");
				paramBuffer.append(key).append("=").append(params.get(key));
				i++;
			}
			newUrl += paramBuffer;
		}
		
		HttpGet httpget = new HttpGet(newUrl);
        
        
        logger.info("executing request " + httpget.getURI());  
        // 执行get请求.    
        CloseableHttpResponse response = httpClient.execute(httpget); 
		
		return this.makeContent(urlString, httpget, response);
	}
 
	/**
	 *<p>Title: sendPost</p>
	 *<p>Description:发送POST请求</p>
	 * @param @param urlString URL地址
	 * @param @return 响应对象
	 * @param @throws IOException 设定文件
	 * @return  HttpRespons 返回类型
	 * @throws
	*/
	public HttpResponse sendPost(String urlString) throws IOException {
		return this.sendPost(urlString,  (Map<String, String>) null, null);
	}
 
	/**
	 *<p>Title: sendPost</p>
	 *<p>Description:发送POST请求</p>
	 * @param @param urlString URL地址
	 * @param @param params  参数集合
	 * @param @return 响应对象
	 * @param @throws IOException 设定文件
	 * @return  HttpRespons 返回类型
	 * @throws
	*/
	public HttpResponse sendPost(String urlString, Map<String, String> params)
			throws IOException {
		return this.sendPost(urlString, params, null);
	}
 
	/**
	 *<p>Title: sendPost</p>
	 *<p>Description:发送POST请求</p>
	 * @param @param urlString URL地址
	 * @param @param params  参数集合
	 * @param @param propertys 请求属性
	 * @return HttpRespons 返回类型
	 * @throws
	*/
	public HttpResponse sendPost(String urlString, Map<String, String> parameters,
			Map<String, String> propertys) throws IOException {
		StringBuffer paramBuffer = new StringBuffer();
		//拼接request body
		if (parameters != null) {
			for (Entry<String, String> param : parameters.entrySet()) {
				if(paramBuffer.length() > 0)paramBuffer.append("&");
				paramBuffer.append(param.getKey()).append("=").append(param.getValue());
			}
		}
		
		return this.sendPost(urlString, paramBuffer.toString(), propertys);
	}
	
	/**
	 * sendPost(发送POST请求)
	 * @param urlString
	 * @param requestBody 请求体
	 * @param properties
	 * @return
	 * HttpResponse
	 * @exception
	 * @since  1.0.0
	*/
	public HttpResponse sendPost(String urlString, String requestBody,
			Map<String, String> properties){
		
		logger.info("发起HTTP POST 请求：" + urlString);
		HttpPost httpPost = new HttpPost(urlString);
		
		if(properties != null){
			for (Entry<String, String> propetry : properties.entrySet()) {
				httpPost.setHeader(propetry.getKey(), propetry.getValue());
			}
		}
		
		try {
			if(StringUtil.isNotNullStr(requestBody)){
				HttpEntity entity;
					entity = new ByteArrayEntity(requestBody.getBytes(this.defaultContentEncoding));
				httpPost.setEntity(entity);
			}
			CloseableHttpResponse response = httpClient.execute(httpPost);
			return this.makeContent(urlString, httpPost, response);
			
		} catch (IOException e) {
			logger.error("http post error", e);
			return null;
		}
		
	}
 
	/**
	 *<p>Title: send</p>
	 *<p>Description:发送HTTP请求</p>
	 * @param @param urlString
	 * @param @param method
	 * @param @param parameters
	 * @param @param propertys
	 * @param @return
	 * @param @throws IOException 设定文件
	 * @return HttpRespons 响映对象
	 * @throws
	*/
	public HttpResponse send(String urlString, String method,
			Map<String, String> parameters, Map<String, String> properties)
			throws IOException {
		StringBuffer paramBuffer = new StringBuffer();
		String newUrl = urlString;
 
		//拼接GET参数
		if (method.equalsIgnoreCase("GET") && parameters != null) {
			int i = 0;
			for (String key : parameters.keySet()) {
				if (i == 0)
					paramBuffer.append("?");
				else
					paramBuffer.append("&");
				paramBuffer.append(key).append("=").append(parameters.get(key));
				i++;
			}
			newUrl += paramBuffer;
		}
		
 
		//拼接request body
		if (method.equalsIgnoreCase("POST") && parameters != null) {
			for (Entry<String, String> param : parameters.entrySet()) {
				if(paramBuffer.length() > 0)paramBuffer.append("&");
				paramBuffer.append(param.getKey()).append("=").append(param.getValue());
			}
		}
 
		return this.send(newUrl, method, paramBuffer.toString(), properties);
	}
	
	/**
	 * send(发送HTTP请求)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param urlString
	 * @param method
	 * @param requestBody
	 * @param properties
	 * @return
	 * @throws IOException
	 * HttpRespons
	 * @exception
	 * @since  1.0.0
	*/
	public HttpResponse send(String urlString, String method,
			String requestBody, Map<String, String> properties) throws IOException{
//		HttpURLConnection urlConnection = null;
// 
//		
//		logger.info("发起HTTP " + method + " 请求：" + urlString);
//		URL url = new URL(urlString);
//		
//		//忽略掉ssl证书
//		if("https".equals(url.getProtocol())){
//			try {
//				SSLUtil.ignoreSsl();
//			} catch (Exception e) {
//				logger.error("ssl ignore error", e);
//			}
//		}
//
//		
//		urlConnection = (HttpURLConnection) url.openConnection();
// 
//		urlConnection.setRequestMethod(method);
//		urlConnection.setDoOutput(true);
//		urlConnection.setDoInput(true);
//		urlConnection.setUseCaches(false);
// 
//		if (properties != null)
//			for (Entry<String, String> propetry : properties.entrySet()) {
//				urlConnection.addRequestProperty(propetry.getKey(), propetry.getValue());
//			}
// 
//		if(StringUtil.isNotNullStr(requestBody)){
//			urlConnection.addRequestProperty("Content-Length", String.valueOf(requestBody.length()));
//			
//			urlConnection.getOutputStream().write(requestBody.getBytes());
//			urlConnection.getOutputStream().flush();
//			urlConnection.getOutputStream().close();			
//		}
//		logger.info("请求参数：" + requestBody);
//		
// 
//		return this.makeContent(urlString, urlConnection);
		
		logger.info("发起HTTP " + method + " 请求：" + urlString);
		HttpRequestBase request;
		if("GET".equals(method)){
			request = new HttpGet(urlString);
		}else if("POST".equals(method)){
			request = new HttpPost(urlString);
		}else{
			return null;
		}
		
		if(properties != null){
			for (Entry<String, String> propetry : properties.entrySet()) {
				request.setHeader(propetry.getKey(), propetry.getValue());
			}
		}
		
		if(StringUtil.isNotNullStr(requestBody) && "POST".equals(method)){
			HttpEntity entity = new ByteArrayEntity(requestBody.getBytes(this.defaultContentEncoding));
			HttpPost httpPost = (HttpPost) request;
			httpPost.setEntity(entity);
		}
		
		CloseableHttpResponse response = httpClient.execute(request);
		return this.makeContent(urlString, request, response);
	}
	
	private HttpResponse makeContent(String urlString, HttpRequestBase request, CloseableHttpResponse response) throws IOException {
		HttpResponse httpResponser = new HttpResponse();
		
		httpResponser.urlString = urlString;
		 
		httpResponser.defaultPort = request.getURI().getPort();
		httpResponser.host = request.getURI().getHost();
		httpResponser.path = request.getURI().getPath();
		httpResponser.port = request.getURI().getPort();
//		httpResponser.protocol
		httpResponser.query = request.getURI().getQuery();
//		httpResponser.ref = request.getURI()
		httpResponser.userInfo = request.getURI().getUserInfo();
		
		HttpEntity entity = response.getEntity();

		httpResponser.content = EntityUtils.toString(entity, this.defaultContentEncoding);
		
		if(response.getEntity().getContentEncoding() != null){
			httpResponser.contentEncoding = response.getEntity().getContentEncoding().getValue();			
		}
//		httpResponser.code = response.getEntity().get
//		httpResponser.message = response.getEntity()
		if(response.getEntity().getContentType() != null){
			httpResponser.contentType = response.getEntity().getContentType().getValue();			
		}
		httpResponser.method = request.getMethod();
//		httpResponser.connectTimeout = request.getURI()
//		httpResponser.readTimeout = urlConnection.getReadTimeout();
		response.close();
		
		return httpResponser;
	}

	/**
	 *<p>Title: makeContent</p>
	 *<p>Description:得到响应对象</p>
	 * @param @param urlString
	 * @param @param urlConnection
	 * @param @return
	 * @param @throws IOException 设定文件
	 * @return HttpRespons 返回类型
	 * @throws
	*/
	@SuppressWarnings("unused")
	private HttpResponse makeContent(String urlString,
			HttpURLConnection urlConnection) throws IOException {
		HttpResponse httpResponser = new HttpResponse();
		try {
			if("https".equals(urlConnection.getURL().getProtocol())){
				SSLUtil.defaultSsl();
			}
			
			
			String ecod = urlConnection.getContentEncoding();
			if (ecod == null)ecod = this.defaultContentEncoding;
			InputStream in = urlConnection.getInputStream();
			
			//特殊处理gzip数据
			if("gzip".equals(ecod)){
				ecod = this.defaultContentEncoding;
				in = new GZIPInputStream(in);
			}
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in,ecod));
			httpResponser.contentCollection = new Vector<String>();
			StringBuffer temp = new StringBuffer();
			String line = bufferedReader.readLine();
			while (line != null) {
				httpResponser.contentCollection.add(line);
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
			bufferedReader.close();
 
			httpResponser.urlString = urlString;
 
			httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
			httpResponser.file = urlConnection.getURL().getFile();
			httpResponser.host = urlConnection.getURL().getHost();
			httpResponser.path = urlConnection.getURL().getPath();
			httpResponser.port = urlConnection.getURL().getPort();
			httpResponser.protocol = urlConnection.getURL().getProtocol();
			httpResponser.query = urlConnection.getURL().getQuery();
			httpResponser.ref = urlConnection.getURL().getRef();
			httpResponser.userInfo = urlConnection.getURL().getUserInfo();
 
			httpResponser.content = temp.toString();
			httpResponser.contentEncoding = ecod;
			httpResponser.code = urlConnection.getResponseCode();
			httpResponser.message = urlConnection.getResponseMessage();
			httpResponser.contentType = urlConnection.getContentType();
			httpResponser.method = urlConnection.getRequestMethod();
			httpResponser.connectTimeout = urlConnection.getConnectTimeout();
			httpResponser.readTimeout = urlConnection.getReadTimeout();
 
			return httpResponser;
		} catch (IOException e) {
			logger.info("HTTP请求错误", e);
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}
 
	/**
	 *<p>Title: getDefaultContentEncoding</p>
	 *<p>Description:默认的响应字符集</p>
	 * @param @return 设定文件
	 * @return String 返回类型
	 * @throws
	*/
	public String getDefaultContentEncoding() {
		return this.defaultContentEncoding;
	}

	/**
	 *<p>Title: setDefaultContentEncoding</p>
	 *<p>Description:设置默认的响应字符集</p>
	 * @param @param defaultContentEncoding 设定文件
	 * @return void 返回类型
	 * @throws
	*/
	public void setDefaultContentEncoding(String defaultContentEncoding) {
		this.defaultContentEncoding = defaultContentEncoding;
	}
}


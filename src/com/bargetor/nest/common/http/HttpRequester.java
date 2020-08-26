package com.bargetor.nest.common.http;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

import com.bargetor.nest.common.util.MapUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.bargetor.nest.common.util.StringUtil;

import javax.net.ssl.SSLHandshakeException;


/**
 * <p>description: HTTP请求对象</p>
 * <p>Date: 2013-9-23 上午09:34:08</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class HttpRequester {
	private static final Logger logger = Logger.getLogger(HttpRequester.class);
	
	private CloseableHttpClient httpClient;
	private RequestConfig requestConfig = HttpRequester.buildDefaultRequestConfig();
	
	/**
	 * defaultContentEncoding:默认内容编码
	 */
	private String defaultContentEncoding;
 
	public HttpRequester() {
		this(null, null);
	}

	/**
	 *
	 * @param keyStore 信任证书
	 * @param keyStorePassword
	 */
	public HttpRequester(File keyStore, String keyStorePassword){
		this.defaultContentEncoding = Charset.defaultCharset().name();
		if(keyStore == null){
			this.httpClient = HttpClients.createDefault();
		}else {
			this.httpClient = SSLUtil.buildClientByKeyStore(keyStore, keyStorePassword);
		}
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
		return this.send(urlString, "GET", params, propertys);
	}

	public String urlEncodeParams(Map<String, String> params){
		if(params == null || params.size() <= 0)return null;
		Map<String, String> encodeParams = new HashMap<>();
		params.forEach((key, value) -> {
			try {
				encodeParams.put(key, URLEncoder.encode(value, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				encodeParams.put(key, value.replace(" ", ""));
			}
		});
		return MapUtil.concatParams(encodeParams);
	}

	public String concatParams(String url, Map<String, String> params){
		String newUrl = url;
		String urlEncodeParams = this.urlEncodeParams(params);
		if(StringUtil.isNotNullStr(urlEncodeParams)) newUrl += "?" + urlEncodeParams;

		return newUrl;
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
		return this.sendPost(urlString, this.urlEncodeParams(parameters), propertys);
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
			Map<String, String> properties) throws IOException {

		return this.send(urlString, "POST", requestBody, properties);
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
		String newUrl = urlString;

		//拼接GET参数
		if (method.equalsIgnoreCase("GET") && parameters != null) {
			newUrl = this.concatParams(urlString, parameters);
		}
 
		//拼接request body
		String requestBody = null;
		if (method.equalsIgnoreCase("POST") && parameters != null) {
			requestBody = this.urlEncodeParams(parameters);
		}
 
		return this.send(newUrl, method, requestBody, properties);
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
		HttpRequestBase request;
		if("GET".equals(method)){
			request = new HttpGet(urlString);
		}else if("POST".equals(method)){
			request = new HttpPost(urlString);

			if(StringUtil.isNotNullStr(requestBody)){
				HttpEntity entity = new ByteArrayEntity(requestBody.getBytes(this.defaultContentEncoding));
				HttpPost httpPost = (HttpPost) request;
				httpPost.setEntity(entity);
			}
		}else{
			return null;
		}
		if(MapUtil.isMapNotNull(properties)){
			for (Entry<String, String> entry : properties.entrySet()) {
				request.setHeader(entry.getKey(), entry.getValue());
			}
		}

		return this.request(request);
	}

	private HttpResponse request(HttpRequestBase request){
		logger.info("launch http " + request.getMethod() + " request:" + request.getURI());
		request.setConfig(requestConfig);
		// 重试3次
		int retry = 0;
		do {
			try {
				CloseableHttpResponse response = httpClient.execute(request);
				return this.makeContent(request.getURI().toString(), request, response);
			} catch (SSLHandshakeException e){
				//https证书校验失败
				logger.error("ssl error", e);
			} catch (IOException e) {
				request.abort();
				logger.info("io exception when send http.", e);
			} finally {
				request.releaseConnection();
			}
			logger.info("send http fail, retry:" + retry);
		} while (++retry < 3);

		logger.error("send http fail after retry 3 times, url=" + request.getURI());
		return null;
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
		httpResponser.code = response.getStatusLine().getStatusCode();
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
			logger.info("http error", e);
			throw e;
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
	}

	public static RequestConfig buildDefaultRequestConfig(){
		RequestConfig requestConfig = RequestConfig.custom()
				.setConnectTimeout(10000)
				.setConnectionRequestTimeout(10000)
				.setSocketTimeout(10000).build();
		return requestConfig;
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


/**
 * Migrant
 * com.bargetor.migrant.springmvc
 * BodyReaderHttpServletRequestWrapper.java
 * 
 * 2015年5月12日-下午11:42:18
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc.servlet;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.util.StreamUtils;

/**
 *
 * BodyReaderHttpServletRequestWrapper
 * 保存一个请求体的缓存，以便反复读取
 * kin
 * kin
 * 2015年5月12日 下午11:42:18
 * 
 * @version 1.0.0
 *
 */
public class BodyReaderHttpServletRequestWrapper extends HttpServletRequestWrapper{
	/**
	 * body:保存request body 数据，以便反复读取
	 *
	 * @since 1.0.0
	 */
	private final byte[] body;
	
	/**
	 * 创建一个新的实例 BodyReaderHttpServletRequestWrapper.
	 *
	 * @param request
	 * @throws IOException 
	 */
	public BodyReaderHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body = StreamUtils.copyToByteArray(request.getInputStream());
	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getReader()
	 */
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.ServletRequestWrapper#getInputStream()
	 */
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener readListener) {
				// TODO Auto-generated method stub
				
			}
		};
	}

}

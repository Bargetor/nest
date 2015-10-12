/**
 * Migrant
 * com.bargetor.migrant.springmvc
 * HttpServletRequestReuseFilter.java
 * 
 * 2015年5月12日-下午11:51:14
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * HttpServletRequestReuseFilter
 * 主要用来解决spring mvc reqeust body 不能重复读取的问题
 * kin
 * kin
 * 2015年5月12日 下午11:51:14
 * 
 * @version 1.0.0
 *
 */
public class HttpServletRequestReuseFilter implements Filter{

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		ServletRequest requestWrapper = null;  
        if(request instanceof HttpServletRequest) {  
            requestWrapper = new BodyReaderHttpServletRequestWrapper((HttpServletRequest) request);  
        }  
        if(null == requestWrapper) {  
            chain.doFilter(request, response);  
        } else {  
            chain.doFilter(requestWrapper, response);  
        } 
		
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}

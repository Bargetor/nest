package com.bargetor.nest.common.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>description: servlet处理器</p>
 * <p>Date: 2013-9-23 下午03:38:23</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public abstract class ServletProcessor {

	public void doGet(HttpServletRequest request, HttpServletResponse response){}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response){}
}

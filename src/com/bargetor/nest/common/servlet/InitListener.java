package com.bargetor.nest.common.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.bargetor.nest.common.config.AllCommonConfigsLoader;
import com.bargetor.nest.common.jdbc.connection.DBConnectionManager;

/**
 * <p>description: 初始化监听器，用于公共模块的初始化</p>
 * <p>Date: 2013-9-23 下午07:11:20</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class InitListener implements ServletContextListener{

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		AllCommonConfigsLoader.getInstance();
		ServletProcessorManager.getInstance();
		DBConnectionManager.getInstance();
	}

}

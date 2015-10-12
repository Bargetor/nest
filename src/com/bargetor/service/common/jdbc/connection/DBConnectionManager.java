package com.bargetor.service.common.jdbc.connection;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bargetor.service.common.config.AllCommonConfigsLoader;
import com.bargetor.service.common.jdbc.beans.AllJdbcConfigFileBean;
import com.bargetor.service.common.jdbc.beans.JDBCConfigBean;

/**
 * <p>description: 数据库连接池管理类</p>
 * <p>Date: 2013-9-20 下午01:22:37</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class DBConnectionManager {
	
	private static final Logger logger = Logger.getLogger(DBConnectionManager.class);
	
	/**
	 * instance:数据库连接池管理单例
	 */
	private static DBConnectionManager instance;
	
	
	/**
	 * connectionPools:连接池映射
	 */
	private Map<String,DBConnectionPool> connectionPools;
	
	/**
	 * configMaps：配置映射
	 */
	private Map<String,JDBCConfigBean> configMaps;
	
	
	
	
	protected DBConnectionManager(){
		init();
	}
	
	/** 
	  * 释放连接 
	  * @param name 
	  * @param con 
	  */ 
	public void freeConnection(String name, Connection con) 
	{ 
	  DBConnectionPool pool=(DBConnectionPool)connectionPools.get(name);//根据关键名字得到连接池 
	  if(pool!=null) 
	  pool.freeConnection(con);//释放连接 
	} 


	/**
	 *<p>Title: getConnection</p>
	 *<p>Description:根据连接池的名字得到一个连接</p>
	 * @param @param name
	 * @param @return 设定文件
	 * @return  Connection 返回类型
	 * @throws
	*/
	public Connection getConnection(String name) {
		DBConnectionPool pool = null;
		Connection con = null;
		pool = (DBConnectionPool) connectionPools.get(name);// 从名字中获取连接池
		con = pool.getConnection();// 从选定的连接池中获得连接
		return con;
	}

	/**
	 *<p>Title: getConnection</p>
	 *<p>Description:得到一个连接，根据连接池的名字和等待时间</p>
	 * @param @param name
	 * @param @param timeout
	 * @param @return 设定文件
	 * @return  Connection 返回类型
	 * @throws
	*/
	public Connection getConnection(String name, long timeout) {
		DBConnectionPool pool = null;
		Connection con = null;
		pool = connectionPools.get(name);// 从名字中获取连接池
		con = pool.getConnection(timeout);// 从选定的连接池中获得连接
		return con;
	}


	/**
	 *<p>Title: release</p>
	 *<p>Description:释放所有连接</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public synchronized void release() {
		Collection<DBConnectionPool> allpools = connectionPools.values();
		Iterator<DBConnectionPool> it = allpools.iterator();
		while (it.hasNext()) {
			DBConnectionPool pool = it.next();
			if (pool != null)
				pool.release();
		}
		connectionPools.clear();
	}
	

	/**
	 *<p>Title: regDBConnectionPool</p>
	 *<p>Description:注册一个连接池</p>
	 * @param config
	 * @return void 返回类型
	*/
	public void regDBConnectionPool(JDBCConfigBean config){
		DBConnectionPool pool = buildPool(config);
		if(pool == null)return;
		logger.info("注册连接池：" + config.toString());
		this.configMaps.put(config.getName(), config);
		this.connectionPools.put(config.getName(), pool);
	}
	
	/**
	 *<p>Title: getInstance</p>
	 *<p>Description:获取数据库连接池管理单例</p>
	 * @param @return 设定文件
	 * @return  DBConnectionManager 返回类型
	 * @throws
	*/
	public static DBConnectionManager getInstance(){
		if(instance == null){
			instance = new DBConnectionManager();
		}
		return instance;
	}
	
	/********************************************** private methods ***************************************/

	
	/**
	 *<p>Title: init</p>
	 *<p>Description:初始化</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private void init(){
		this.connectionPools = new HashMap<String, DBConnectionPool>();
		this.configMaps = new HashMap<String, JDBCConfigBean>();
		initPools();
	}
	
	/**
	 *<p>Title: initPools</p>
	 *<p>Description:初始化连接池</p>
	 * @return void 返回类型
	*/
	private void initPools(){
		AllJdbcConfigFileBean allConfigBean = (AllJdbcConfigFileBean) AllCommonConfigsLoader.getInstance().getConfigBean(AllJdbcConfigFileBean.CONFIG_ID);
		String[] files = allConfigBean.getFileNames();
		if(files == null)return;
		for(String file : files){
			JDBCConfigBean config = (JDBCConfigBean) AllCommonConfigsLoader.getInstance().loadConfig(file, JDBCConfigBean.class.getName());
			this.regDBConnectionPool(config);
		}
	}
	
	/**
	 * @return 
	 *<p>Title: buildPool</p>
	 *<p>Description:根据配置文件创建连接池</p>
	 * @param @param config 设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private DBConnectionPool buildPool(JDBCConfigBean config){
		if(config == null)return null;
		//连接池存在，不创建，亦或刷新配置 TODO
		if(this.connectionPools.containsKey(config.getName()))return null;
		return new DBConnectionPool(config);
	}
	
}

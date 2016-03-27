package com.bargetor.nest.common.jdbc.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.bargetor.nest.common.jdbc.beans.JDBCConfigBean;

public class DBConnectionPool {
	
	/**
	 * config:数据库配置
	 */
	private JDBCConfigBean config;
	
	private List<Connection> freeConnections;
	
	/**
	 * inUsed:正在使用连接数
	 */
	private int inUsed;
	
	public DBConnectionPool(JDBCConfigBean config){
		this.config = config;
		init();
	}
	

	/**
	 *<p>Title: getConnection</p>
	 *<p>Description: 获取连接</p>
	 * @param @param timeout
	 * @param @return 设定文件
	 * @return  Connection 返回类型
	 * @throws
	*/
	public synchronized Connection getConnection(long timeout) {
		Connection con = findOneConnection();
		if (con == null && timeout > 0) {
			try {
				wait(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			con = findOneConnection(); // 继续获得连接
		}
		if (con != null) {
			this.freeConnections.remove(0);
			this.inUsed++;
		}
		return con;
	}

	/**
	 *<p>Title: getConnection</p>
	 *<p>Description:获取连接</p>
	 * @param @return 设定文件
	 * @return  Connection 返回类型
	 * @throws
	*/
	public synchronized Connection getConnection() {
		return getConnection(0);
	}
	
	/**
	 *<p>Title: freeConnection</p>
	 *<p>Description:归还连接</p>
	 * @param @param con 设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public synchronized void freeConnection(Connection con) {
		this.freeConnections.add(con);// 添加到空闲连接的末尾
		this.inUsed--;
	}
	
	/**
	 *<p>Title: release</p>
	 *<p>Description:注销，释放所有连接</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public synchronized void release() {
		Iterator<Connection> allConns = this.freeConnections.iterator();
		while (allConns.hasNext()) {
			Connection con = (Connection) allConns.next();
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		this.freeConnections.clear();

	}
	
	
	
	/******************************************** private methods *********************************************/

	private synchronized Connection findOneConnection(){
		Connection con = null;
		if (this.freeConnections.size() > 0) {
			con = (Connection) this.freeConnections.get(0);
		}else{
			if (this.config.getMaxConn() == 0 || this.config.getMaxConn() < this.inUsed) {
				con = null;// 达到最大连接数，暂时不能获得连接了。
			}else{
				con = addNewConnection();
			}
		}
		return con;
	}
	
	
	/**
	 *<p>Title: init</p>
	 *<p>Description:初始化</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private void init(){
		this.freeConnections = new ArrayList<Connection>();
		initPoolConnection();
	}
	
	/**
	 *<p>Title: initConnection</p>
	 *<p>Description:初始化连接池连接</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private void initPoolConnection(){
		int count = this.config.getMinConn() - this.freeConnections.size();
		while(count > 0){
			addNewConnection();
			count--;
		}
	}
	
	/**
	 *<p>Title: addNewConnection</p>
	 *<p>Description:增加一个新连接</p>
	 * @param @return 设定文件
	 * @return  Connection 返回类型
	 * @throws
	*/
	private Connection addNewConnection(){
		Connection newConnection = buildNewConnection();
		if(newConnection != null){
			this.freeConnections.add(newConnection);
		}
		return newConnection;
	}
	
	/**
	 *<p>Title: buildConnection</p>
	 *<p>Description:创建连接</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private Connection buildNewConnection(){
		try {
			Class.forName(config.getDriverName());
			return DriverManager.getConnection(this.config.getUrl(),this.config.getUser(),this.config.getPassword());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}

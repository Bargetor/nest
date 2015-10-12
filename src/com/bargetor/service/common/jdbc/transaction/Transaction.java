package com.bargetor.service.common.jdbc.transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.bargetor.service.common.jdbc.connection.DBConnectionManager;

/**
 * <p>description: 事务</p>
 * <p>Date: 2013-9-20 下午04:07:01</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class Transaction {

	private Connection conn;

	private String name;

	public Transaction(String name) {
		this.name = name;
		conn = DBConnectionManager.getInstance().getConnection(this.name);
	}

	/**
	 *<p>Title: getConnection</p>
	 *<p>Description:获取连接</p>
	 * @param @return 设定文件
	 * @return  Connection 返回类型
	 * @throws
	*/
	public Connection getConnection() {
		return conn;
	}
	
	/**
	 *<p>Title: getStatement</p>
	 *<p>Description:获取声明</p>
	 * @return
	 * @return Statement 返回类型
	*/
	public Statement getStatement(){
		if(conn != null){
			try {
				return conn.createStatement();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 *<p>Title: getPreparedStatement</p>
	 *<p>Description:获取PreparedStatement</p>
	 * @param @param sql 预编译sql
	 * @param @return 
	 * @return  PreparedStatement 
	 * @throws
	*/
	public PreparedStatement getPreparedStatement(String sql){
		try {
			if(conn != null){
				return getConnection().prepareStatement(sql);				
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *<p>Title: getName</p>
	 *<p>Description:获取名称（同连接名称）</p>
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public String getName() {
		return name;
	}

	/**
	 *<p>Title: startTransaction</p>
	 *<p>Description:开启事务</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void startTransaction() {
		try {
			if(conn == null){
				conn = DBConnectionManager.getInstance().getConnection(this.name);				
			}
			conn.setAutoCommit(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *<p>Title: rollback</p>
	 *<p>Description:回滚事务</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void rollback() {
		try {
			if (conn != null) conn.rollback();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *<p>Title: commit</p>
	 *<p>Description:提交事务,事务提交后，将被注销</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void commit() { 
		try {
			if (conn != null){
				conn.commit();
				this.release();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 *<p>Title: release</p>
	 *<p>Description:注销事务</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void release() {
		try {
			if (conn != null) {
				DBConnectionManager.getInstance().freeConnection(getName(), conn);
				conn = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
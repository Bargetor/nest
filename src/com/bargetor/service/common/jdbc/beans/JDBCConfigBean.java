package com.bargetor.service.common.jdbc.beans;

/**
 * <p>description: 数据库配置BEAN</p>
 * <p>Date: 2013-9-20 下午01:49:18</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class JDBCConfigBean {
	
	private String name;
	
	private String driverName;
	
	private String url;
	
	private String user;
	
	private String password;
	
	private int minConn;
	
	private int maxConn;

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMinConn() {
		return minConn;
	}

	public void setMinConn(int minConn) {
		this.minConn = minConn;
	}

	public int getMaxConn() {
		return maxConn;
	}

	public void setMaxConn(int maxConn) {
		this.maxConn = maxConn;
	}

	public String toString() {
		return "[driverName=" + driverName + ", maxConn="
				+ maxConn + ", minConn=" + minConn + ", name=" + name
				+ ", password=" + password + ", url=" + url + ", user=" + user
				+ "]";
	}
	
	
	
}

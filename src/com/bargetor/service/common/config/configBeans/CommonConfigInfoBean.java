package com.bargetor.service.common.config.configBeans;

/**
 * <p>description: 公共配置信息BEAN</p>
 * <p>Date: 2013-9-16 下午04:45:06</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class CommonConfigInfoBean {
	private String id;
	
	private String fileName;
	
	private String className;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CommonConfigInfoBean(String id, String fileName, String className) {
		super();
		this.id = id;
		this.fileName = fileName;
		this.className = className;
	}

	public String toString() {
		return "[className=" + className + ", fileName="
				+ fileName + ", id=" + id + "]";
	}

}

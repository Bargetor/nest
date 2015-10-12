package com.bargetor.service.common.jdbc.beans;

import com.bargetor.service.common.util.StringUtil;

/**
 * <p>description: 所有的JDBC配置文件名BEAN</p>
 * <p>Date: 2013-9-23 下午06:33:37</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class AllJdbcConfigFileBean {
	
	public static final String CONFIG_ID = "all-jdbc-config";
	
	private String configFiles;

	public String getConfigFiles() {
		return configFiles;
	}

	public void setConfigFiles(String configFiles) {
		this.configFiles = configFiles;
	}
	
	/**
	 *<p>Title: getFileNames</p>
	 *<p>Description:获取所有文件名</p>
	 * @return
	 * @return String[] 返回类型
	*/
	public String[] getFileNames(){
		if(!StringUtil.isNullStr(configFiles)){
			return this.configFiles.split(";");
		}
		return null;
	}

}

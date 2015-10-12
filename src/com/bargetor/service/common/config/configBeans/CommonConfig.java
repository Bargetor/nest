package com.bargetor.service.common.config.configBeans;

/**
 * <p>description: 公共配置</p>
 * <p>Date: 2013-9-23 上午09:55:42</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class CommonConfig {
	
	public static final String CONFIG_ID = "common-config";

	/**
	 * encoding:编码
	 */
	private String encoding;

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	
}

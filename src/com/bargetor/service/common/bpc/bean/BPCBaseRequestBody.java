/**
 * Migrant
 * com.bargetor.migrant.support.http
 * BaseRequestBody.java
 * 
 * 2015年5月9日-下午2:09:38
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bpc.bean;

/**
 *
 * BaseRequestBody
 * 基础的请求体封装，根据JSON-RPC封装
 * kin
 * kin
 * 2015年5月9日 下午2:09:38
 * 
 * @version 1.0.0
 *
 */
public class BPCBaseRequestBody {
	/**
	 * bpc:Bargetor Communication Protocol, BCP
	 *
	 * @since 1.0.0
	 */
	private String bpc;
	/**
	 * id:调用ID
	 *
	 * @since 1.0.0
	 */
	private String id;
	/**
	 * params:参数，可以自定义为任何类型
	 *
	 * @since 1.0.0
	 */
	private String params;

	public String getBpc() {
		return bpc;
	}

	public void setBpc(String bpc) {
		this.bpc = bpc;
	}

	/**
	 * id
	 *
	 * @return  the id
	 * @since   1.0.0
	 */
	
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * params
	 *
	 * @return  the params
	 * @since   1.0.0
	 */
	
	public String getParams() {
		return params;
	}
	/**
	 * @param params the params to set
	 */
	public void setParams(String params) {
		this.params = params;
	}

	
}

/**
 * Migrant
 * com.bargetor.migrant.support.http
 * BaseResponseBody.java
 * 
 * 2015年5月9日-下午2:09:53
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc.bean;

import com.bargetor.nest.common.util.JsonUtil;

/**
 *
 * BaseResponseBody
 * 
 * kin
 * kin
 * 2015年5月9日 下午2:09:53
 * 
 * @version 1.0.0
 *
 */
public class BPCBaseResponseBody {
	/**
	 * bpc:Bargetor Communication Protocol, BCP
	 *
	 * @since 1.0.0
	 */
	private String bpc = "1.0.0";
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
	private BPCResponseResult result;
	
	/**
	 * error:协议错误
	 *
	 * @since 1.0.0
	 */
	private BPCResponseError error;

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
	 * result
	 *
	 * @return  the result
	 * @since   1.0.0
	 */
	
	public BPCResponseResult getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(BPCResponseResult result) {
		this.result = result;
	}

	/**
	 * error
	 *
	 * @return  the error
	 * @since   1.0.0
	 */
	
	public BPCResponseError getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(BPCResponseError error) {
		this.error = error;
	}

	public String toJsonString(){
		return JsonUtil.beanToJson(this).toString();
	}
}

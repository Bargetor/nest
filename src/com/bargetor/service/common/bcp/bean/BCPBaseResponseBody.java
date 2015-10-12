/**
 * Migrant
 * com.bargetor.migrant.support.http
 * BaseResponseBody.java
 * 
 * 2015年5月9日-下午2:09:53
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.bean;

import com.bargetor.service.common.util.JsonUtil;

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
public class BCPBaseResponseBody {
	/**
	 * bcp:Bargetor Communication Protocol, BCP
	 *
	 * @since 1.0.0
	 */
	private String bcp = "1.0.0";
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
	private BCPResponseResult result;
	
	/**
	 * error:协议错误
	 *
	 * @since 1.0.0
	 */
	private BCPResponseError error;
	/**
	 * bcp
	 *
	 * @return  the bcp
	 * @since   1.0.0
	 */
	
	public String getBcp() {
		return bcp;
	}
	/**
	 * @param bcp the bcp to set
	 */
	public void setBcp(String bcp) {
		this.bcp = bcp;
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
	
	public BCPResponseResult getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(BCPResponseResult result) {
		this.result = result;
	}

	/**
	 * error
	 *
	 * @return  the error
	 * @since   1.0.0
	 */
	
	public BCPResponseError getError() {
		return error;
	}
	/**
	 * @param error the error to set
	 */
	public void setError(BCPResponseError error) {
		this.error = error;
	}

	public String toJsonString(){
		return JsonUtil.beanToJson(this).toString();
	}
}

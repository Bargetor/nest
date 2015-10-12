/**
 * bargetorCommon
 * com.bargetor.service.common.bcp
 * BCPResponseResultAndError.java
 * 
 * 2015年5月13日-下午11:18:36
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bcp.bean;

/**
 *
 * BCPResponseResultAndError
 * 
 * kin
 * kin
 * 2015年5月13日 下午11:18:36
 * 
 * @version 1.0.0
 *
 */
public class BCPResponseResultAndError {
	private BCPResponseResult result;
	private BCPResponseError error;
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
	

}

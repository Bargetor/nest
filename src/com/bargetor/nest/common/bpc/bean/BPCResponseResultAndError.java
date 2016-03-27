/**
 * bargetorCommon
 * com.bargetor.nest.common.bpc
 * BPCResponseResultAndError.java
 * 
 * 2015年5月13日-下午11:18:36
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.bpc.bean;

/**
 *
 * BPCResponseResultAndError
 * 
 * kin
 * kin
 * 2015年5月13日 下午11:18:36
 * 
 * @version 1.0.0
 *
 */
public class BPCResponseResultAndError {
	private BPCResponseResult result;
	private BPCResponseError error;
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
	

}

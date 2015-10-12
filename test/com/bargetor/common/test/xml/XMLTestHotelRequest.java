/**
 * bargetorCommon
 * com.bargetor.common.test.xml
 * XMLTestHotelRequest.java
 * 
 * 2015年6月13日-下午5:28:25
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.common.test.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
/**
 *
 * XMLTestHotelRequest
 * 
 * kin
 * kin
 * 2015年6月13日 下午5:28:25
 * 
 * @version 1.0.0
 *
 */



public class XMLTestHotelRequest {
	
	@XmlElement(name = "RequestBody")
	private XMLTestPingRequestBody requestBody;

	/**
	 * requestBody
	 *
	 * @return  the requestBody
	 * @since   1.0.0
	 */
	@XmlTransient
	public XMLTestPingRequestBody getRequestBody() {
		return requestBody;
	}

	/**
	 * @param requestBody the requestBody to set
	 */
	public void setRequestBody(XMLTestPingRequestBody requestBody) {
		this.requestBody = requestBody;
	}
	
	
}

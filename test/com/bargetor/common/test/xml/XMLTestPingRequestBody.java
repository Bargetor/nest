/**
 * bargetorCommon
 * com.bargetor.common.test.xml
 * XMLTestSubClass.java
 * 
 * 2015年6月13日-下午5:34:40
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.common.test.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * XMLTestSubClass
 * 
 * kin
 * kin
 * 2015年6月13日 下午5:34:40
 * 
 * @version 1.0.0
 *
 */
public class XMLTestPingRequestBody extends XMLTestHotelRequestBody{
	
	@XmlElement(name = "OTA_PingRQ", namespace = "http://www.opentravel.org/OTA/2003/05")
	private XMLTestPingRequestData requestData;

	/**
	 * requestData
	 *
	 * @return  the requestData
	 * @since   1.0.0
	 */
	@XmlTransient
	public XMLTestPingRequestData getRequestData() {
		return requestData;
	}

	/**
	 * @param requestData the requestData to set
	 */
	public void setRequestData(XMLTestPingRequestData requestData) {
		this.requestData = requestData;
	}

	
	
	
}

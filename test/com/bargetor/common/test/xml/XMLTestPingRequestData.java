/**
 * bargetorCommon
 * com.bargetor.common.test.xml
 * XMLTestRequestData.java
 * 
 * 2015年6月13日-下午5:45:29
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.common.test.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * XMLTestRequestData
 * 
 * kin
 * kin
 * 2015年6月13日 下午5:45:29
 * 
 * @version 1.0.0
 *
 */
public class XMLTestPingRequestData {
	@XmlElement(name = "EchoData", namespace = "http://www.opentravel.org/OTA/2003/05")
	private String data;

	/**
	 * data
	 *
	 * @return  the data
	 * @since   1.0.0
	 */
	@XmlTransient
	public String getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	public void setData(String data) {
		this.data = data;
	}
	
	
}

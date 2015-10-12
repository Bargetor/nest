/**
 * bargetorCommon
 * com.bargetor.common.test.xml
 * XMLTestBean.java
 * 
 * 2015年6月13日-下午4:32:40
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.common.test.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * XMLTestBean
 * 
 * kin
 * kin
 * 2015年6月13日 下午4:32:40
 * 
 * @version 1.0.0
 *
 */
@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLTestBean {
	
	@XmlElement(name = "Header")
	private XMLTestHeader header;

	@XmlElement(name = "HotelRequest")
	private XMLTestHotelRequest request;
	/**
	 * header
	 *
	 * @return  the header
	 * @since   1.0.0
	 */
	@XmlTransient
	public XMLTestHeader getHeader() {
		return header;
	}

	/**
	 * @param header the header to set
	 */
	public void setHeader(XMLTestHeader header) {
		this.header = header;
	}

	/**
	 * request
	 *
	 * @return  the request
	 * @since   1.0.0
	 */
	@XmlTransient
	public XMLTestHotelRequest getRequest() {
		return request;
	}

	/**
	 * @param request the request to set
	 */
	public void setRequest(XMLTestHotelRequest request) {
		this.request = request;
	} 
	
	

}

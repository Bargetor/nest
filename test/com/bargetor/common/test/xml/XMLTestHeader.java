/**
 * bargetorCommon
 * com.bargetor.common.test.xml
 * XMLTestHeader.java
 * 
 * 2015年6月13日-下午4:35:59
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.common.test.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * XMLTestHeader
 * 
 * kin
 * kin
 * 2015年6月13日 下午4:35:59
 * 
 * @version 1.0.0
 *
 */
public class XMLTestHeader {
	
	@XmlAttribute(name = "AllianceID")
	private String allianceID;
	
	@XmlAttribute(name = "SID")
	private String sid;
	
	@XmlAttribute(name = "TimeStamp")
	private String timeStamp;
	
	@XmlAttribute(name = "RequestType")
	private String requestType;
	
	@XmlAttribute(name = "Signature")
	private String signature;

	/**
	 * allianceID
	 *
	 * @return  the allianceID
	 * @since   1.0.0
	 */
	@XmlTransient
	public String getAllianceID() {
		return allianceID;
	}

	/**
	 * @param allianceID the allianceID to set
	 */
	public void setAllianceID(String allianceID) {
		this.allianceID = allianceID;
	}

	/**
	 * sid
	 *
	 * @return  the sid
	 * @since   1.0.0
	 */
	@XmlTransient
	public String getSid() {
		return sid;
	}

	/**
	 * @param sid the sid to set
	 */
	public void setSid(String sid) {
		this.sid = sid;
	}

	/**
	 * timeStamp
	 *
	 * @return  the timeStamp
	 * @since   1.0.0
	 */
	@XmlTransient
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	/**
	 * requestType
	 *
	 * @return  the requestType
	 * @since   1.0.0
	 */
	@XmlTransient
	public String getRequestType() {
		return requestType;
	}

	/**
	 * @param requestType the requestType to set
	 */
	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	/**
	 * signature
	 *
	 * @return  the signature
	 * @since   1.0.0
	 */
	@XmlTransient
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	
	
}

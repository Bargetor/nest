/**
 * bargetorCommon
 * com.bargetor.service.ucs.edm.bean
 * EDMMailSendParams.java
 * 
 * 2015年5月4日-下午10:02:10
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.ucs.edm.bean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * EDMMailSendParams
 * 
 * kin
 * kin
 * 2015年5月4日 下午10:02:10
 * 
 * @version 1.0.0
 *
 */
public class EDMMailSendBaseParams {
	private String form;
	private List<String> to;
	private String subject;
	/**
	 * fromName:发件人名称
	 *
	 * @since 1.0.0
	 */
	private String fromName;
	/**
	 * bcc:密送
	 *
	 * @since 1.0.0
	 */
	private List<String> bcc;
	/**
	 * cc:抄送
	 *
	 * @since 1.0.0
	 */
	private List<String> cc;
	
	public void addTo(String to){
		if(this.to == null){
			this.to = new ArrayList<String>();
		}
		this.to.add(to);
	}
	
	public void addTo(List<String> to){
		if(this.to == null){
			this.to = new ArrayList<String>();
		}
		this.to.addAll(to);
	}
	
	public void addBcc(String bcc){
		if(this.bcc == null){
			this.bcc = new ArrayList<String>();
		}
		this.bcc.add(bcc);
	}
	
	public void addBcc(List<String> bcc){
		if(this.bcc == null){
			this.bcc = new ArrayList<String>();
		}
		this.bcc.addAll(bcc);
	}
	
	public void addCc(String cc){
		if(this.cc == null){
			this.cc = new ArrayList<String>();
		}
		this.cc.add(cc);
	}
	
	public void addCC(List<String> cc){
		if(this.cc == null){
			this.cc = new ArrayList<String>();
		}
		this.cc.addAll(cc);
	}
	
	/**
	 * form
	 *
	 * @return  the form
	 * @since   1.0.0
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form the form to set
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * to
	 *
	 * @return  the to
	 * @since   1.0.0
	 */
	
	public List<String> getTo() {
		return to;
	}
	/**
	 * @param to the to to set
	 */
	public void setTo(List<String> to) {
		this.to = to;
	}
	/**
	 * subject
	 *
	 * @return  the subject
	 * @since   1.0.0
	 */
	
	public String getSubject() {
		return subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}
	/**
	 * fromName
	 *
	 * @return  the fromName
	 * @since   1.0.0
	 */
	
	public String getFromName() {
		return fromName;
	}
	/**
	 * @param fromName the fromName to set
	 */
	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	/**
	 * bcc
	 *
	 * @return  the bcc
	 * @since   1.0.0
	 */
	
	public List<String> getBcc() {
		return bcc;
	}
	/**
	 * @param bcc the bcc to set
	 */
	public void setBcc(List<String> bcc) {
		this.bcc = bcc;
	}
	/**
	 * cc
	 *
	 * @return  the cc
	 * @since   1.0.0
	 */
	
	public List<String> getCc() {
		return cc;
	}
	/**
	 * @param cc the cc to set
	 */
	public void setCc(List<String> cc) {
		this.cc = cc;
	}
	

}

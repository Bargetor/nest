/**
 * bargetorCommon
 * com.bargetor.service.ucs.edm.bean
 * EDMHtmlMailSendParams.java
 * 
 * 2015年5月4日-下午10:13:13
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.ucs.edm.bean;

/**
 *
 * EDMHtmlMailSendParams
 * 
 * kin
 * kin
 * 2015年5月4日 下午10:13:13
 * 
 * @version 1.0.0
 *
 */
public class EDMHtmlMailSendParams extends EDMMailSendBaseParams{
	private String html;

	/**
	 * html
	 *
	 * @return  the html
	 * @since   1.0.0
	 */
	
	public String getHtml() {
		return html;
	}

	/**
	 * @param html the html to set
	 */
	public void setHtml(String html) {
		this.html = html;
	}
	
}

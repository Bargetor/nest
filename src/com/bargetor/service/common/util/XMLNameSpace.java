/**
 * bargetorCommon
 * com.bargetor.service.common.util
 * XMLNameSpace.java
 * 
 * 2015年6月13日-下午7:19:07
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.util;

/**
*
* XMLNameSpace
* 
* kin
* kin
* 2015年6月13日 下午7:11:49
* 
* @version 1.0.0
*
*/
public class XMLNameSpace{
	private String namespacePrefix;
	private String namespaceURI;
	/**
	 * namespacePrefix
	 *
	 * @return  the namespacePrefix
	 * @since   1.0.0
	 */
	
	public String getNamespacePrefix() {
		return namespacePrefix;
	}
	/**
	 * @param namespacePrefix the namespacePrefix to set
	 */
	public void setNamespacePrefix(String namespacePrefix) {
		this.namespacePrefix = namespacePrefix;
	}
	/**
	 * namespaceURI
	 *
	 * @return  the namespaceURI
	 * @since   1.0.0
	 */
	
	public String getNamespaceURI() {
		return namespaceURI;
	}
	/**
	 * @param namespaceURI the namespaceURI to set
	 */
	public void setNamespaceURI(String namespaceURI) {
		this.namespaceURI = namespaceURI;
	}
	
	
}
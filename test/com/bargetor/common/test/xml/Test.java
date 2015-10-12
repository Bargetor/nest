/**
 * bargetorCommon
 * com.bargetor.common.test.xml
 * Test.java
 * 
 * 2015年6月13日-下午4:41:18
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.common.test.xml;

import com.bargetor.service.common.util.XmlUtil;

/**
 *
 * Test
 * 
 * kin
 * kin
 * 2015年6月13日 下午4:41:18
 * 
 * @version 1.0.0
 *
 */
public class Test {

	public static void main(String[] args){
		XMLTestBean bean = XmlUtil.xmlToBean("<?xml version=\"1.0\" encoding=\"utf-8\"?><!--接口提供方：携程；调用方：合作方--><Request><!--AllianceID:分销商ID;SID:站点ID;TimeStamp:响应时间戳（从1970年到现在的秒数）;RequestType:请求接口的类型;Signature:MD5加密串--><Header  AllianceID=\"x\" SID=\"xx\" TimeStamp=\"xxxxxx\"  RequestType=\" OTA_Ping \" Signature=\"xxxxxxx\" /><HotelRequest><RequestBody xmlns:ns=\"http://www.opentravel.org/OTA/2003/05\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><ns:OTA_PingRQ><!--测试文本：string类型--><ns:EchoData>阿什顿</ns:EchoData></ns:OTA_PingRQ></RequestBody></HotelRequest></Request>", XMLTestBean.class.getPackage().getName());
		System.out.println(XmlUtil.beanToXml(bean));
	}
	
}

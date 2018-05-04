/**
 * bargetorCommon
 * com.bargetor.nest.ucs.edm
 * EDMServer.java
 * 
 * 2015年5月4日-下午9:45:59
 *  2015Bargetor-版权所有
 *
 */
//package com.bargetor.nest.ucs.edm;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import com.bargetor.nest.common.http.HttpRequester;
//import com.bargetor.nest.common.http.HttpResponse;
//import com.bargetor.nest.common.util.StringUtil;
//import com.bargetor.nest.ucs.edm.bean.EDMHtmlMailSendParams;
//import com.bargetor.nest.ucs.edm.bean.EDMMailSendBaseParams;
//
///**
// *
// * EDMServer
// * 邮件营销平台服务,整套服务基于sendcloud
// * kin
// * kin
// * 2015年5月4日 下午9:45:59
// *
// * @version 1.0.0
// *
// */
//public class EDMServer {
//	private static EDMServer instance;
//
//	protected EDMServer(){
//
//	}
//
//	public static EDMServer getInstance(){
//		if(instance == null){
//			instance = new EDMServer();
//		}
//		return instance;
//	}
//
//	/**
//	 * send(发送邮件)
//	 * 自动适配类型
//	 * @param params
//	 *void
//	 * @throws IOException
//	 * @exception
//	 * @since  1.0.0
//	*/
//	public <T extends EDMMailSendBaseParams>void send(T params) throws IOException{
//		if(params == null)return;
//		if(params instanceof EDMHtmlMailSendParams){
//			this.sendHtmlMail((EDMHtmlMailSendParams) params);
//		}
//	}
//
//	/**
//	 * sendHtmlMail(发送HTML邮件)
//	 * (这里描述这个方法适用条件 – 可选)
//	 * @param params
//	 *void
//	 * @throws IOException
//	 * @exception
//	 * @since  1.0.0
//	*/
//	public void sendHtmlMail(EDMHtmlMailSendParams params) throws IOException{
//		Map<String, String> paramsMap = this.buildBaseParams(params);
//		if(StringUtil.isNullStr(params.getHtml()))return;
//		paramsMap.put("html", params.getHtml());
//
//		HttpRequester requester = new HttpRequester();
//		HttpResponse respons = requester.sendPost(EDMConstant.MAIL_SEND_URL, paramsMap);
//		System.out.println(respons.getContent());
//	}
//
//	/********************************************* private method ********************************************/
//	/**
//	 * buildBaseParams(构建基础请求参数)
//	 * (这里描述这个方法适用条件 – 可选)
//	 * @return
//	 *Map<String,String>
//	 * @exception
//	 * @since  1.0.0
//	*/
//	private Map<String, String> buildBaseParams(EDMMailSendBaseParams params){
//		Map<String, String> paramsMap = new HashMap<String, String>();
//		paramsMap.put("api_user", EDMConstant.SC_API_USER);
//		paramsMap.put("api_key", EDMConstant.SC_API_KEY);
//
//		paramsMap.put("subject", params.getSubject());
//
//		paramsMap.put("from", params.getForm());
//		if(StringUtil.isNotNullStr(params.getFromName())){
//			paramsMap.put("fromname", params.getFromName());
//		}
//
//		paramsMap.put("to", StringUtil.joinList(params.getTo(), ";"));
//		if(params.getCc() != null){
//			paramsMap.put("cc", StringUtil.joinList(params.getCc(), ";"));
//		}
//		if(params.getBcc() != null){
//			paramsMap.put("bcc", StringUtil.joinList(params.getBcc(), ";"));
//		}
//
//		return paramsMap;
//	}
//
//
//	public static void main(String[] args) throws IOException{
//		EDMHtmlMailSendParams params = new EDMHtmlMailSendParams();
//		params.setForm("migrant@bargetor.com");
//		params.addTo("madgin@qq.com");
//		params.setSubject("Chestnut系统通知");
//		params.setHtml("Chestnut需要您验证密码', '你太棒了！你已成功的从SendCloud发送了一封测试邮件，接下来快登录前台去完善账户信息吧！");
//
//		EDMServer.getInstance().send(params);
//	}
//
//}

package com.bargetor.nest.common.servlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.bargetor.nest.common.servlet.beans.ServletProcessorConfigBean;
import com.bargetor.nest.common.util.ReflectUtil;
import com.bargetor.nest.common.util.StringUtil;
import com.bargetor.nest.common.util.XmlUtil;

/**
 * <p>description: servlet�������</p>
 * <p>Date: 2013-9-23 ����03:50:47</p>
 * <p>modify��</p>
 * @author: Madgin
 * @version: 1.0
 */
public class ServletProcessorManager {
	private static final Logger logger = Logger.getLogger(ServletProcessorManager.class);
	
	/**
	 * instance:����
	 */
	private static ServletProcessorManager instance;
	
	private Document configDoc;
	
	private Map<String,ServletProcessorConfigBean> configMapForId;
	
	private Map<String,ServletProcessorConfigBean> configMapForUrl;
	
	
	protected ServletProcessorManager(){
		init();
	}
	
	
	/**
	 *<p>Title: getInstance</p>
	 *<p>Description:��ȡ����</p>
	 * @return
	 * @return ServletProcessorConfigManager ��������
	*/
	public static ServletProcessorManager getInstance(){
		if(instance == null){
			instance = new ServletProcessorManager();
		}
		return instance;
	}
	
	/**
	 *<p>Title: getConfigForUrlPattern</p>
	 *<p>Description:��ȡ����</p>
	 * @param urlPattern
	 * @return
	 * @return ServletProcessorConfigBean ��������
	*/
	public ServletProcessorConfigBean getConfigForUrlPattern(String urlPattern){
		return this.configMapForUrl.get(urlPattern);
	}
	
	/**
	 *<p>Title: getConfigForID</p>
	 *<p>Description:��ȡ����</p>
	 * @param id
	 * @return
	 * @return ServletProcessorConfigBean ��������
	*/
	public ServletProcessorConfigBean getConfigForID(String id){
		return this.configMapForUrl.get(id);
	}
	
	/**
	 *<p>Title: getProcessorForUrlPattern</p>
	 *<p>Description:��ȡ����ʵ��</p>
	 * @param urlPattern
	 * @return
	 * @return ServletProcessor ��������
	*/
	public ServletProcessor getProcessorForUrlPattern(String urlPattern){
		ServletProcessorConfigBean config = this.getConfigForUrlPattern(urlPattern);
		ServletProcessor processor = null;
		if(config != null){
			processor = this.buildServletProcessor(config.getProcessorName());

		}
		return processor;
	}
	
	/**
	 *<p>Title: getProcessorForID</p>
	 *<p>Description:��ȡ����ʵ��</p>
	 * @param id
	 * @return
	 * @return ServletProcessor ��������
	*/
	public ServletProcessor getProcessorForID(String id){
		ServletProcessorConfigBean config = this.getConfigForID(id);
		ServletProcessor processor = null;
		if(config != null){
			processor = this.buildServletProcessor(config.getProcessorName());

		}
		return processor;
	}
	
	
	/****************************************** private methods *******************************************/

	private void init(){
		this.configDoc = XmlUtil.initDocument("servlet-processor-configs.xml");
		this.configMapForId = new HashMap<String, ServletProcessorConfigBean>();
		this.configMapForUrl = new HashMap<String, ServletProcessorConfigBean>();
		this.readConfigInfoBeans();
	}
	
	/**
	 *<p>Title: readConfigInfoBeans</p>
	 *<p>Description:��ȡ������Ϣ</p>
	 * @param  �趨�ļ�
	 * @return  void ��������
	 * @throws
	*/
	private void readConfigInfoBeans(){
		List<Node> configNodeList = XmlUtil.getNodeList(this.configDoc,"processor");
		for(Node node : configNodeList){
			String id = node.getAttributes().getNamedItem("id").getNodeValue();
			String urlPattern = node.getAttributes().getNamedItem("urlPattern").getNodeValue();
			String processorName = node.getAttributes().getNamedItem("processorName").getNodeValue();
			ServletProcessorConfigBean bean = new ServletProcessorConfigBean(id, urlPattern, processorName);
			this.configMapForId.put(id, bean);
			this.configMapForUrl.put(urlPattern, bean);
			logger.info("���ServletProcessor:" + bean.toString());
		}
	}
	
	private ServletProcessor buildServletProcessor(String processorName) {
		ServletProcessor processor = null;
		if (!StringUtil.isNullStr(processorName)) {
			try {
				processor = (ServletProcessor) ReflectUtil.newInstance(processorName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return processor;
	}
}

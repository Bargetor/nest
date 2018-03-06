package com.bargetor.nest.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlUtil {
	private static final Logger logger = Logger.getLogger(XmlUtil.class);
	/**
	 * xmlToBean(XML to bean)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param xmlStr
	 * @return
	 * T
	 * @exception
	 * @since  1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static <T>T xmlToBean(String xmlStr, Class<?>... classes){
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(classes);
			return (T) xmlToBean(xmlStr, jc);
		} catch (JAXBException e) {
			logger.error("xml to bean error", e);
			return null;
		}
	}
	
	
//	@SuppressWarnings("unchecked")
//	public static <T>T xmlToBean(String xmlStr, Class<T> clazz, final XMLNameSpace... nameSpaces){
//		try {
//			JAXBContext jc = JAXBContext.newInstance(clazz);
//			Unmarshaller unmarshaller = jc.createUnmarshaller();
//			
//			//处理命名空间
//			if(nameSpaces != null){
//				unmarshaller.setProperty("com.sun.xml.bind.namespacePrefixMapper", new NamespacePrefixMapper(){
//
//					@Override
//					public String getPreferredPrefix(String namespaceUri,
//							String suggestion, boolean requirePrefix) {
//						for(XMLNameSpace nameSpace : nameSpaces){
//							 if (namespaceUri.equals(nameSpace.getNamespaceURI())) return nameSpace.getNamespacePrefix();
//			                 return suggestion;
//						}
//						return null;
//					}
//					
//				});
//			}
//			
//			StringReader reader = new StringReader(xmlStr);
//			T result = (T) unmarshaller.unmarshal(reader);
//			
//			return result;
//		} catch (JAXBException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
	
	/**
	 * xmlToBean(xml to bean by schema)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param xmlStr
	 * @param packageName
	 * @return
	 * T
	 * @exception
	 * @since  1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static <T>T xmlToBean(String xmlStr, String packageName){
		JAXBContext jc;
		try {
			jc = JAXBContext.newInstance(packageName);
			return (T) xmlToBean(xmlStr, jc);
		} catch (JAXBException e) {
			logger.error("xml to bean error", e);
			return null;
		}
			
	}
	
	private static Object xmlToBean(String xmlStr, JAXBContext jc){
		try {
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			unmarshaller.setSchema(null);
			StringReader reader = new StringReader(xmlStr);
			
			Object result = unmarshaller.unmarshal(reader);
			return result;
		} catch (JAXBException e) {
			logger.error("xml to bean error", e);
			return null;
		}
	}
	
	/**
	 * beanToXml(bean to xml)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param bean
	 * @return
	 * String
	 * @exception
	 * @since  1.0.0
	*/
	public static String beanToXml(Object bean, Class<?>... classes){
		try {
			if(bean == null)return null;
			List<Class<?>> classList = new ArrayList<Class<?>>();
			classList.add(bean.getClass());
			if(classes != null){
				for(Class<?> clazz : classes){
					classList.add(clazz);
				}
			}
			
			Class<?>[] temp = {bean.getClass()};
			JAXBContext jc = JAXBContext.newInstance(classList.toArray(temp));
			return beanToXml(bean, jc);
		} catch (JAXBException e) {
			logger.error("bean to xml error", e);
			return null;
		}
	}
	
	/**
	 * beanToXml(bean to xml)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param bean
	 * @return
	 * String
	 * @exception
	 * @since  1.0.0
	*/
	public static String beanToXmlByPackage(Object bean){
		try {
			if(bean == null)return null;
			JAXBContext jc = JAXBContext.newInstance(bean.getClass().getPackage().getName());
			return beanToXml(bean, jc);
		} catch (JAXBException e) {
			logger.error("bean to xml error", e);
			return null;
		}
	}
	
	private static String beanToXml(Object bean, JAXBContext jc){
		try {
			if(bean == null)return null;
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);// 是否省略xm头声明信息
			StringWriter writer = new StringWriter();
			marshaller.marshal(bean, writer);
			return writer.getBuffer().toString();
		} catch (JAXBException e) {
			logger.error("bean to xml error", e);
			return null;
		}
	}
	
	/**
	 *<p>Title: initDocument</p>
	 *<p>Description:加载总配置文档</p>
	 * @param docName
	 * @return
	 * @return Document 返回类型
	*/
	public static Document initDocument(String docName){
		Document doc = null;
		if(StringUtil.isNullStr(docName))return doc;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
        DocumentBuilder builder;
		try {
			builder = dbf.newDocumentBuilder();
			InputStream in = XmlUtil.class.getClassLoader().getResourceAsStream(docName);  
			doc = builder.parse(in);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return doc;
	}
	
	/**
	 *<p>Title: getNodeList</p>
	 *<p>Description:获取节点</p>
	 * @param @param nodeName
	 * @param @return 设定文件
	 * @return  List<Node> 返回类型
	 * @throws
	*/
	public static List<Node> getNodeList(Document doc, String nodeName){
		return getNodeList(doc,nodeName, null);
	}
	
	/**
	 *<p>Title: getNodeList</p>
	 *<p>Description:获取节点</p>
	 * @param @param nodeName
	 * @param @param node
	 * @param @return 设定文件
	 * @return  List<Node> 返回类型
	 * @throws
	*/
	public static List<Node> getNodeList(Document doc,String nodeName,Node node){
		List<Node> result = new ArrayList<Node>();
		if(nodeName == null || "".equals(nodeName))return result;
		getNodeList(doc,nodeName, result, node);
		return result;
	}
	
	/**
	 *<p>Title: getNodeList</p>
	 *<p>Description:递归获取节点</p>
	 * @param @param nodeName
	 * @param @param result
	 * @param @param node 设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private static void getNodeList(Document doc,String nodeName,List<Node> result,Node node){
		NodeList children;
		if(node == null){
			Element root = doc.getDocumentElement();
			children = root.getChildNodes();
		}else{
			if(node.getNodeName().equals(nodeName)){
				result.add(node);
				return;
			}
			children = node.getChildNodes();
		}
		if(children == null)return;
		for(int i=0;i<children.getLength();i++){
			getNodeList(doc,nodeName, result, children.item(i));
		}
	}
	


}

package com.bargetor.service.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import com.bargetor.service.common.config.configBeans.CommonConfigInfoBean;
import com.bargetor.service.common.config.exception.ConfigException;
import com.bargetor.service.common.util.ReflectUtil;
import com.bargetor.service.common.util.StringUtil;
import com.bargetor.service.common.util.XmlUtil;


/**
 * <p>description: 所有公共配置文件配置bean</p>
 * <p>Date: 2013-9-16 下午04:33:47</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class AllCommonConfigsLoader {
	private static final Logger logger = Logger.getLogger(AllCommonConfigsLoader.class);
	
	private Map<String,CommonConfigInfoBean> allCommonConfigInfos;
	
	private Map<String,Object> allCommonConfigs;
	
	private Document allCommonConfigInfosDoc = null;
	
	private static AllCommonConfigsLoader instance;
	
	protected AllCommonConfigsLoader(){
		init();
	}
	
	/**
	 *<p>Title: addConfigInfo</p>
	 *<p>Description:添加配置文件信息</p>
	 * @param @param id
	 * @param @param fileName
	 * @param @param className 设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void addConfigInfo(String id,String fileName,String className){
		addConfigInfo(new CommonConfigInfoBean(id,fileName, className));
	}
	
	/**
	 *<p>Title: addConfigInfo</p>
	 *<p>Description:添加配置文件信息</p>
	 * @param @param bean 设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void addConfigInfo(CommonConfigInfoBean bean){
		if(bean == null)return;
		if(StringUtil.isNullStr(bean.getId()) || StringUtil.isNullStr(bean.getFileName()) || StringUtil.isNullStr(bean.getClassName()))return;
		if(allCommonConfigInfos.containsKey(bean.getId()))allCommonConfigInfos.remove(bean.getId());
		allCommonConfigInfos.put(bean.getId(), bean);
		logger.info("增加配置：" + bean.toString());
	}
	
	/**
	 *<p>Title: getConfigBeanClassName</p>
	 *<p>Description:获取配置文件bean class name</p>
	 * @param @param id 配置文件名
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public String getConfigBeanClassName(String id){
		CommonConfigInfoBean bean = getConfigInfoBean(id);
		return bean == null ? null : bean.getClassName();
	}
	
	/**
	 *<p>Title: getConfigInfoBean</p>
	 *<p>Description:获取配置信息文件bean</p>
	 * @param @param id 配置文件名
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public CommonConfigInfoBean getConfigInfoBean(String id){
		return allCommonConfigInfos.get(id);
	}
	
	/**
	 *<p>Title: getConfigBean</p>
	 *<p>Description:获取配置bean</p>
	 * @param @param id 配置文件名
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public Object getConfigBean(String id){
		return allCommonConfigs.get(id);
	}
	
	/**
	 *<p>Title: getAllCommonConfigInfosMap</p>
	 *<p>Description:获取所有配置文件信息MAP</p>
	 * @param @return 设定文件
	 * @return  Map<String,String> 返回类型
	 * @throws
	*/
	public Map<String,CommonConfigInfoBean> getAllCommonConfigInfosMap(){
		return allCommonConfigInfos;
	}
	
	/**
	 *<p>Title: loadAllConfig</p>
	 *<p>Description:加载所有配置</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void loadAllConfig(){
		for(CommonConfigInfoBean bean : allCommonConfigInfos.values()){
			loadConfig(bean);
		}
	}
	
	
	/**
	 *<p>Title: loadConfig</p>
	 *<p>Description:加载配置</p>
	 * @param @param id 配置文件
	 * @return  void 返回类型
	 * @throws
	*/
	public void loadConfig(String id){
		CommonConfigInfoBean bean = allCommonConfigInfos.get(id);
		loadConfig(bean);
	}
	
	/**
	 *<p>Title: loadConfig</p>
	 *<p>Description:加载配置</p>
	 * @param @param bean 配置信息
	 * @return  void 返回类型
	 * @throws
	*/
	public void loadConfig(CommonConfigInfoBean bean){
		if(bean == null)return;
		if(StringUtil.isNullStr(bean.getFileName()))return;
		if(StringUtil.isNullStr(bean.getClassName()))return;
		Object config = loadConfig(bean.getFileName(), bean.getClassName());
		allCommonConfigs.remove(bean.getId());
		allCommonConfigs.put(bean.getId(), config);
		
	}
	
	/**
	 *<p>Title: loadConfig</p>
	 *<p>Description:加载配置</p>
	 * @param @param bean 配置信息
	 * @return  void 返回类型
	 * @throws
	*/
	public Object loadConfig(String fileName,String className){
		if(StringUtil.isNullStr(fileName))return null;
		if(StringUtil.isNullStr(className))return null;
		try {
			Properties pros = loadConfigFile(fileName);
			Object config = ReflectUtil.newInstance(className);
			String keyName = null;
			String configValue = null;
			for(Object key : pros.keySet()){
				keyName = key.toString();
				configValue = pros.getProperty(keyName);
				ReflectUtil.setProperty(config, keyName, configValue);
			}
			return config;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new ConfigException();
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new ConfigException();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new ConfigException();
		}
		
	}
	
	public static AllCommonConfigsLoader getInstance(){
		if(instance == null){
			instance = new AllCommonConfigsLoader();
		}
		return instance;
	}
	
	
	/*********************************************** private methods *************************************************/
	
	/**
	 *<p>Title: init</p>
	 *<p>Description:初始化</p>
	 * @return void 返回类型
	*/
	private void init(){
		if(allCommonConfigInfos == null){
			allCommonConfigInfos = new HashMap<String, CommonConfigInfoBean>();
		}
		
		if(allCommonConfigs == null){
			allCommonConfigs = new HashMap<String, Object>();
		}
		
		allCommonConfigInfosDoc = XmlUtil.initDocument("all-common-configs.xml");
		readConfigInfoBeans();
		closeDocument();
		loadAllConfig();
	}
	
	/**
	 *<p>Title: loadConfigFile</p>
	 *<p>Description:加载config文件</p>
	 * @param @param fileName
	 * @param @return 设定文件
	 * @return  Properties 返回类型
	 * @throws
	*/
	private Properties loadConfigFile(String fileName){
        InputStream path = AllCommonConfigsLoader.class.getClassLoader().getResourceAsStream(fileName);
        
        Properties pros = new Properties();
        try {
            pros.load(path);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return pros;
	}
	
	/**
	 *<p>Title: readConfigInfoBeans</p>
	 *<p>Description:读取配置信息</p>
	 * @param  设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private void readConfigInfoBeans(){
		List<Node> configNodeList = XmlUtil.getNodeList(allCommonConfigInfosDoc,"config");
		for(Node node : configNodeList){
			String id = node.getAttributes().getNamedItem("id").getNodeValue();
			String fileName = node.getAttributes().getNamedItem("fileName").getNodeValue();
			String className = node.getAttributes().getNamedItem("className").getNodeValue();
			addConfigInfo(id, fileName, className);
		}
	}
	
	/**
	 *<p>Title: closeDocument</p>
	 *<p>Description:关闭总配置文档</p>
	 * @param @throws OutBoundConfigException 设定文件
	 * @return  void 返回类型
	 * @throws
	*/
	private void closeDocument(){
		if(allCommonConfigInfosDoc == null)return;
		allCommonConfigInfosDoc = null;
	}
	

}

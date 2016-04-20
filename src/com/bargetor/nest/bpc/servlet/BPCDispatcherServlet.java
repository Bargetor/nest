package com.bargetor.nest.bpc.servlet;

import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bargetor.nest.bpc.annotation.BPCService;
import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCResponseBean;
import com.bargetor.nest.bpc.bean.BPCServiceProxyBean;
import com.bargetor.nest.bpc.filter.BPCFilter;
import com.bargetor.nest.bpc.handler.BPCExceptionHandler;
import com.bargetor.nest.bpc.handler.BPCRequestProcessHandler;
import com.bargetor.nest.bpc.handler.BPCReturnValueHandler;
import com.bargetor.nest.bpc.manager.BPCDispatchManager;
import com.bargetor.nest.common.bpc.BPCUtil;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.util.ArrayUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCDispatcherServlet extends HttpServlet implements InitializingBean, BeanFactoryPostProcessor, ApplicationContextAware {
	public static final String BPC_DEFAULT_URL = "/invoke";


	private static final Logger logger = Logger.getLogger(BPCDispatcherServlet.class);
	private ApplicationContext applicationContext;
	private BPCReturnValueHandler returnValueHandler;
	private BPCExceptionHandler exceptionHandler;
	private BPCRequestProcessHandler processHandler;

	private String[] scanPackages;
	private Set<BPCFilter> filterSet;

	private boolean isDebug = false;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.returnValueHandler == null){
			this.returnValueHandler = new BPCReturnValueHandler();
		}
		if(this.exceptionHandler == null){
			this.exceptionHandler = new BPCExceptionHandler();
		}


		this.processHandler = new BPCRequestProcessHandler();
		processHandler.setExceptionHandler(this.exceptionHandler);
		processHandler.setReturnValueHandler(this.returnValueHandler);

	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		super.service(req, res);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String httpMethod = req.getMethod();

		if(!BPCDispatchManager.getInstance().containsUrlMapping(req.getRequestURI().toString())){
			return;
		}

		//非POST方法不支持
		if(!"POST".equals(httpMethod)){
			resp.setStatus(405);
			return;
		}

		String requestBody = new String(StreamUtils.copyToByteArray(req.getInputStream()));
		BPCRequestBean requestBean = JSON.parseObject(requestBody, BPCRequestBean.class);

		//检查参数合法性
		if(!ParamCheckUtil.check(requestBean)){
			JSONObject invalidRequestJson = new JSONObject();
			JSONObject error = new JSONObject();
			error.put("message", "Invalid request");
			invalidRequestJson.put("error", error);
			BPCUtil.writeResponse(resp, invalidRequestJson.toJSONString());
			return;
		}

		//创建bpc request
		BPCRequest request = new BPCRequest(req, requestBean);

		//创建 bpc response
		BPCResponseBean responseBean = new BPCResponseBean();
		responseBean.setId(requestBean.getId());
		responseBean.setBpc(requestBean.getBpc());
		BPCResponse response = new BPCResponse(resp, responseBean);

		if(!this.doFilter(request, response))return;

		this.processHandler.process(request, response);

	}

	/**
	 * 在此,主要用于扫描和注册 BPC 服务
	 * @param beanFactory
	 * @throws BeansException
     */
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if(this.scanPackages == null || this.scanPackages.length == 0){
			logger.error("no bpc nest scan package");
		}

		SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(applicationContext);
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;

		for(String scanPackage : this.scanPackages){
			try {
				for(Resource resource : applicationContext.getResources(resolvePackageToScan(scanPackage))){
					MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
					ClassMetadata classMetadata = metadataReader.getClassMetadata();
					AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
					String bpcServiceAnnotationName = BPCService.class.getName();
					if(annotationMetadata.hasAnnotation(bpcServiceAnnotationName)){
						logger.info(String.format("bpc load nest %s", classMetadata.getClassName()));
						String url = (String) annotationMetadata.getAnnotationAttributes(bpcServiceAnnotationName).get("url");
						this.registerBPCService(defaultListableBeanFactory, classMetadata.getClassName(), url);
					}
				}
			}catch (Exception e){
				logger.error(String.format("bpc scan package {%s} error", scanPackage));
			}
		}
	}


	private void registerBPCService(DefaultListableBeanFactory defaultListableBeanFactory, String className, String url){
		//初始化proxy
		BeanDefinitionBuilder proxyBeanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(BPCServiceProxyBean.class);
		proxyBeanDefinitionBuilder.addPropertyValue("targetClassName", className);
		proxyBeanDefinitionBuilder.addPropertyValue("url", url);

		//init bpc nest
		logger.info(String.format("bpc start init nest {%s}", className));
		Class<?> targetClass = null;
		try {
			targetClass = ClassUtils.getDefaultClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			logger.error(String.format("bpc load nest class {%s} error", className), e);
			return;
		}
		BeanDefinitionBuilder targetBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClass);

		//binding target
		proxyBeanDefinitionBuilder.addPropertyValue("target", targetBeanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.registerBeanDefinition(className + "-bpc-nest-proxy", proxyBeanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.registerBeanDefinition(className, targetBeanDefinitionBuilder.getRawBeanDefinition());
	}


	private String resolvePackageToScan(String scanPackage) {
		return CLASSPATH_URL_PREFIX + convertClassNameToResourcePath(scanPackage) + "/**/*.class";
	}

	private boolean doFilter(BPCRequest request, BPCResponse response){
		if(request.getMethod().isTest() && this.isDebug)return true;
		if(ArrayUtil.isCollectionNull(this.filterSet))return true;
		for (BPCFilter filter: this.filterSet) {
			if(!filter.pass(request, response)){
				logger.info(String.format("bpc filter {%s} no pass -> %s",
						filter.getClass().getName(),
						request.getRequestBean())
				);
				return false;
			}
		}

		return true;
	}

	/****************************************** getter and setter *******************************************/

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public String[] getScanPackages() {
		return scanPackages;
	}

	public void setScanPackages(String[] scanPackages) {
		this.scanPackages = scanPackages;
	}

	public BPCReturnValueHandler getReturnValueHandler() {
		return returnValueHandler;
	}

	public void setReturnValueHandler(BPCReturnValueHandler returnValueHandler) {
		this.returnValueHandler = returnValueHandler;
	}

	public BPCExceptionHandler getExceptionHandler() {
		return exceptionHandler;
	}

	public void setExceptionHandler(BPCExceptionHandler exceptionHandler) {
		this.exceptionHandler = exceptionHandler;
	}

	public Set<BPCFilter> getFilterSet() {
		return filterSet;
	}

	public void setFilterSet(Set<BPCFilter> filterSet) {
		this.filterSet = filterSet;
	}

	public BPCRequestProcessHandler getProcessHandler() {
		return processHandler;
	}

	public void setProcessHandler(BPCRequestProcessHandler processHandler) {
		this.processHandler = processHandler;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean debug) {
		isDebug = debug;
	}
}

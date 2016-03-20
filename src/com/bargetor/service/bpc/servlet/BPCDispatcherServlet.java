package com.bargetor.service.bpc.servlet;

import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import com.alibaba.fastjson.JSON;
import com.bargetor.service.bpc.annotation.BPCService;
import com.bargetor.service.bpc.bean.BPCRequestBean;
import com.bargetor.service.bpc.bean.BPCServiceMethod;
import com.bargetor.service.bpc.bean.BPCServiceProxyBean;
import com.bargetor.service.bpc.handler.BPCExceptionHandler;
import com.bargetor.service.bpc.handler.BPCReturnValueHandler;
import com.bargetor.service.bpc.manager.BPCDispatchManager;
import com.bargetor.service.common.util.StringUtil;
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

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCDispatcherServlet extends HttpServlet implements InitializingBean, BeanFactoryPostProcessor, ApplicationContextAware {
	public static final String BPC_DEFAULT_URL = "/invoke";


	private static final Logger logger = Logger.getLogger(BPCDispatcherServlet.class);
	private ApplicationContext applicationContext;
	private BPCReturnValueHandler returnValueHandler;
	private BPCExceptionHandler exceptionHandler;

	private String[] scanPackages;

	@Override
	public void afterPropertiesSet() throws Exception {
		if(this.returnValueHandler == null){
			this.returnValueHandler = new BPCReturnValueHandler();
		}
		if(this.exceptionHandler == null){
			this.exceptionHandler = new BPCExceptionHandler();
		}
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
		requestBean.setId(StringUtil.getUUID());
		try {
			Object returnValue = this.invokeMethod(req.getRequestURI().toString(), requestBean);
			this.returnValueHandler.process(req, resp, requestBean, returnValue);
		}catch (Throwable e){
			this.exceptionHandler.process(req, resp, requestBean, e);
		}

	}

	private Object invokeMethod(String url, BPCRequestBean requestBean) throws Throwable {
		logger.info(String.format("bpc invoke method {%s}", requestBean.getMethod()));
		BPCServiceMethod method = BPCDispatchManager.getInstance().getServiceMethod(url, requestBean.getMethod());
		if(method == null)return null;
		return method.invoke(requestBean);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		if(this.scanPackages == null || this.scanPackages.length == 0){
			logger.error("no bpc service scan package");
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
						logger.info(String.format("bpc load service %s", classMetadata.getClassName()));
						String url = (String) annotationMetadata.getAnnotationAttributes(bpcServiceAnnotationName).get("url");
						this.registerBPCService((DefaultListableBeanFactory)beanFactory, classMetadata.getClassName(), url);
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

		//init bpc service
		logger.info(String.format("bpc start init service {%s}", className));
		Class<?> targetClass = null;
		try {
			targetClass = ClassUtils.getDefaultClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			logger.error(String.format("bpc load service class {%s} error", className), e);
			return;
		}
		BeanDefinitionBuilder targetBeanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(targetClass);

		//binding target
		proxyBeanDefinitionBuilder.addPropertyValue("target", targetBeanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.registerBeanDefinition(className + "-bpc-service-proxy", proxyBeanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.registerBeanDefinition(className, targetBeanDefinitionBuilder.getRawBeanDefinition());
	}


	private String resolvePackageToScan(String scanPackage) {
		return CLASSPATH_URL_PREFIX + convertClassNameToResourcePath(scanPackage) + "/**/*.class";
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
}

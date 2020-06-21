package com.bargetor.nest.bpc.servlet;

import static org.springframework.util.ClassUtils.convertClassNameToResourcePath;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;

import com.alibaba.fastjson.JSON;
import com.bargetor.nest.bpc.annotation.BPCService;
import com.bargetor.nest.bpc.bean.BPCRequestBean;
import com.bargetor.nest.bpc.bean.BPCRequestMetaBean;
import com.bargetor.nest.bpc.bean.BPCResponseBean;
import com.bargetor.nest.bpc.bean.BPCServiceProxyBean;
import com.bargetor.nest.bpc.exception.BPCMetaParamInvalidException;
import com.bargetor.nest.bpc.exception.BPCMethodNotFoundException;
import com.bargetor.nest.bpc.filter.BPCFilter;
import com.bargetor.nest.bpc.handler.BPCExceptionHandler;
import com.bargetor.nest.bpc.handler.BPCRequestProcessHandler;
import com.bargetor.nest.bpc.handler.BPCReturnValueHandler;
import com.bargetor.nest.bpc.manager.BPCDispatchManager;
import com.bargetor.nest.common.check.param.ParamCheckUtil;
import com.bargetor.nest.common.executor.ExecutorManager;
import com.bargetor.nest.common.springmvc.SpringApplicationUtil;
import com.bargetor.nest.common.util.ArrayUtil;
import com.bargetor.nest.common.util.StringUtil;
import com.bargetor.nest.influxdb.InfluxDBManager;
import com.bargetor.nest.influxdb.InfluxDBManagerImpl;
import org.apache.log4j.Logger;
import org.influxdb.dto.Point;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.concurrent.TimeUnit;

/**
 * Created by Bargetor on 16/3/20.
 */
public class BPCDispatcherServlet extends HttpServlet implements InitializingBean, BeanFactoryPostProcessor, ApplicationContextAware {
	public static final String BPC_DEFAULT_URL_PATTERN = "/invoke/**";
	public static final String BPC_INFLUXDB_INVOKE_SUCCESS_TAG = "bpc_invoke_is_success";

	private static final Logger logger = Logger.getLogger(BPCDispatcherServlet.class);
	private ApplicationContext applicationContext;
	private BPCReturnValueHandler returnValueHandler;
	private BPCExceptionHandler exceptionHandler;
	private BPCRequestProcessHandler processHandler;

	private String[] scanPackages;
	private Set<BPCFilter> filterSet;

	private boolean isDebug = false;
	private String debugToken;

	@Autowired
	private InfluxDBManager influxDBManager;
	private String pointMeasurement = "bpc";

	@Override
	public void afterPropertiesSet() {
		if(this.returnValueHandler == null){
			this.returnValueHandler = new BPCReturnValueHandler();
		}
		if(this.exceptionHandler == null){
			this.exceptionHandler = new BPCExceptionHandler();
		}

		this.processHandler = new BPCRequestProcessHandler();
	}

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		super.service(req, res);
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String httpMethod = req.getMethod();

		if(!BPCDispatchManager.getInstance().containsMappingForUrl(req.getRequestURI().toString())){
			resp.setStatus(403);
			return;
		}

		//非POST方法不支持
		if(!"POST".equals(httpMethod)){
			resp.setStatus(405);
			return;
		}

		//创建bpc request
		String requestBody = new String(StreamUtils.copyToByteArray(req.getInputStream()));
		BPCRequestBean requestBean = JSON.parseObject(requestBody, BPCRequestBean.class);
		BPCRequest bpcRequest = new BPCRequest(req, requestBean);

		//创建 bpc response
		BPCResponseBean responseBean = new BPCResponseBean();
		responseBean.setId(requestBean.getId());
		responseBean.setBpc(requestBean.getBpc());
		BPCResponse bpcResponse = new BPCResponse(resp, responseBean);

		long bpcStartTime = System.currentTimeMillis();
		try {
			this.serviceExecutor(req, resp, bpcRequest, bpcResponse);

			//调用成功打点
			this.point(bpcRequest, bpcStartTime, true);
		}catch (Throwable e){
			logger.error(String.format("method process error and request body is %s", requestBody), e);
			this.exceptionHandler.process(bpcRequest, bpcResponse, e);

			//调用失败打点
			this.point(bpcRequest, bpcStartTime, false);
		}

	}

	protected void serviceExecutor(HttpServletRequest req, HttpServletResponse resp, BPCRequest bpcRequest, BPCResponse bpcResponse) throws Throwable {
		if(bpcRequest == null || bpcResponse == null)return;

		//检查参数合法性
		if(!ParamCheckUtil.check(bpcRequest.getRequestBean()))
			throw new BPCMetaParamInvalidException();

		//检查request
		if(bpcRequest.getMethod() == null)
			throw new BPCMethodNotFoundException(bpcRequest.getRequestBean().getMethod());


		if(!this.doFilter(bpcRequest, bpcResponse))return;

		Object returnValue = this.processHandler.process(bpcRequest, bpcResponse);

		this.returnValueHandler.process(bpcRequest, bpcResponse, returnValue);
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
						logger.info(String.format("bpc load service %s", classMetadata.getClassName()));
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
		defaultListableBeanFactory.registerBeanDefinition(className + "-bpc-nest-proxy", proxyBeanDefinitionBuilder.getBeanDefinition());
		defaultListableBeanFactory.registerBeanDefinition(className, targetBeanDefinitionBuilder.getRawBeanDefinition());
	}


	private String resolvePackageToScan(String scanPackage) {
		return CLASSPATH_URL_PREFIX + convertClassNameToResourcePath(scanPackage) + "/**/*.class";
	}

	private boolean doFilter(BPCRequest request, BPCResponse response){
		if (request.getMethod().isTest()){
			if (!this.isDebug) {
				logger.info("test method must use in debug mode");
				return false;
			}
			BPCRequestMetaBean metaBean = request.getRequestBean().getMeta();
			if (metaBean == null) {
				logger.info("debug token miss");
				return false;
			}
			String requestDebugToken = metaBean.getToken();
			if (StringUtil.isNullStr(requestDebugToken)) {
				logger.info("debug token miss");
				return false;
			}
			if (requestDebugToken.equals(this.debugToken)){
				return true;
			}else{
				logger.info("debug token miss");
				return false;
			}
		}

		if(ArrayUtil.isNull(this.filterSet))return true;
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

	/**
	 * 记录调用打点
	 * @param request
	 * @param isSuccess 调用是否成功
	 */
	private void point(BPCRequest request, long bpcStartTime, boolean isSuccess){
		if (this.influxDBManager == null) this.influxDBManager = (InfluxDBManager) SpringApplicationUtil.getBean(InfluxDBManager.class);
		ExecutorManager.getInstance().commitRunnable(() -> {
			if(request == null)return;
			if(request.getMethod() == null)return;
			Point point = Point.measurement(this.pointMeasurement)
					.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
					.addField(request.getMethod().getMethodName(), isSuccess)
					.addField(request.getMethod().getMethodName() + "_invoke_time", System.currentTimeMillis() - bpcStartTime)
					.tag(BPC_INFLUXDB_INVOKE_SUCCESS_TAG, new Boolean(isSuccess).toString())
					.build();

			this.influxDBManager.writePoint(point);
		});
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

	public InfluxDBManager getInfluxDBManager() {
		return influxDBManager;
	}

	public void setInfluxDBManager(InfluxDBManager influxDBManager) {
		this.influxDBManager = influxDBManager;
	}

	public String getPointMeasurement() {
		return pointMeasurement;
	}

	public void setPointMeasurement(String pointMeasurement) {
		this.pointMeasurement = pointMeasurement;
	}

	public String getDebugToken() {
		return debugToken;
	}

	public void setDebugToken(String debugToken) {
		this.debugToken = debugToken;
	}
}

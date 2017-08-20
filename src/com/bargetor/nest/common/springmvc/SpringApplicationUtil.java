/**
 * bargetorCommon
 * com.bargetor.nest.common.springmvc
 * SpringApplicationUtil.java
 * 
 * 2015年4月26日-下午12:54:23
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.springmvc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * SpringApplicationUtil
 * 
 * kin kin 2015年4月26日 下午12:54:23
 * 
 * @version 1.0.0
 *
 */
public class SpringApplicationUtil implements ApplicationContextAware{

	public static ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		SpringApplicationUtil.applicationContext = applicationContext;
	}

	public static Object getBean(String name) {
		return applicationContext.getBean(name);
	}

	public static Object getBean(String name, Object... args){
		return applicationContext.getBean(name, args);
	}
	
	public static Object getBean(Class<?> clazz) {
		Object result = null;
		try {
			result = applicationContext.getBean(clazz);
		}catch (Exception e){
			result = null;
		}
		return result;
	}

	public static Object getBean(Class<?> clazz, Object... args) {
		Object result = null;
		try {
			result = applicationContext.getBean(clazz, args);
		}catch (Exception e){
			result = null;
		}
		return result;
	}

	public static <T>Map<String, T> getBeans(Class<T> clazz){
		Map<String, T> result = null;
		try {
			result = applicationContext.getBeansOfType(clazz);
		}catch (Exception e){}

		return result;
	}

	public static List<ClassAnnotationInfo> scanAnnotation(
			ApplicationContext applicationContext,
			Class<?> annotationClass,
			String scanPackage) throws IOException {
		SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(applicationContext);

		List<ClassAnnotationInfo> annotationInfos = new ArrayList<>();
		for(Resource resource : applicationContext.getResources(resolvePackageToScan(scanPackage))){
			MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
			ClassMetadata classMetadata = metadataReader.getClassMetadata();
			AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
			String annotationName = annotationClass.getName();
			if(annotationMetadata.hasAnnotation(annotationName)){
				Map<String, Object> annotationAttributes = annotationMetadata.getAnnotationAttributes(annotationName);
				ClassAnnotationInfo annotationInfo = new ClassAnnotationInfo(classMetadata, annotationAttributes);
				annotationInfos.add(annotationInfo);
			}
		}

		return annotationInfos;
	}

	public static List<MethodAnnotationInfo> scanMethodAnnotation(
			ApplicationContext applicationContext,
			Class<?> annotationClass,
			String scanPackage) throws IOException {
		SimpleMetadataReaderFactory metadataReaderFactory = new SimpleMetadataReaderFactory(applicationContext);

		List<MethodAnnotationInfo> methodAnnotationInfos = new ArrayList<>();
		for(Resource resource : applicationContext.getResources(resolvePackageToScan(scanPackage))){
			MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
			ClassMetadata classMetadata = metadataReader.getClassMetadata();
			AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
			String annotationName = annotationClass.getName();
			if(annotationMetadata.hasAnnotatedMethods(annotationName)){
				for (MethodMetadata methodMetadata : annotationMetadata.getAnnotatedMethods(annotationName)) {
					Map<String, Object> annotationAttributes = methodMetadata.getAnnotationAttributes(annotationName);
					MethodAnnotationInfo annotationInfo = new MethodAnnotationInfo(classMetadata, methodMetadata, annotationAttributes);
					methodAnnotationInfos.add(annotationInfo);
				}
			}
		}

		return methodAnnotationInfos;
	}

	private static String resolvePackageToScan(String scanPackage){
		return ResourceUtils.CLASSPATH_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(scanPackage) + "/**/*.class";

	}

	public static class ClassAnnotationInfo {
		private ClassMetadata classMetadata;
		private Map<String, Object> annotationAttributes;

		public ClassAnnotationInfo(ClassMetadata classMetadata, Map<String, Object> annotationAttributes) {
			this.classMetadata = classMetadata;
			this.annotationAttributes = annotationAttributes;
		}

		public ClassMetadata getClassMetadata() {
			return classMetadata;
		}

		public void setClassMetadata(ClassMetadata classMetadata) {
			this.classMetadata = classMetadata;
		}

		public Map<String, Object> getAnnotationAttributes() {
			return annotationAttributes;
		}

		public void setAnnotationAttributes(Map<String, Object> annotationAttributes) {
			this.annotationAttributes = annotationAttributes;
		}
	}

	public static class MethodAnnotationInfo {
		private ClassMetadata classMetadata;
		private MethodMetadata methodMetadata;
		private Map<String, Object> annotationAttributes;

		public MethodAnnotationInfo(ClassMetadata classMetadata, MethodMetadata methodMetadata, Map<String, Object> annotationAttributes) {
			this.classMetadata = classMetadata;
			this.methodMetadata = methodMetadata;
			this.annotationAttributes = annotationAttributes;
		}

		public ClassMetadata getClassMetadata() {
			return classMetadata;
		}

		public void setClassMetadata(ClassMetadata classMetadata) {
			this.classMetadata = classMetadata;
		}

		public MethodMetadata getMethodMetadata() {
			return methodMetadata;
		}

		public void setMethodMetadata(MethodMetadata methodMetadata) {
			this.methodMetadata = methodMetadata;
		}

		public Map<String, Object> getAnnotationAttributes() {
			return annotationAttributes;
		}

		public void setAnnotationAttributes(Map<String, Object> annotationAttributes) {
			this.annotationAttributes = annotationAttributes;
		}
	}
}

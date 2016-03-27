package com.bargetor.nest.common.servlet.beans;

public class ServletProcessorConfigBean {
	
	private String id;
	
	private String urlPattern;
	
	private String processorName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(String urlPattern) {
		this.urlPattern = urlPattern;
	}

	public String getProcessorName() {
		return processorName;
	}

	public void setProcessorName(String processorName) {
		this.processorName = processorName;
	}

	public ServletProcessorConfigBean(String id, String urlPattern,String processorName) {
		super();
		this.id = id;
		this.urlPattern = urlPattern;
		this.processorName = processorName;
	}

	public String toString() {
		return "[id=" + id + ", processorName="
				+ processorName + ", urlPattern=" + urlPattern + "]";
	}
	
	
	
	
}

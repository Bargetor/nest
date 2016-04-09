/**
 * Migrant
 * com.bargetor.migrant.model.task
 * TaskError.java
 * 
 * 2015年6月22日-下午7:42:10
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.task.bean;

import java.math.BigInteger;
import java.util.Date;

/**
 *
 * TaskError
 * 
 * kin
 * kin
 * 2015年6月22日 下午7:42:10
 * 
 * @version 1.0.0
 *
 */
public class Task {

	private BigInteger taskId;
	private String type;
	private String attributeJson;
	private String errorJson;
	private String status;
	private String tag;
	private Date createTime;
	private Date updateTime;

	public BigInteger getTaskId() {
		return taskId;
	}

	public void setTaskId(BigInteger taskId) {
		this.taskId = taskId;
	}

	/**
	 * type
	 *
	 * @return  the type
	 * @since   1.0.0
	 */
	
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public String getAttributeJson() {
		return attributeJson;
	}

	public void setAttributeJson(String attributeJson) {
		this.attributeJson = attributeJson;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * status
	 *
	 * @return  the status
	 * @since   1.0.0
	 */
	
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * createTime
	 *
	 * @return  the createTime
	 * @since   1.0.0
	 */
	
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	/**
	 * tag
	 *
	 * @return  the tag
	 * @since   1.0.0
	 */
	
	public String getTag() {
		return tag;
	}
	/**
	 * @param tag the tag to set
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getErrorJson() {
		return errorJson;
	}

	public void setErrorJson(String errorJson) {
		this.errorJson = errorJson;
	}

	public enum TaskStatus{
		created, running, done, error
	}
}

/**
 * bargetorCommon
 * com.bargetor.service.common.bpc.version
 * BCPApiVesrsionCondition.java
 * 
 * 2015年6月16日-下午10:24:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.bpc.version;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.mvc.condition.RequestCondition;

import com.bargetor.service.common.util.StringUtil;

/**
 *
 * BCPApiVesrsionCondition
 * 
 * kin
 * kin
 * 2015年6月16日 下午10:24:45
 * 
 * @version 1.0.0
 *
 */
public class BPCAPIVersionCondition implements RequestCondition<BPCAPIVersionCondition>{
	public static final String BCP_API_VERSION_HEADER_NAME = "api";
	
	private int apiVersion;
    
    public BPCAPIVersionCondition(int apiVersion){
        this.apiVersion = apiVersion;
    }
	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.condition.RequestCondition#combine(java.lang.Object)
	 */
	@Override
	public BPCAPIVersionCondition combine(BPCAPIVersionCondition other) {
		return new BPCAPIVersionCondition(other.getApiVersion());
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.condition.RequestCondition#getMatchingCondition(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public BPCAPIVersionCondition getMatchingCondition(
			HttpServletRequest request) {
		String versionStr = request.getHeader(BPCAPIVersionCondition.BCP_API_VERSION_HEADER_NAME);
		if(StringUtil.isNullStr(versionStr))return null;
		int version = Integer.valueOf(versionStr);
		if(version >= this.apiVersion){
			return this;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.condition.RequestCondition#compareTo(java.lang.Object, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public int compareTo(BPCAPIVersionCondition other,
			HttpServletRequest request) {
		return other.getApiVersion() - this.apiVersion;
	}


	/**
	 * apiVersion
	 *
	 * @return  the apiVersion
	 * @since   1.0.0
	 */
	
	public int getApiVersion() {
		return apiVersion;
	}

}

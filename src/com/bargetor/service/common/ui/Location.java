/**
 * bargetorCommon
 * com.bargetor.service.common.ui
 * Location.java
 * 
 * 2015年7月22日-下午9:55:00
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.ui;

/**
 *
 * Location
 * 
 * kin
 * kin
 * 2015年7月22日 下午9:55:00
 * 
 * @version 1.0.0
 *
 */
public class Location {
	private double lng;
	private double lat;
	/**
	 * 创建一个新的实例 Location.
	 *
	 */
	public Location() {
		// TODO Auto-generated constructor stub
	}
	
	public Location(double lng, double lat) {
		this.lng = lng;
		this.lat = lat;
	}
	/**
	 * lng
	 *
	 * @return  the lng
	 * @since   1.0.0
	 */
	
	public double getLng() {
		return lng;
	}
	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}
	/**
	 * lat
	 *
	 * @return  the lat
	 * @since   1.0.0
	 */
	
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return lat + "," + lng;
	}
	
}

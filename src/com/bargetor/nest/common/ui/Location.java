/**
 * bargetorCommon
 * com.bargetor.nest.common.ui
 * Location.java
 * 
 * 2015年7月22日-下午9:55:00
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.ui;

import com.alibaba.fastjson.annotation.JSONField;
import com.bargetor.nest.common.geometry.Point;
import com.bargetor.nest.common.util.CoordTransformUtil;

import java.io.Serializable;

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
public class Location implements Serializable {
	@JSONField(serialize = false, deserialize = false)
	private Type type = Type.GCJ02;
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
		return this.toLngLat();
	}

	public String toLatLng(){
		return lat + "," + lng;
	}

	public String toLngLat(){
		return lng + "," + lat;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Location to(Type type){
		switch (this.type){
			case WGS84:
				switch (type){
					case WGS84:
						return this;
					case GCJ02:
						return CoordTransformUtil.wgs84Togcj02(this.lng, this.lat);
					case BD09:
						Location gcj02 = CoordTransformUtil.wgs84Togcj02(this.lng, this.lat);
						return CoordTransformUtil.gcj02Tobd09(gcj02.getLng(), gcj02.getLat());
				}
			case GCJ02:
				switch (type){
					case WGS84:
						return CoordTransformUtil.gcj02Towgs84(this.lng, this.lat);
					case GCJ02:
						return this;
					case BD09:
						return CoordTransformUtil.gcj02Tobd09(this.lng, this.lat);
				}
			case BD09:
				switch (type){
					case WGS84:
						Location gcj02 = CoordTransformUtil.bd09Togcj02(this.lng, this.lat);
						return CoordTransformUtil.gcj02Towgs84(gcj02.getLng(), gcj02.getLat());
					case GCJ02:
						return CoordTransformUtil.bd09Togcj02(this.lng, this.lat);
					case BD09:
						return this;
				}
		}
		return this;
	}

	public String toWKTString(){
		return String.format("Point(%s %s)", this.lng, this.lat);
	}

	public Point toPoint(){
		return new Point(this.lng, this.lat);
	}

	public enum Type{
		WGS84,
		GCJ02,
		BD09
	}

	public static void main(String[] args){
		Location location = new Location(116.403882, 39.914873);
		location.setType(Type.BD09);

		System.out.println("the original location is:" + location.toString());
		System.out.println("the transfrom location is: " + location.to(Type.GCJ02));
	}
}

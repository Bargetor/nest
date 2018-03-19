/**
 * bargetorCommon
 * com.bargetor.nest.common.ui
 * Point.java
 * 
 * 2015年7月16日-下午11:08:55
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.geometry;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

/**
 *
 * Point
 * 
 * kin
 * kin
 * 2015年7月16日 下午11:08:55
 * 
 * @version 1.0.0
 *
 */
public class Point extends Geometry<Coordinate>{

	public static Point zero(){
		return new Point(0, 0);
	}

	public Point(){
		this.setCoordinates(new Coordinate());
	}

	public Point(Coordinate coordinate){
		this(coordinate.getX(), coordinate.getY());
	}
	
	/**
	 * 创建一个新的实例 Point.
	 *
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		this.type = Type.Point;
		this.coordinates = new Coordinate(x, y);
	}
	/**
	 * x
	 *
	 * @return  the x
	 * @since   1.0.0
	 */
	@JSONField(serialize = false)
	public double getX() {
		return this.coordinates.getX();
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.coordinates.setX(x);
	}
	/**
	 * y
	 *
	 * @return  the y
	 * @since   1.0.0
	 */
	@JSONField(serialize = false)
	public double getY() {
		return this.coordinates.getY();
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.coordinates.setY(y);
	}

	@Override
	public String toString() {
		return this.coordinates.toString();
	}

	public static void main(String[] args){
		Point point = new Point();
		point.setX(1);
		System.out.println(JSON.toJSONString(point));

		Point dePoint = JSON.parseObject(JSON.toJSONString(point), Point.class);
		System.out.println(dePoint);
	}
}

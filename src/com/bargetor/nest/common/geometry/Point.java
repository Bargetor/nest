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

	public Point(){
		this(0, 0);
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
	
	public double getY() {
		return this.coordinates.getY();
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.setY(y);
	}


	public static void main(String[] args){
		Point point = new Point();
		point.setX(1);
		System.out.println(JSON.toJSONString(point));
	}
}

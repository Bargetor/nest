/**
 * bargetorCommon
 * com.bargetor.service.common.ui
 * Point.java
 * 
 * 2015年7月16日-下午11:08:55
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.ui;

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
public class Point {
	
	private double x;
	private double y;
	
	
	
	
	/**
	 * 创建一个新的实例 Point.
	 *
	 * @param x
	 * @param y
	 */
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	/**
	 * x
	 *
	 * @return  the x
	 * @since   1.0.0
	 */
	
	public double getX() {
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * y
	 *
	 * @return  the y
	 * @since   1.0.0
	 */
	
	public double getY() {
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
	
	

}

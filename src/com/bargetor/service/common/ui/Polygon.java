/**
 * bargetorCommon
 * com.bargetor.service.common.ui
 * Polygon.java
 * 
 * 2015年7月16日-下午11:10:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/**
 *
 * Polygon
 * 
 * kin
 * kin
 * 2015年7月16日 下午11:10:45
 * 
 * @version 1.0.0
 *
 */
public class Polygon {

	private List<Point> points = new ArrayList<Point>();
	/**
	 * points
	 *
	 * @return  the points
	 * @since   1.0.0
	 */
	
	public List<Point> getPoints() {
		return points;
	}

	/**
	 * clearAllPoints(清除所有点)
	 * (这里描述这个方法适用条件 – 可选)
	 * void
	 * @exception
	 * @since  1.0.0
	*/
	public void clearAllPoints(){
		this.points.clear();
	}
	
	public void addPoint(Point point){
		this.points.add(point);
	}
	
	public void addPoints(Collection<Point> points){
		this.points.addAll(points);
	}
	
	/**
	 * isPolygon(判断是否为多边形)
	 * 没有3个点，不构成多边形
	 * @return
	 * boolean
	 * @exception
	 * @since  1.0.0
	*/
	public boolean isPolygon(){
		return this.points.size() >= 3;
	}

}

/**
 * bargetorCommon
 * com.bargetor.nest.common.ui
 * Polygon.java
 * 
 * 2015年7月16日-下午11:10:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.ui;

import com.bargetor.nest.common.util.ArrayUtil;

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


	public double getMaxX(){
		double max = Double.MIN_VALUE;
		for(Point point : this.points){
			max = Math.max(max, point.getX());
		}
		return max;
	}

	public double getMaxY(){
		double max = Double.MIN_VALUE;
		for(Point point : this.points){
			max = Math.max(max, point.getY());
		}
		return max;
	}

	public double getMinX(){
		double min = Double.MAX_VALUE;
		for(Point point : this.points){
			min = Math.min(min, point.getX());
		}
		return min;
	}

	public double getMinY(){
		double min = Double.MAX_VALUE;
		for(Point point : this.points){
			min = Math.min(min, point.getY());
		}
		return min;
	}

	public Point getCenter(){
		if(ArrayUtil.isCollectionNull(this.points))return null;
		double totalx = 0;
		double totaly = 0;
		int count = this.points.size();

		for (Point point : this.points) {
			totalx += point.getX();
			totaly += point.getY();
		}

		return new Point(totalx / count, totaly / count);
	}

}

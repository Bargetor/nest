/**
 * bargetorCommon
 * com.bargetor.nest.common.ui
 * Polygon.java
 * 
 * 2015年7月16日-下午11:10:45
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.geometry;

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
public class Polygon extends Geometry<List<Coordinate>> {

	public Polygon(){
		this.type = Type.Polygon;
		this.coordinates = new ArrayList<>();
	}

	/**
	 * points
	 *
	 * @return  the points
	 * @since   1.0.0
	 */
	
	public List<Point> getPoints() {
		return ArrayUtil.list2List(this.coordinates, coordinate -> new Point(coordinate));
	}

	/**
	 * clearAllPoints(清除所有点)
	 * (这里描述这个方法适用条件 – 可选)
	 * void
	 * @exception
	 * @since  1.0.0
	*/
	public void clearAllPoints(){
		this.coordinates.clear();
	}
	
	public void addPoint(Point point){
		this.coordinates.add(point.getCoordinates());
	}
	
	public void addPoints(Collection<Point> points){
		if(ArrayUtil.isNull(points))return;
		points.forEach(point -> this.addPoint(point));
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
		return this.coordinates.size() >= 3;
	}


	public double getMaxX(){
		double max = Double.MIN_VALUE;
		for(Coordinate coordinate : this.coordinates){
			max = Math.max(max, coordinate.getX());
		}
		return max;
	}

	public double getMaxY(){
		double max = Double.MIN_VALUE;
		for(Coordinate coordinate : this.coordinates){
			max = Math.max(max, coordinate.getY());
		}
		return max;
	}

	public double getMinX(){
		double min = Double.MAX_VALUE;
		for(Coordinate coordinate : this.coordinates){
			min = Math.min(min, coordinate.getX());
		}
		return min;
	}

	public double getMinY(){
		double min = Double.MAX_VALUE;
		for(Coordinate coordinate : this.coordinates){
			min = Math.min(min, coordinate.getY());
		}
		return min;
	}

	public Point getCenter(){
		if(ArrayUtil.isNull(this.coordinates))return null;
		double totalX = 0;
		double totalY = 0;
		int count = this.coordinates.size();

		for (Coordinate coordinate : this.coordinates) {
			totalX += coordinate.getX();
			totalY += coordinate.getY();
		}

		return new Point(totalX / count, totalY / count);
	}

}

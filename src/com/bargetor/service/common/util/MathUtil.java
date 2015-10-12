/**
 * bargetorCommon
 * com.bargetor.service.common.util
 * MathUtil.java
 * 
 * 2015年7月23日-下午11:36:47
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.util;

import com.bargetor.service.common.ui.Location;

/**
 *
 * MathUtil
 * 定义一些数学上的工具
 * kin
 * kin
 * 2015年7月23日 下午11:36:47
 * 
 * @version 1.0.0
 *
 */
public class MathUtil {

	
	/**
	 * EARTH_RADIUS:地球半径，单位KM
	 *
	 * @since 1.0.0
	 */
	public static double EARTH_RADIUS = 6378.137;
	
	
	/**
	 * rad(计算弧度)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param d
	 * @return
	 * double
	 * @exception
	 * @since  1.0.0
	*/
	private static double rad(double d){
		return d * Math.PI / 180;
	}
	
	/**
	 * calcDistance(计算地球上两点距离,单位M)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param location1
	 * @param location2
	 * @return
	 * double
	 * @exception
	 * @since  1.0.0
	*/
	public static double calcDistance(Location location1, Location location2) {
		double radLat1 = rad(location1.getLat());
		double radLat2 = rad(location2.getLat());
		double a = radLat1 - radLat2;
		double b = rad(location1.getLng()) - rad(location2.getLng());

		return 2* Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
						+ Math.cos(radLat1) * Math.cos(radLat2)
						* Math.pow(Math.sin(b / 2), 2))) * EARTH_RADIUS * 1000;
	}
	
	
	public static void main(String[] args) {
        
        Location location1 = new Location(121.396884, 31.205736);
        Location location2 = new Location(121.940815, 30.897403);
        long startTime = System.currentTimeMillis();
        System.out.println(calcDistance(location1, location2));
        System.out.println(System.currentTimeMillis() - startTime);
	}
	
	
}

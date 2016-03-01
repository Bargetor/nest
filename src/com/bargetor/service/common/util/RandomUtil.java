/**
 * Migrant
 * com.bargetor.migrant.util
 * Ramdom.java
 * 
 * 2015年2月8日-下午6:29:27
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.service.common.util;

import java.util.Random;

/**
 *
 * RamdomUtil
 * 
 * kin
 * kin
 * 2015年2月8日 下午6:29:27
 * 
 * @version 1.0.0
 *
 */
public class RandomUtil {
	
	/**
	 * randomIntByInterval(获取指定区间的随机数)
	 * 会出现最小值,不会出现最大值
	 * @param min
	 * @param max
	 * @return
	 *int
	 * @exception
	 * @since  1.0.0
	*/
	public static int randomIntByInterval(int min, int max){
		Random random = new Random();
		return random.nextInt(max) % (max - min + 1) + min;
	}
	
	public static double random(){
		Random random = new Random();
		return random.nextDouble();
	}

	public static void main(String[] args){
		System.out.println(randomIntByInterval(0, 9));
	}

}

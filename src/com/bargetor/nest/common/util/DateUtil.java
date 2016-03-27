package com.bargetor.nest.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * <p>description: 日期</p>
 * <p>Date: 2012-5-29 下午04:10:43</p>
 * <p>modify：</p>
 * @author: majin
 * @version: 1.0
 */
public class DateUtil {
	public static final String timeFormatStr = "yyyy-MM-dd HH:mm:ss";
	public static final String oldStyleFormatStr = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatStr);
	public static final Calendar cal = Calendar.getInstance();
	

	public static String getStr(String formatStr, Date date){
		SimpleDateFormat timeFormat = new SimpleDateFormat(formatStr);
		return timeFormat.format(date);
	}

	/**
	 *<p>Title: getNow</p>
	 *<p>Description:得到当前时间</p>
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public static String getNow(){
		return timeFormat.format(new Date());
	}
	
	/**
	 *<p>Title: getNowToLate</p>
	 *<p>Description:获取当前时间之后ms秒的时间</p>
	 * @param @param ms
	 * @param @return 
	 * @return  String 
	 * @throws
	*/
	public static String getNowToLate(long ms){
		return timeFormat.format(new Date(System.currentTimeMillis() + ms));
	}
	
	/**
	 *<p>Title: dateToString</p>
	 *<p>Description:日期转字符串</p>
	 * @param @param time
	 * @param @return 
	 * @return  String 
	 * @throws
	*/
	public static String dateToString(long time){
		return dateToString(new Date(time));  
	}
	
	/**
	 *<p>Title: dateToString</p>
	 *<p>Description:日期转字符串</p>
	 * @param @param date
	 * @param @return 
	 * @return  String 
	 * @throws
	*/
	public static String dateToString(Date date){
		return timeFormat.format(date);
	}
	
	/**
	 *<p>Title: getStart</p>
	 *<p>Description:获取一天的开始</p>
	 * @param @param date
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public static String getStart(Date date){
		return new SimpleDateFormat("yyyy-MM-dd 00:00:00").format(date);
	}

	public static Date getTodayStart(){
		String startStr = getStart(new Date());
		try {
			return timeFormat.parse(startStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *<p>Title: getEnd</p>
	 *<p>Description:获取一天的结束</p>
	 * @param @param date
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public static String getEnd(Date date){
		return new SimpleDateFormat("yyyy-MM-dd 23:59:59").format(date);
	}
	
	/**
	 *<p>Title: getWeek</p>
	 *<p>Description:得到星期</p>
	 * @param @return 设定文件
	 * @return  int 返回类型
	 * @throws
	*/
	public static int getWeek(){
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return week == 0 ? 7:week;
	}
	
	/**
	 *<p>Title: includeTime</p>
	 *<p>Description:时间段内是否包含当前时间</p>
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return 设定文件
	 * @return  boolean 返回类型
	 * @throws
	*/
	public static boolean includeTime(String startTime,String endTime){
		return includeTime(startTime, getNow(), endTime);
	}
	
	/**
	 *<p>Title: includeTime</p>
	 *<p>Description:时间段内是否包含时间</p>
	 * @param @param startTime
	 * @param @param includeTime
	 * @param @param endTime
	 * @param @return 设定文件
	 * @return  boolean 返回类型
	 * @throws
	*/
	public static boolean includeTime(String startTime,String includeTime,String endTime){
		if(startTime == null || includeTime == null || endTime == null)return false;
		return includeTime.compareTo(startTime)>=0 && endTime.compareTo(includeTime)>=0;
	}
	
	/**
	 *<p>Title: timeLack</p>
	 *<p>Description:计算时间差(ms)</p>
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return 设定文件
	 * @return  long 返回类型
	 * @throws
	*/
	public static long timeLack(String startTime,String endTime){
		if(startTime == null || "".equals(startTime))return 0;
		if(endTime == null || "".equals(endTime))return 0;
		try {
			return timeFormat.parse(endTime).getTime() - timeFormat.parse(startTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}

	}

	public static Date strToDate(String dateStr){
		if(StringUtil.isNullStr(dateStr))return null;
		try {
			return timeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static TimeZone getTimeZone(){
		return TimeZone.getDefault();
	}

	public static String getMSStyleDateStr(Date date){
		if(date == null)return null;
		SimpleDateFormat format =  new SimpleDateFormat(oldStyleFormatStr);
		String temp = format.format(date);
		StringBuffer buffer = new StringBuffer(temp);
		buffer.insert(buffer.length() - 2, ":");
		return buffer.toString();
	}
	
	public static void main(String[] arg){
		System.out.println(getWeek());
		System.out.println(includeTime("2012-05-29 00:00:00", "2012-05-29 16:33:00"));

		System.out.println(getMSStyleDateStr(new Date()));
	}
}
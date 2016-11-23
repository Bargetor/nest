package com.bargetor.nest.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * <p>description: 日期</p>
 * <p>Date: 2012-5-29 下午04:10:43</p>
 * <p>modify：</p>
 * @author: majin
 * @version: 1.0
 */
public class DateUtil {
	public static final String timeFormatStr = "yyyy-MM-dd HH:mm:ss";
	public static final String timePartFormatStr = "HH:mm:ss";
	public static final SimpleDateFormat timeFormat = new SimpleDateFormat(timeFormatStr);
	public static final SimpleDateFormat timePartFormat = new SimpleDateFormat(timePartFormatStr);


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
	public static String getNowToLateForStr(long ms){
		return timeFormat.format(new Date(System.currentTimeMillis() + ms));
	}

	/**
	 *<p>Title: getNowToLate</p>
	 *<p>Description:获取当前时间之后ms秒的时间</p>
	 * @param @param ms
	 * @param @return
	 * @return  Date
	 * @throws
	 */
	public static Date getNowToLate(long ms){
		return getLate(new Date(), ms);
	}

	public static Date getLate(Date date, long ms){
		return new Date(date.getTime() + ms);
	}

	public static String getDateStr(Date date){
		return getStr("yyyy-MM-dd", date);
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
	 *<p>Title: getDayStartStr</p>
	 *<p>Description:获取一天的开始</p>
	 * @param @param date
	 * @param @return 设定文件
	 * @return  String 返回类型
	 * @throws
	*/
	public static String getDayStartStr(Date date){
		return timeFormat.format(getDayStart(date));
	}

	public static Date getDayStart(Date date){
//		if(date == null)return null;
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.set(Calendar.HOUR_OF_DAY, 0);
//		cal.set(Calendar.MINUTE, 0);
//		cal.set(Calendar.SECOND, 0);
//		cal.set(Calendar.MILLISECOND, 0);
//
//		return cal.getTime();

		long day = date.getTime() / 86400000L;
		long time = day * 86400000L - getTimeZone().getRawOffset();
		return new Date(time);
	}

	public static Date getTodayStart(){
		return getDayStart(new Date());
	}
	
	/**
	 *<p>Title: getDayEnd</p>
	 *<p>Description:获取一天的结束</p>
	 * @param @param date
	 * @param @return 设定文件
	 * @return  Date 返回类型
	 * @throws
	*/
	public static Date getDayEnd(Date date){
//		if(date == null)return null;
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.set(Calendar.HOUR_OF_DAY, 23);
//		cal.set(Calendar.MINUTE, 59);
//		cal.set(Calendar.SECOND, 59);
//		cal.set(Calendar.MILLISECOND, 999);
//
//		return cal.getTime();

		long day = date.getTime() / 86400000L;
		long time = (day + 1) * 86400000L - getTimeZone().getRawOffset();
		time -= 1;
		return new Date(time);
	}

	public static String getDayEndStr(Date date){
		return timeFormat.format(getDayEnd(date));
	}
	
	/**
	 *<p>Title: getWeek</p>
	 *<p>Description:得到星期</p>
	 * @param @return 设定文件
	 * @return  int 返回类型
	 * @throws
	*/
	public static int getWeek(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int week = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return week == 0 ? 7:week;
	}

	/**
	 * 获取时间部分，日期部分将会被抹去，设置为1970-01-01
	 * @return
	 */
	public static Date getTimePart(Date date){
		long time = date.getTime();
		long timePart = time % 86400000L;

		return new Date(timePart);
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

	public static boolean includeTime(Date startTime, Date time, Date endTime){
		if(startTime == null || time == null || endTime == null)return false;
		return time.compareTo(startTime) >= 0 && endTime.compareTo(time) >= 0;
	}

	/**
	 * 更早的时间
	 * @param date1
	 * @param date2
     * @return
     */
	public static Date early(Date date1, Date date2){
		if(date1 == null)return date2;
		if(date2 == null)return date1;
		return date1.getTime() >= date2.getTime() ? date2 : date1;
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

	/**
	 * 获取两个日期之间,日期字符串的列表
	 * 比如 start 2016-04-04 end 2016-04-06, 返回 <2016-04-04, 2016-04-05, 2016-04-06>
	 * @param startDate
	 * @param endDate
     * @return
     */
	public static List<String> getIntervalDateStrList(Date startDate, Date endDate){
		if(startDate == null || endDate == null)return null;
		if(endDate.getTime() < startDate.getTime())return null;

		List<String> result = new ArrayList<>();
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		while (cal.getTime().getTime() <= endDate.getTime()){
			result.add(getDateStr(cal.getTime()));
			cal.add(Calendar.DATE, 1);
		}

		return result;
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

	public static Date strToDate(String dateStr, String formatStr){
		if(StringUtil.isNullStr(dateStr) || StringUtil.isNullStr(formatStr))return null;
		SimpleDateFormat format =  new SimpleDateFormat(formatStr);
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parse(DateFormat format, String dateStr){
		if(format == null)return null;
		if(StringUtil.isNullStr(dateStr))return null;
		try {
			return format.parse(dateStr);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseTimeOnly(String timeStr){
		if(StringUtil.isNullStr(timeStr))return null;
		try {
			return timePartFormat.parse(timeStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static TimeZone getTimeZone(){
		return TimeZone.getDefault();
	}
	
	public static void main(String[] arg){
//		System.out.println(getWeek());
//		System.out.println(includeTime("2012-05-29 00:00:00", "2012-05-29 16:33:00"));

//		System.out.println(getMSStyleDateStr(new Date()));

//		long startTime = System.currentTimeMillis();
//		System.out.println(getDayEndStr(new Date()));
//		System.out.println("time is :" + (System.currentTimeMillis() - startTime));

//		Date date = getTimePart(new Date());
//		System.out.println(timeFormat.format(date));
//		System.out.println("time is :" + (System.currentTimeMillis() - startTime));

		Date date = getDayEnd(new Date());
		System.out.println(timeFormat.format(date));
	}
}
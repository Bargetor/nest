package com.bargetor.nest.common.util;

import com.google.common.collect.Range;

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
	public static final String datePartFormatStr = "yyyy-MM-dd";
	public static final String dateTimeTZFormatStr = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	public static final ThreadLocal<SimpleDateFormat> timeFormat = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(timeFormatStr);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> timePartFormat = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(timePartFormatStr);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> datePartFormat = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(datePartFormatStr);
		}
	};

	public static final ThreadLocal<SimpleDateFormat> dateTimeTZPartFormat = new ThreadLocal<SimpleDateFormat>(){
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(dateTimeTZFormatStr);
		}
	};

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
		return timeFormat.get().format(new Date());
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
		return timeFormat.get().format(new Date(System.currentTimeMillis() + ms));
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
		return timeFormat.get().format(date);
	}

	/**
	 * 获取日期在一定单位内的开始
	 * @param date 日期
	 * @param unit 单位，Calendar
	 * @return
	 * @see Calendar
	 */
	public static Date getStart(Date date, int unit){
		if(date == null)return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int tempUnit = unit;
		switch (tempUnit){
			case Calendar.YEAR:
				cal.set(Calendar.DAY_OF_YEAR, 1);
				tempUnit = Calendar.DAY_OF_YEAR;
			case Calendar.MONTH:
				if(tempUnit == Calendar.MONTH){
					cal.set(Calendar.DAY_OF_MONTH, 1);
					tempUnit = Calendar.DAY_OF_MONTH;
				}
			case Calendar.WEEK_OF_YEAR:
			case Calendar.WEEK_OF_MONTH:
				if(tempUnit == Calendar.WEEK_OF_MONTH || tempUnit == Calendar.WEEK_OF_MONTH){
					cal.set(Calendar.DAY_OF_WEEK, 1);
				}
			case Calendar.DAY_OF_YEAR:
			case Calendar.DAY_OF_MONTH:
			case Calendar.DAY_OF_WEEK:
				cal.set(Calendar.HOUR_OF_DAY, 0);
			case Calendar.HOUR_OF_DAY:
			case Calendar.HOUR:
				cal.set(Calendar.MINUTE, 0);
			case Calendar.MINUTE:
				cal.set(Calendar.SECOND, 0);
			case Calendar.SECOND:
				cal.set(Calendar.MILLISECOND, 0);
		}
		return cal.getTime();
	}

	/**
	 * 获取日期在一定单位内的结束
	 * @param date 日期
	 * @param unit 单位，Calendar
	 * @return
	 * @see Calendar
	 */
	public static Date getEnd(Date date, int unit){
		if(date == null)return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int tempUnit = unit;
		switch (tempUnit){
			case Calendar.YEAR:
				cal.add(Calendar.YEAR, 1);
				cal.set(Calendar.DAY_OF_YEAR, 0);
				tempUnit = Calendar.DAY_OF_YEAR;
			case Calendar.MONTH:
				if(tempUnit == Calendar.MONTH){
					cal.add(Calendar.MONTH, 1);
					cal.set(Calendar.DAY_OF_MONTH, 0);
					tempUnit = Calendar.DAY_OF_MONTH;
				}
			case Calendar.WEEK_OF_YEAR:
			case Calendar.WEEK_OF_MONTH:
				if(tempUnit == Calendar.WEEK_OF_MONTH || tempUnit == Calendar.WEEK_OF_MONTH){
					cal.set(Calendar.DAY_OF_WEEK, 0);
				}
			case Calendar.DAY_OF_YEAR:
			case Calendar.DAY_OF_MONTH:
			case Calendar.DAY_OF_WEEK:
				cal.set(Calendar.HOUR_OF_DAY, 23);
			case Calendar.HOUR_OF_DAY:
			case Calendar.HOUR:
				cal.set(Calendar.MINUTE, 59);
			case Calendar.MINUTE:
				cal.set(Calendar.SECOND, 59);
			case Calendar.SECOND:
				cal.set(Calendar.MILLISECOND, 999);
		}
		return cal.getTime();
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
		return timeFormat.get().format(getDayStart(date));
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
		if(date == null)return null;
		long day = (date.getTime() + getTimeZone().getRawOffset()) / 86400000L;
		long time = day * 86400000L - getTimeZone().getRawOffset();
		return new Date(time);
	}

	public static Date getDayMid(Date date){
		if(date == null)return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 12);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
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

		if(date == null)return null;
		long day = (date.getTime() + getTimeZone().getRawOffset()) / 86400000L;
		long time = (day + 1) * 86400000L - getTimeZone().getRawOffset();
		time -= 1;
		return new Date(time);
	}

	public static String getDayEndStr(Date date){
		return timeFormat.get().format(getDayEnd(date));
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
		long time = date.getTime() + getTimeZone().getRawOffset();
		long timePart = time % 86400000L - getTimeZone().getRawOffset();

		return new Date(timePart);
	}

	public static String getDatePartStr(Date date){
		if (date == null) return null;
		return datePartFormat.get().format(date);
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
	public static long timeLack(String startTime, String endTime){
		if(startTime == null || "".equals(startTime))return 0;
		if(endTime == null || "".equals(endTime))return 0;
		try {
			return timeFormat.get().parse(endTime).getTime() - timeFormat.get().parse(startTime).getTime();
		} catch (Exception e) {
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
		List<Date> dates = getIntervalDateList(startDate, endDate);
		if(ArrayUtil.isNull(dates))return null;
		return ArrayUtil.list2List(dates, DateUtil::getDateStr);
	}

	/**
	 * 获取两个日期之间,日期字符串的列表
	 * 比如 start 2016-04-04 end 2016-04-06, 返回 <2016-04-04, 2016-04-05, 2016-04-06>
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static List<Date> getIntervalDateList(Date startDate, Date endDate){
		if(startDate == null || endDate == null)return null;
		Date start = startDate;
		Date end = endDate;
		if(endDate.getTime() < startDate.getTime()){
			start = endDate;
			end = startDate;
		}

		List<Range<Date>> ranges = breakDown(start, end, Calendar.DAY_OF_YEAR);
		List<Date> result = ArrayUtil.list2List(ranges, Range::lowerEndpoint);

		return result;
	}

	/**
	 * 将起止日期在某一时间单位上进行分裂
	 * @param start 开始时间
	 * @param end 结束时间
	 * @param calendarUnit 时间单位
	 * @return 分类出的区间
	 * @see Calendar
	 */
	public static List<Range<Date>> breakDown(Date start, Date end, int calendarUnit){
		if (start == null || end == null) return null;
		if(end.compareTo(start) < 0)return null;
		List<Range<Date>> ranges = new ArrayList<>();

		Date processDate = start;
		Range<Date> range = null;
		do{
			Date unitStart = DateUtil.getStart(processDate, calendarUnit);
			Date unitEnd = DateUtil.getEnd(processDate, calendarUnit);
			range = Range.closed(unitStart, unitEnd);
			ranges.add(range);
			processDate = new Date(unitEnd.getTime() + 1);
		}while (!range.contains(end));

		return ranges;
	}

	public static Date strToDate(String dateStr){
		if(StringUtil.isNullStr(dateStr))return null;
		try {
			return timeFormat.get().parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date strToDate(String dateStr, String formatStr){
		if(StringUtil.isNullStr(dateStr) || StringUtil.isNullStr(formatStr))return null;
		SimpleDateFormat format =  new SimpleDateFormat(formatStr);
		try {
			return format.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Date parse(DateFormat format, String dateStr){
		if(format == null)return null;
		if(StringUtil.isNullStr(dateStr))return null;
		try {
			return format.parse(dateStr);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date parseTimeOnly(String timeStr){
		if(StringUtil.isNullStr(timeStr))return null;
		try {
			return timePartFormat.get().parse(timeStr);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date parseDateOnly(String dateStr){
		if(StringUtil.isNullStr(dateStr))return null;
		try {
			return datePartFormat.get().parse(dateStr);
		} catch (Exception e) {
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
		System.out.println(timeFormat.get().format(date));

		System.out.println(getEnd(date, Calendar.WEEK_OF_MONTH));
	}
}
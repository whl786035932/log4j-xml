/**
 * DateUtil.java
 * cn.videoworks.despotui.util
 * 
 *
 *   ver     date      		author
 * ──────────────────────────────────
 *   		 2014年2月10日 		meishen
 *
 * Copyright (c) 2014, TNT All Rights Reserved.
 */

package cn.videoworks.cms.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * ClassName:DateUtil
 * 
 * @author meishen
 * @version Ver 1.0.0
 * @Date 2014年2月10日 下午3:49:58
 */
public class DateUtil {

	public static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat formatYMD = new SimpleDateFormat(
			"yyyy-MM-dd");
	public static final SimpleDateFormat formatHMS = new SimpleDateFormat(
			"HH:mm:ss");
	
	/**
	 * getPastDate:(得到过去时间)
	 * @author   meishen
	 * @Date	 2018	2018年9月10日		下午2:44:01 
	 * @return String    
	 * @throws
	 */
	public static String getPastDate(int past) {  
       Calendar calendar = Calendar.getInstance();  
       calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
       Date today = calendar.getTime();  
       String result = format.format(today);  
       return result;  
    }  
	
	/**
	 * getDate:(把字符串转换成date)
	 * 
	 * @author meishen
	 * @Date 2014年2月10日 下午3:51:17
	 * @return Date
	 * @throws
	 */
	public static Date getDate(String str) {
		Date date = null;
		try {
			date = format.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * getDate:(把字符串转换成date)
	 * 
	 * @author meishen
	 * @Date 2014年2月10日 下午3:51:17
	 * @return Date
	 * @throws
	 */
	public static Date getDateYMD(String str) {
		Date date = null;
		try {
			date = formatYMD.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * getDate:(毫秒转date)
	 *
	 * @author   meishen
	 * @Date	 2018	2018年6月6日		下午3:28:58
	 * @param millionSeconds
	 * @return   
	 * @return Date    
	 * @throws 
	 * @since  Videoworks　Ver 1.1
	 */
	public static Date getDate(Long millionSeconds) {
		Date date = null;
		try {
			date = new Date(millionSeconds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getDateStr(String str) {
		String date = "";
		try {
			date = format.format(format.parse(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static String getDateStrYMD(String str) {
		String date = "";
		try {
			date = formatYMD.format(formatYMD.parse(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}

	public static final Timestamp getNowTimeStamp() {
		Date now = new Date();
		return Timestamp.valueOf(format.format(now));
	}

	public static final String getNowTime() {
		Date now = new Date();
		return format.format(now);
	}
	
	public static final String getDate(Date date) {
		return format.format(date);
	}
	
	public static final String getDateYMD(Date date) {
		return formatYMD.format(date);
	}

	public static final String getNowTimeYMD() {
		Date now = new Date();
		return formatYMD.format(now);
	}

	public static final long getNowTimeYMDLong() {
		Date now = new Date();
		try {
			return formatYMD.parse(formatYMD.format(now)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static final long getNowTimeLong() {
		Date now = new Date();
		try {
			return format.parse(format.format(now)).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static final long geTimeYMDLong(String date) {
		try {
			return formatYMD.parse(date).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static final long geTimeHMSLong(String time) {
		try {
			return formatHMS.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static final long geTimeYMDHSMLong(String time) {
		try {
			return format.parse(time).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static Timestamp getTimeStamp(String time) {
		Date date;
		try {
			date = format.parse(time);
			return Timestamp.valueOf(format.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("init " + time + " error!");
		}
		return null;
	}
	
	public static Timestamp getTimeStampYMD(String time) {
		Date date;
		try {
			date = formatYMD.parse(time);
			return Timestamp.valueOf(format.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
			System.out.println("init " + time + " error!");
		}
		return null;
	}

	/** 将utc的long值转为utc时间 */
	public static GregorianCalendar getUTCDate(long millionSeconds) {
		long sd = millionSeconds;
		Date dat = new Date(sd);
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(dat);

		return gc;
	}

	/** 将utc时间转本地时间 */

	public static String convertUTCtoLocal(GregorianCalendar gc) {
		String sb = format.format(gc.getTime());
		return sb;
	}

	public static long localLongFromUTClLong(long millionSeconds) {
		GregorianCalendar gcDate = getUTCDate(millionSeconds);
		String localDate = convertUTCtoLocal(gcDate);
		try {
			Date parse = format.parse(localDate);
			return parse.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * 获取当前utc时间的long值
	 */
	public static long getNowUTC() {
		Calendar calendar = Calendar.getInstance();
		int offset = calendar.get(Calendar.ZONE_OFFSET);
		calendar.add(Calendar.MILLISECOND, -offset);
		Date date = calendar.getTime();
		String nowTime = formatHMS.format(date);
		System.out.println("当前时间为nowTime=" + nowTime);
		long nowTimeLong = DateUtil.geTimeHMSLong(nowTime);
		return nowTimeLong;
	}

	/**
	 * 获取相隔的天数
	 */
	public static int daysBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuring(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (mss % (1000 * 60)) / 1000;
		return days + " days " + hours + " hours " + minutes + " minutes "
				+ seconds + " seconds ";
	}

	public static String formatDuringDay(long mss) {
		long days = mss / (1000 * 60 * 60 * 24);
		return String.valueOf(days);
	}

	/**
	 * 
	 * @param begin
	 *            时间段的开始
	 * @param end
	 *            时间段的结束
	 * @return 输入的两个Date类型数据之间的时间间格用* days * hours * minutes * seconds的格式展示
	 * @author fy.zhang
	 */
	public static String formatDuring(Date begin, Date end) {
		return formatDuring(end.getTime() - begin.getTime());
	}

	/**
	 * @Description:获取相隔的小时数
	 * @author:whl
	 * @throws ParseException
	 */
	public static int hoursBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_hours = (time2 - time1) / (1000 * 3600);

		return Integer.parseInt(String.valueOf(between_hours));
	}

	/**
	 * @Description:比较时间大小
	 * @author:whl
	 * @throws ParseException
	 */
	public static long timeBetween(String smdate, String bdate)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));
		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_hours = time2 - time1;
		return between_hours;
	}

	/**
	 * 
	 * 
	 * @Description:将时间的毫秒数转为字符串
	 * @author:whl
	 * @return:String
	 * @Date:2017年9月11日 上午10:39:02
	 */
	public static String dateTime(long millionSeconds) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date date = new Date(millionSeconds);
		String str = sdf.format(date);
		return str;
	}

	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String str = sdf.format(date);
		return str;
	}
	public static String dateTimeToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(date);
		return str;
	}

	/**
	 * 近n天的时间
	 * 
	 * @param n
	 *            天数
	 * @throws ParseException
	 */
	public static Date lastTime(int n) throws ParseException {
		Date nDate = null;
		Date nowTime = new Date();
		// 当天时间
		String now = formatYMD.format(nowTime);
		Date nowDay = formatYMD.parse(now);
		long nTime = nowDay.getTime() / 1000 - n * 24 * 60 * 60;
		nDate = new Date(nTime * 1000);
		return nDate;
	}

	/**
	 * 
	 * 
	 * @Description:
	 * @author:whl
	 * @return:String
	 * @Date:2017年10月21日 下午3:40:49
	 */
	public static String distanceTime(String nowTime, String bigTime)
			throws ParseException {
		int daysBetween = daysBetween(nowTime, bigTime);
		if (daysBetween >= 365 || (daysBetween >= 366)) {
			return "一年以上";
		} else if (daysBetween >= 30) {
			return "一月以上";
		} else if (daysBetween >= 7) {
			return "一周以上";
		}else if(daysBetween<0){
			return "已结束";
		} else {
			daysBetween = daysBetween == 0 ? 1 : daysBetween;
			return daysBetween + "天以上";
		}
	}
	
	public static void main(String[] args) {
		 System.out.println(DateUtil.getPastDate(72));
	}
	
	
	
	public static String timeStampConvertStr(Timestamp timestamp) {
		return format.format(timestamp);
	}
	
	public static final String getNowTimeOnlyYMD(){
		Date now=new Date();
		return formatYMD.format(now);
	}

}

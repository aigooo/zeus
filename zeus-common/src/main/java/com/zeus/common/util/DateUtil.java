package com.zeus.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String DATE_PARTTEN = "yyyy-MM-dd";

	public static String TIME_PARTTEN = System.getProperty("TIME_PARTTEN", "yyyy-MM-dd HH:mm:ss");
	
	public static Boolean theEndOfTheYear(Date lotDate) {
		if(lotDate==null){
			return Boolean.FALSE;
		}
		String dateStr = dateToString(lotDate);
		if(dateStr.contains("12-31")){
			return Boolean.TRUE;
		}else{
			return Boolean.FALSE;
		}
	}
	
	/**
	 * 将字符串类型转化为日期类型
	 * @param date
	 * @return
	 * @addBy Administrator
	 * @addTime 上午9:41:52
	 */
	public static Date stringToDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PARTTEN);
		Date d = null;
		try {
			String[] datesl = new String[3];
			if (date.contains("-")) {
				datesl = date.split("-");
			} else if (date.contains("/")) {
				datesl = date.split("/");
			}
			if (datesl[1].length() == 1) {
				datesl[1] = "0" + datesl[1];
			}
			if (datesl[2].length() == 1) {
				datesl[2] = "0" + datesl[2];
			}
			d = sdf.parse(datesl[0] + "-" + datesl[1] + "-" + datesl[2]);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	
	public static String dateToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_PARTTEN);
		return sdf.format(date);
	}
	
	public static String timeToString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(TIME_PARTTEN);
		return sdf.format(date);
	}
	
	public static Date yesterday(Date date) {
		Long yes = date.getTime() - 24 * 60 * 60 * 1000;
		return new Date(yes);
	}

	public static Date tomorrow(Date date) {
		Long tom = date.getTime() + 24 * 60 * 60 * 1000;
		return new Date(tom);
	}

}

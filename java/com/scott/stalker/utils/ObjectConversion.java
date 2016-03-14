package com.scott.stalker.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ObjectConversion {
	
	public static Date long2Date (Long l) {
		if (l <= 0)
			return null;
		
		return new Date(l);
	}
	
	public static String date2String (Date date) {
		if (date == null)
			return null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(date);
	}
	
	/**
	 * yyyy-MM-dd
	 * @return
	 * @throws ParseException 
	 */
	public static Date string2Date1 (String str) throws ParseException {
		/*if (str == null || str.length() <= 0)
			return null;*/
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		return formatter.parse(str);
	}
	
	public static long date2Long (Date date) {
		if (date == null)
			return 0;
		
		return date.getTime();
	}
	
	public static Date addOneDay (Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, 1);
		
		return new Date (cal.getTimeInMillis());
	}
	
	public static void main(String[] args) throws ParseException {
		System.out.println(string2Date1("2016-03-01").getTime());
	}

}

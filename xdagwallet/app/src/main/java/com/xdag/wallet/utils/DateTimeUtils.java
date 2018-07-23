package com.xdag.wallet.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangxuguo on 2018/7/9.
 */

public class DateTimeUtils {
    public static final String NORMAT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_yyyy_MM = "yyyy-MM";

    public static String getFORMAT_yyyy_MM(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_yyyy_MM);
        return  sdf.format(date);
    }

    public static long getPreOneMonthMaxMills(long timeMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(month == 1){
            year --;
            month = 12;
        }else {
            month--;
        }
        calendar.set(year,month,1,0,0,0);
        calendar.set(year,month,calendar.getMaximum(Calendar.DAY_OF_MONTH),23,59,59);
        return calendar.getTimeInMillis();
    }
    public static long getPreOneMonthByTimMills(long timeMills) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMills);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        if(month == 1){
            year --;
            month = 12;
        }else {
            month--;
        }
        calendar.set(year,month,1,0,0,0);
        return calendar.getTimeInMillis();
    }
    public static long getMaxMillsForCurMonth(Calendar ca) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ca.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTimeInMillis();
    }
    public static long getMinMillsForCurMonth(Calendar ca) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(ca.getTimeInMillis());
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public static String getFORMAT_yyyy_MM(long mills) {
        SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_yyyy_MM);
        return  sdf.format(new Date(mills));
    }
}

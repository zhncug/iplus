package com.iplus.common.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.time.Duration;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public abstract class DateTimeUtils {

    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String MM_DOT_DD = "MM.dd";
    public static final String H = "H点";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME);
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern(DATE);

    private static final int CHART_TYPE_TODAY = 0;
    private static final int TOP_CHART_TYPE_7 = 1;
    private static final int TOP_CHART_TYPE_30 = 2;

    /**
     * 单位秒
     * @return
     */
    public static long timestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * yyyy-MM-dd HH:mm:ss 转时间戳 单位秒
     * @return
     */
    public static long timestamp(String dateTime) {
        DateTime time = DateTime.parse(dateTime, DATE_TIME_FORMATTER);
        return time.getMillis() / 1000L;
    }


    public static long timestamp(String dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        DateTime time = DateTime.parse(dateTime, formatter);
        return time.getMillis() / 1000L;
    }

    /**
     * 返回值单位秒
     * @param date
     * @return
     */
    public static long timestamp(Date date) {
        return date.getTime() / 1000L;
    }

    /**
     * @param time s
     */
    public static Date formatSecondUnitToDate(long time) {
        return new Date(time * 1000);
    }


    /**
     * @param time s
     * @return yyyy-MM-dd hh:mm:ss
     */
    public static String formatSecondUnitToString(long time) {
        return formatToString(time * 1000, DATE_TIME);
    }

    /**
     * @param time s
     * @return
     */
    public static String formatSecondUnitToString(long time, String pattern) {
        return formatToString(time * 1000, pattern);
    }


    /**
     * @param time ms
     * @param pattern          eg:yyyy-MM-dd
     * @return
     */
    public static String formatToString(long time, String pattern) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.print(time);
    }

    /**
     * @param time ms
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String formatToString(long time) {
        return formatToString(time, DATE_TIME);
    }

    /**
     * 返回当前日期  eg:2016-01-01
     * @return
     */
    public static String currentDate() {
        return DATE_FORMATTER.print(System.currentTimeMillis());
    }


    /**
     * 返回当前时间 eg:2016-01-01 12:12:12
     * @return
     */
    public static String currentDateTime() {
        return DATE_TIME_FORMATTER.print(System.currentTimeMillis());
    }

    public static String currentDateBegin() {
        return DATE_FORMATTER.print(System.currentTimeMillis()) + " 00:00:00";
    }

    public static String currentDateEnd() {
        return DATE_FORMATTER.print(System.currentTimeMillis()) + " 23:59:59";
    }

    /**
     * 某一天的开始
     * @param date 单位为毫秒
     * @return
     */
    public static String dateBegin(long date) {
        return DATE_FORMATTER.print(date) + " 00:00:00";
    }

    /**
     * 某一天的结束
     * @param date 单位为毫秒
     * @return
     */
    public static String dateEnd(long date) {
        return DATE_FORMATTER.print(date) + " 23:59:59";
    }

    /**
     * 获取距离1970-01-01间隔多少天的时间戳
     * @param days 天数
     * @return timestamp s
     */
    public static long getTimestampByDays(final int days) {
        return getTimestampByDays(0, days);
    }

    /**
     * 获取距离data间隔多少天的时间戳
     * @param data timestamp s
     * @param days 天数
     * @return
     */
    public static long getTimestampByDays(final long data, final int days) {
        DateTime dateTime = new DateTime(data * 1000);
        return dateTime.plusDays(days).getMillis() / 1000L;
    }

    public static long timeUnitChange(long souTime, TimeUnit souUnit, TimeUnit targetUnit){
        if(souUnit == targetUnit){
            return souTime;
        }
        return targetUnit.convert(souTime, souUnit);
    }

    /**
     * @return 单位秒
     */
    public static long increaseDays(String since, int days) {
        return increaseDays(since, days, DATE_FORMATTER);
    }

    /**
     * @return 单位秒
     */
    public static long increaseDays(long timestamp, int days) {
        return increaseDays(new DateTime(timestamp * 1000L), days).getMillis() / 1000L;
    }

    /**
     * @return 单位秒
     */
    public static long increaseDays(String since, int days, DateTimeFormatter formatter) {
        DateTime dt = DateTime.parse(since, formatter);
        return increaseDays(dt, days).getMillis() / 1000L;
    }

    public static DateTime increaseDays(DateTime since, int days) {
        if (days > 0) {
            return since.plusDays(days);
        } else {
            return since.minusDays(-days);
        }
    }

    public static Duration durationFrom(Date date) {
        return Duration.ofSeconds(timestamp() - timestamp(date));
    }

    public static String format(String datetime, String format) {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(format);
        DateTime dt = DateTime.parse(datetime, DATE_TIME_FORMATTER);
        return dt.toString(formatter);
    }

    /**
     * 将秒转换成 x天x小时x分x秒
     * @param
     * @return
     */
    public static String formatTime(long s) {
        Integer mi = 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = s / dd;
        Long hour = (s - day * dd) / hh;
        Long minute = (s - day * dd - hour * hh) / mi;
        Long second = s - day * dd - hour * hh - minute * mi;

        StringBuffer sb = new StringBuffer();
        if(day > 0) {
            sb.append(day+"天");
        }
        if(hour > 0) {
            sb.append(hour+"小时");
        }
        if(minute > 0) {
            sb.append(minute+"分");
        }
        if(second > 0) {
            sb.append(second+"秒");
        }
        return sb.toString();
    }

    public static Duration duration(Date from, Date to) {
        return Duration.ofSeconds(timestamp(to) - timestamp(from));
    }
}

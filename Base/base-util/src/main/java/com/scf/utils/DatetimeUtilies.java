/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scf.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
/**
 * 日期工具类
 * @author wubin
 * @date 2016年7月28日 下午4:27:24 
 * @version V1.1.0
 */
public class DatetimeUtilies
{
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    
    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    
    public static final String DATE_TIME_MS = "yyyy-MM-dd HH:mm:ss:SSS";
    
    public static final String DATE_TIME_MIN = "yyyy-MM-dd HH:mm";
    
    public static final String DATE = "yyyy-MM-dd";
    
    public static final String TIME = "HH:mm:ss";
    
    public static final String DATE_HOUR = "yyyy-MM-dd HH";
    
    public static final String DATE_SHORT = "yyMMdd";
    
    /**
     * 获取当前时间秒数
     * 
     * @return String
     */
    public static String getCurrTime10()
    {
        return "" + System.currentTimeMillis() / 1000;
    }
    
    
    /**
     * 检查给定的日期是否在两个日期中间
     *
     * @param current compare date
     * @param min min date
     * @param max max date
     * @return if between min date and max date, then return true.
     */
    public static boolean between(Date current, Date min, Date max)
    {
        return current.after(min) && current.before(max);
    }
    
    /**
     * 计算给定月分的自然周划分区间段（可能会上下月交界）为5周。
     *
     * @param month 指定的月份
     * @return weeks array. for example, ['03.01-03.07', '03.08-03.14']
     */
    public static List<String> getMonthWeeks(int month)
    {
        SimpleDateFormat df = new SimpleDateFormat("MM.dd");
        List<String> list = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.WEEK_OF_MONTH, 1);
        calendar.set(Calendar.DAY_OF_WEEK, 2);
        for (int i = 0; i < 5; i++)
        {
            Date d = calendar.getTime();
            String dt = df.format(d);
            d = DateUtils.addDays(d, 6);
            dt += "-" + df.format(d);
            calendar.setTime(DateUtils.addDays(d, 1));
            list.add(dt);
        }
        return list;
    }
    
    /**
     * return current time by timestamp.
     *
     * @return
     */
    public static Timestamp currentTime()
    {
        return new Timestamp(System.currentTimeMillis());
    }
    
    /**
     *
     * @return
     */
    public static Date currentDay()
    {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }
    
    /**
     *
     * @param date
     * @return
     */
    public static boolean beforeToday(Date date)
    {
        Date today = currentDay();
        return date.before(today);
    }
    
    /**
     * get the days between give date range.
     *
     * @param from begin date
     * @param to end date
     * @return days
     */
    public static int daysBetween(Date from, Date to)
    {
        long t = to.getTime() - from.getTime();
        return Integer.valueOf(t / 1000 / 60 / 60 / 24 + "");
    }
    
    /**
     * return current week range.
     *
     * @return [0] begin date, [1] end date
     */
    public static Date[] getCurrentWeek()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date date1 = cal.getTime();
        Date date2 = DateUtils.addDays(date1, 6);
        return new Date[] {date1, date2};
    }
    
    /**
     * return current month range.
     *
     * @return [0] begin date, [1] end date
     */
    public static Date[] getCurrentMonth()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date date1 = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date date2 = cal.getTime();
        return new Date[] {date1, date2};
    }
    
    /**
     * get current quarter.
     *
     * @return quarter number
     */
    public static int getCurrentQuarter()
    {
        Calendar calendar = Calendar.getInstance();
        int mh = calendar.get(Calendar.MONTH) + 1;
        int qr = (mh - (mh - 1) % 3 + 2) / 3;
        return qr;
    }
    
    /**
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean dateEquals(Date date1, Date date2)
    {
        boolean eq = true;
        if ((date1 != null && date2 == null) || (date2 != null && date1 == null))
        {
            eq = false;
        }
        else if (date1 != null && date2 != null)
        {
            eq = date1.getTime() == date2.getTime();
        }
        return eq;
    }
    
    /**
     * 获取本地时间的当前时间戳.<br>
     * 1. 时间戳格式为：yyyyMMddHHmmss<br>
     * 
     * @return 时间戳字符串
     * @author LiCunjing
     */
    public static String getCurTimestampStr()
    {
        SimpleDateFormat formatter = new SimpleDateFormat(YYYYMMDDHHMMSS);
        
        return formatter.format(new Date());
    }
    
    // 通过时间格式格式化时间
    public static String formatDateTime(String pattern, Date date)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        
        return formatter.format(date);
    }
    
    /**
     * 当前时间加上固定秒数<br>
     * 
     * @return 时间
     * @author zhangchangshun
     */
    public static Date addSecond(Date date, int seconds)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, seconds);
        return calendar.getTime();
    }
    
    /**
     * 将指定格式的时间字符串转为Date对象<br>
     * 1. timeString的格式必须符合pattern，否则抛出异常。<br>
     * 
     * @param pattern 时间格式
     * @param timeString 时间字符串
     * @return Date类型时间
     * @throws ParseException 时间字符串解析失败，pattern与timeString不符
     * @author LiCunjing
     */
    public static Date parse(String pattern, String timeString)
        throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.parse(timeString);
    }
    
    /**
     * 计算当天剩余秒数
     * 
     * @return
     */
    public static int getRemainSeconds()
    {
        Calendar calendar = Calendar.getInstance();
        long startTime = calendar.getTimeInMillis();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long endTime = calendar.getTimeInMillis();
        int time = (int)((endTime - startTime) / 1000);
        return time;
    }
    
    /**
     * 获取系统指定天数之前的时间
     * @author Oliver
     * @param days
     * @return
     */
    public static Date getSpecifyDaysAgo(int days)
    {
        Calendar calendar = Calendar.getInstance();
        
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        return calendar.getTime();
    }
    
    
    /**
     * 获得某天的零点时刻0:0:0
     * 
     * @param date
     *            日期
     * @return
     */
    public static Date getDayBegin(Date date) {

        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    
    /**
     * 获得某天的截至时刻23:59:59
     * 
     * @param date
     * @return
     */
    public static Date getDayEnd(Date date) {

        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }
}

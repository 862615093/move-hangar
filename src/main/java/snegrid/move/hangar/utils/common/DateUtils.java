package snegrid.move.hangar.utils.common;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.lang.management.ManagementFactory;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 时间工具类
 *
 * @author drone
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {

    public static String YYYY = "yyyy";

    public static String YYYY_MM = "yyyy-MM";

    public static String YYYY_MM_DD = "yyyy-MM-dd";

    public static String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 获取当前Date型日期
     *
     * @return Date() 当前日期
     */
    public static Date getNowDate() {
        return new Date();
    }

    /**
     * 获取当前日期, 默认格式为yyyy-MM-dd
     *
     * @return String
     */
    public static String getDate() {
        return dateTimeNow(YYYY_MM_DD);
    }

    public static final String getTime() {
        return dateTimeNow(YYYY_MM_DD_HH_MM_SS);
    }

    public static final String dateTimeNow() {
        return dateTimeNow(YYYYMMDDHHMMSS);
    }

    public static final String dateTimeNow(final String format) {
        return parseDateToStr(format, new Date());
    }

    public static final String dateTime(final Date date) {
        return parseDateToStr(YYYY_MM_DD, date);
    }

    public static final String parseDateToStr(final String format, final Date date) {
        return new SimpleDateFormat(format).format(date);
    }

    public static final Date dateTime(final String format, final String ts) {
        try {
            return new SimpleDateFormat(format).parse(ts);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    public static final String datePath() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyy/MM/dd");
    }

    /**
     * 日期路径 即年/月/日 如20180808
     */
    public static final String dateTime() {
        Date now = new Date();
        return DateFormatUtils.format(now, "yyyyMMdd");
    }

    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取服务器启动时间
     */
    public static Date getServerStartDate() {
        long time = ManagementFactory.getRuntimeMXBean().getStartTime();
        return new Date(time);
    }

    /**
     * 计算两个时间差
     */
    public static String getDatePoor(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // long ns = 1000;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        // 计算差多少分钟
        long min = diff % nd % nh / nm;
        // 计算差多少秒//输出结果
        // long sec = diff % nd % nh % nm / ns;
        return day + "天" + hour + "小时" + min + "分钟";
    }


    /**
     * 秒转换成时分秒
     *
     * @param seconds
     * @return
     */
    public static String secToTime(int seconds) {
        int hour = seconds / 3600;
        int minute = (seconds - hour * 3600) / 60;
        int second = (seconds - hour * 3600 - minute * 60);
        StringBuffer sb = new StringBuffer(50);
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        return sb.length() > 0 ? sb.toString() : null;
    }

    /**
     * 获取两个日期之间的所有日期(字符串格式, 按天计算)
     *
     * @param startTime String 开始时间 yyyy-MM-dd
     * @param endTime   String 结束时间 yyyy-MM-dd
     * @return
     */
    public static List<String> getBetweenDays(String startTime, String endTime) {
        if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime)) {
            return null;
        }
        //1、定义转换格式
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        Date start = null;
        try {
            start = df.parse(startTime);
            Date end = df.parse(endTime);
            if (Objects.isNull(start) || Objects.isNull(end)) {
                return null;
            }
            List<String> result = new ArrayList<String>();

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            tempStart.add(Calendar.DAY_OF_YEAR, 1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            result.add(sdf.format(start));
            while (tempStart.before(tempEnd)) {
                result.add(sdf.format(tempStart.getTime()));
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 前、后几天
     *
     * @param d
     * @param day
     * @return
     */
    public static Date rollDay(Date d, int day) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_MONTH, day);
        return cal.getTime();
    }

    /**
     * 前、后几月
     *
     * @param d
     * @param mon
     * @return
     */
    public static Date rollMon(Date d, int mon) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MONTH, mon);
        return cal.getTime();
    }


    public static String DateToString(Date time, String formatter) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
        String newDate = sdf.format(time);
        return newDate;
    }

    public static Date stringToDate(String str, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 日期型字符串转化为日期 格式
     */
    public static Date formartDateToString(Object str, String parsePatterns) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取间隔分钟数
     *
     * @param beforeDate 前一个日期
     * @param afterDate  后一个日期
     * @return 间隔分钟数
     * @author maoli
     * @date 2020/12/25 11:11
     **/
    public static long getIntervalMinutes(Date beforeDate, Date afterDate) {
        long beforeDateLong = beforeDate.getTime();
        long afterDateLong = afterDate.getTime();
        return (afterDateLong - beforeDateLong) / 60000;
    }


    /**
     * @param pattern 时间格式
     * @return 当前月第一天
     * @description:获取当前月第一天
     * @author:
     */
    public static String getMonthStart(String pattern) {
        //获取当前月第一天
        Calendar currentDate = Calendar.getInstance();
        currentDate.add(Calendar.MONTH, 0);
        currentDate.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天

        if (pattern == null || "".equals(pattern)) {
            pattern = "yyyy-MM-dd";
        }

        SimpleDateFormat format = new SimpleDateFormat(pattern);

        return format.format(currentDate.getTime());
    }

    /**
     * @param pattern 时间格式
     * @return 当前月最后一天
     * @description:获取当前月最后一天
     * @author:
     */
    public static String getMonthEnd(String pattern) {
        //获取当前月最后一天
        Calendar currentDate = Calendar.getInstance();
        currentDate.set(Calendar.DAY_OF_MONTH, currentDate.getActualMaximum(Calendar.DAY_OF_MONTH));

        if (pattern == null || "".equals(pattern)) {
            pattern = "yyyy-MM-dd";
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(currentDate.getTime());
    }

    /**
     * 比较日期大小
     * <p>
     * 2017年9月7日 16:15:53
     * xj
     *
     * @param DATE1      第一个时间
     * @param DATE2      第二个时间
     * @param dateFormat 日期格式
     * @return Integer null日期格式有误，1：第一个日期大，0：两个日期一样，-1：第二个日期大
     */
    public static Integer compareDate(String DATE1, String DATE2, String dateFormat) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.getMessage();
        }

        return null;
    }


    /**
     * 获取天之前和之后的日期
     *
     * @param date
     * @param days
     * @return
     */
    public static Date getFrontOrAfterDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + days);
        return calendar.getTime();

    }


    /**
     * 校验时间是否为 yyyyMMddHHmmss 格式
     *
     * @param str
     * @return
     */
    public static boolean checkDateTime(String str, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        boolean flag = true;
        try {
            LocalDateTime.parse(str, dtf);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 秒转换成时分秒
     *
     * @return
     */
    public static String sedChangeToMinAndSed(Integer seconds) {
        int temp = 0;
        StringBuffer sb = new StringBuffer();
        temp = seconds / 3600;
        sb.append(temp + ":");

        temp = seconds % 3600 / 60;
        sb.append(temp + ":");

        temp = seconds % 3600 % 60;
        sb.append(temp);
        return sb.toString();
    }


    /**
     * 两个时间相差秒数
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static int calLastedTime(Date startDate, Date endDate) {
        long a = endDate.getTime();
        long b = startDate.getTime();
        int c = (int) ((a - b) / 1000);
        return c;
    }

    /***
     *  功能描述：日期转换cron表达式
     * @param date
     * @param dateFormat : e.g:yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String formatDateByPattern(Date date, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }

    /***
     * convert Date to cron ,eg.  "0 07 10 15 1 ? 2016"
     * @param date  : 时间点
     * @return
     */
    public static String getCron(Date date) {
        String dateFormat = "ss mm HH dd MM ? *";
        return formatDateByPattern(date, dateFormat);
    }

    /**
     * 字符串时间转毫秒时间戳
     *
     * @param timers
     * @return
     */
    public static long timeToStamp(String timers) {
        Date d = new Date();
        long timeStemp = 0;
        try {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            d = sf.parse(timers);// 日期转换为时间戳
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        timeStemp = d.getTime();
        return timeStemp;
    }

    /**
     * 时间戳转换成字符串
     */
    public static String getDateToString(long time) {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(d);
    }

    public static void main(String[] args) {
        System.out.println(getDateToString(1658650077000L));
    }
}

package com.brucezee.jspider.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间相关工具类
 * Created by brucezee on 2017/1/7.
 */
public class SpiderTimeUtils {
    private static final Logger logger = LoggerFactory.getLogger(SpiderTimeUtils.class);
    public static final String FORMAT_YYYYMMDDHHMMSS =  "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS2 = "yyyy/MM/dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS3 = "yy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYYMMDDHHMMSS4 = "yyyyMMddHHmmss";

    public static final String FORMAT_YYYYMMDD =  "yyyy-MM-dd";
    public static final String FORMAT_YYYYMMDD2 = "yyyy/MM/dd";
    public static final String FORMAT_YYYYMMDD3 = "yyyy年MM月dd日";
    public static final String FORMAT_YYYYMMDD4 = "yyyyMMdd";

    public static final String FORMAT_YYYYMMDDHHMM =  "yyyy-MM-dd HH:mm";
    public static final String FORMAT_YYYYMMDDHHMM2 = "yyyy/MM/dd HH:mm";

    public static final String FORMAT_YYYYMM =  "yyyy-MM";
    public static final String FORMAT_YYYYMM2 = "yyyy/MM";
    public static final String FORMAT_YYYYMM3 = "yyyy年MM月";
    public static final String FORMAT_YYYYMM4 = "yyyyMM";

    /**
     * 转换日期字符串为日期对象
     * @param timeText 日期字符串
     * @param pattern 日期格式（如yyyy-MM-dd HH:mm:ss）
     * @return 成功返回日期对象，失败返回null。
     */
    public static Date parseDate(String timeText, String pattern) {
        if (StringUtils.isNotBlank(timeText)) {
            if (StringUtils.isNotBlank(pattern)) {
                try {
                    return new SimpleDateFormat(pattern).parse(timeText);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                return parseDate(timeText);
            }
        }
        return null;
    }

    /**
     * 格式化日期字符串，兼容部分通用日期格式
     * @param timeText 日期字符串
     * @return 字符串对应的日期
     */
    public static Date parseDate(String timeText) {
        if (StringUtils.isBlank(timeText)) {
            return null;
        }
        timeText = StringUtils.trim(timeText);
        if (timeText.contains("-")) {
            //常用的放在前面
            if (timeText.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS);
            }
            if (timeText.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDD);
            }
            if (timeText.matches("^\\d{2}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS3);
            }
            if (timeText.matches("^\\d{4}-\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMM);
            }
            if (timeText.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDDHHMM);
            }
        }

        if (timeText.contains("/")) {
            //常用的放在前面
            if (timeText.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS2);
            }
            if (timeText.matches("^\\d{4}/\\d{1,2}/\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDD2);
            }

            if (timeText.matches("^\\d{4}/\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMM2);
            }
            if (timeText.matches("^\\d{4}/\\d{1,2}/\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
                return parseDate(timeText, FORMAT_YYYYMMDDHHMM2);
            }
        }

        if (timeText.contains("年")) {
            if (timeText.matches("^\\d{4}年\\d{1,2}月\\d{1,2}日$")) {
                return parseDate(timeText, FORMAT_YYYYMMDD3);
            }

            if (timeText.matches("^\\d{4}年\\d{1,2}月$")) {
                return parseDate(timeText, FORMAT_YYYYMM3);
            }
        }

        //常用的放在前面
        if (timeText.matches("\\d{6}")) {
            return parseDate(timeText, FORMAT_YYYYMM4);
        }
        if (timeText.matches("\\d{8}")) {
            return parseDate(timeText, FORMAT_YYYYMMDD4);
        }
        if (timeText.matches("\\d{14}")) {
            return parseDate(timeText, FORMAT_YYYYMMDDHHMMSS4);
        }

        throw new IllegalArgumentException("Invalid date format :" + timeText + "");
    }

    /**
     * 将日期字符串转换成另外一种格式的日期字符串
     * @param timeText 日期字符串
     * @param pattern 原字符串的日期格式
     * @param resultPattern 新日期的格式
     * @return 指定的另外一种格式的日期字符串
     */
    public static String parseDateString(String timeText, String pattern, String resultPattern) {
        Date date = parseDate(timeText, pattern);
        if (date != null) {
            return formatTime(date, resultPattern);
        }
        return null;
    }

    /**
     * 将日期字符串转换成另外一种格式的日期字符串
     * @param timeText 日期字符串
     * @param resultPattern 新日期的格式
     * @return 指定的另外一种格式的日期字符串
     */
    public static String parseDateString(String timeText, String resultPattern) {
        return parseDateString(timeText, null, resultPattern);
    }

    /**
     * 格式化时间
     * @param time 日期
     * @param pattern 时间格式字符串 如：yyyy-MM-dd HH:mm:ss
     * @return 指定格式的时间字符串
     */
    public static String formatTime(Date time, String pattern) {
        if (time != null && StringUtils.isNotBlank(pattern)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.format(time);
        }
        return null;
    }

    /**
     * 转换日期字符串为时间毫秒数
     * @param timeText 日期字符串
     * @param pattern 日期格式（如yyyy-MM-dd HH:mm:ss）
     * @return 成功返回时间毫秒数，失败返回null。
     */
    public static Long parseTime(String timeText, String pattern) {
        Date date = parseDate(timeText, pattern);
        if (date != null) {
            return date.getTime();
        }
        return null;
    }
}

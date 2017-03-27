package com.brucezee.jspider.common.utils;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具类
 * Created by brucezee on 2017/1/7.
 */
public class SpiderStrUtils {
    public static final String NUMBERS_REGEX = "-?[\\d]+(,\\d{3})*\\.?[\\d]*";

    /**
     * 获取字符串中介于prefix和suffix之间的字符串
     * @param content 原字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 没有找到返回null。
     */
    public static String getMiddleText(String content, String prefix, String suffix) {
        return getMiddleText(content, prefix, suffix, true, true);
    }

    /**
     * 获取字符串中介于prefix和suffix之间的字符串
     * @param content 原字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @Param lazyPrefix 是否使用第一次找到的前缀作为截取前缀
     * @Param lazySuffix 是否使用第一次找到的后缀作为截取后缀
     * @return 没有找到返回null。
     */
    public static String getMiddleText(String content, String prefix, String suffix, boolean lazyPrefix, boolean lazySuffix) {
        if (content != null) {
            if (prefix != null && suffix != null) {
                //截取前缀到后缀的字符串
                int prefixIndex = lazyPrefix ? content.indexOf(prefix) : content.lastIndexOf(prefix);
                if (prefixIndex >= 0) {
                    String tail = content.substring(prefixIndex + prefix.length());
                    int suffixIndex = lazySuffix ? tail.indexOf(suffix) : tail.lastIndexOf(suffix);
                    if (suffixIndex > 0) {
                        return tail.substring(0, suffixIndex);
                    }
                }
            } else if (prefix == null && suffix != null) {
                //截取后缀到末尾的字符串
                int suffixIndex = lazySuffix ? content.indexOf(suffix) : content.lastIndexOf(suffix);
                if (suffixIndex > 0) {
                    return content.substring(0, suffixIndex);
                }
            } else if (prefix != null && suffix == null) {
                //截取开头到前缀的字符串
                int prefixIndex = lazyPrefix ? content.indexOf(prefix) : content.lastIndexOf(prefix);
                if (prefixIndex >= 0) {
                    return content.substring(prefixIndex + prefix.length(), content.length());
                }
            }
        }
        return null;
    }

    /**
     * 从文本中根据一连串的前缀和后缀截取字符串
     * @param content 原字符串
     * @param prefixSuffixes 前缀后缀的组合
     * @return 没有找到返回null
     */
    public static String getMiddleText(String content, PrefixSuffix...prefixSuffixes) {
        if (prefixSuffixes == null || prefixSuffixes.length == 0) {
            return content;
        }
        if (prefixSuffixes.length > 1) {
            for (int i = 0; i < prefixSuffixes.length - 1; i++) {
                PrefixSuffix prefixSuffix = prefixSuffixes[i];
                content = getWholeText(content,
                        prefixSuffix.prefix,
                        prefixSuffix.suffix,
                        prefixSuffix.lazyPrefix,
                        prefixSuffix.lazySuffix);
            }
        }
        PrefixSuffix prefixSuffix = prefixSuffixes[prefixSuffixes.length-1];
        String text = getMiddleText(content,
                prefixSuffix.prefix,
                prefixSuffix.suffix,
                prefixSuffix.lazyPrefix,
                prefixSuffix.lazySuffix);
        return StringUtils.trim(text);
    }

    /**
     * 获取字符串中介于prefix和suffix之间的字符串 然后拼接上prefix和suffix返回
     * @param content 原字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @Param lazyPrefix 是否使用第一次找到的前缀作为截取前缀
     * @Param lazySuffix 是否使用第一次找到的后缀作为截取后缀
     * @return 没有找到返回null。
     */
    public static String getWholeText(String content, String prefix, String suffix, boolean lazyPrefix, boolean lazySuffix) {
        String middleText = getMiddleText(content, prefix, suffix, lazyPrefix, lazySuffix);
        if (middleText != null) {
            return new StringBuilder().append(StringUtils.defaultString(prefix)).append(middleText).append(StringUtils.defaultString(suffix)).toString();
        }
        return null;
    }

    /**
     * 获取字符串中介于prefix和suffix之间的字符串 然后拼接上prefix和suffix返回
     * @param content 原字符串
     * @param prefix 前缀
     * @param suffix 后缀
     * @return 没有找到返回null。
     */
    public static String getWholeText(String content, String prefix, String suffix) {
        return getWholeText(content, prefix, suffix, true, true);
    }

    /**
     * 获取一个字符串中第一次出现的数字连续字符串
     * @param text 原字符串
     * @return 没有找到返回null
     */
    public static String getFirstNumberFromText(String text) {
        return getFirstNumberFromText(text, true);
    }

    /**
     * 获取一个字符串中第一次出现的数字连续字符串
     * @param text 原字符串
     * @param justNumbers 是否要求纯数字（没有小数点）
     * @return 没有找到返回null
     */
    public static String getFirstNumberFromText(String text, boolean justNumbers) {
        if (StringUtils.isNotBlank(text)) {
            String regex = justNumbers ? "\\d+" : NUMBERS_REGEX;
            Matcher numberMatcher = Pattern.compile(regex).matcher(text);
            if (numberMatcher.find()) {
                String number = numberMatcher.group();
                return number.replace(",", "");
            }
        }
        return "";
    }

    /**
     * 获取uuid
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取随机Spider的uuid
     * @return
     */
    public static String getSpiderUUID() {
        return "Spider" + RandomUtils.nextInt(10000, 100000);
    }
}

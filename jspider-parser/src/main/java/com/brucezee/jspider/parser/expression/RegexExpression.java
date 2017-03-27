package com.brucezee.jspider.parser.expression;

import com.brucezee.jspider.common.utils.SpiderStrUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 多个正则表达式组合截取字符串
 * Created by zhoubing on 2016/11/30.
 */
public class RegexExpression {
    private String regex;
    private int groupCount = 0;//默认0
    private boolean isRelative = false;

    private static final String REGEX_KEY = "regex";
    private static final String RELATIVE_PREFIX = " ";//相对路径的正则 前面使用空格作为标志

//    regex[0]:aaaaaaaa regex[1]:ffffffffff regex[3]:ddddddddd regex:eeeeeeee regex[0]:iiiiiiiii
//    以regex为前缀或不使用前缀，regex[1]表示取分组中的第1项分组内容 regex不加分组标记默认取第0项分组内容，多个表达式用空格隔开
//    整个表达式的前面以空格开头表示相对表达式


    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public int getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(int groupCount) {
        this.groupCount = groupCount;
    }

    public boolean isRelative() {
        return isRelative;
    }

    public void setRelative(boolean isRelative) {
        this.isRelative = isRelative;
    }

    public static boolean isRegexExpression(String selector) {
        return selector != null && selector.trim().startsWith(REGEX_KEY);
    }

    //regex[0]:aaaaaaaa regex[1]:ffffffffff regex[3]:ddddddddd regex:eeeeeeee regex[0]:iiiiiiiii

    public static List<RegexExpression> parse(String selector) {
        List<RegexExpression> selectorList = new LinkedList<RegexExpression>();
        while(selector.length() > 0) {
            int index = selector.indexOf(REGEX_KEY);
            if (index < 0) {
                if (selectorList.size() == 0) {
                    RegexExpression regexExpression = new RegexExpression();
                    regexExpression.setRelative(selector.startsWith(RELATIVE_PREFIX));
                    regexExpression.setRegex(selector.trim());
                    selectorList.add(regexExpression);
                }
                break;
            }
            boolean isRelative = selector.startsWith(RELATIVE_PREFIX);
            selector = selector.substring(index+5);

            int groupCount = 0;
            String regex = null;

            if (selector.startsWith("[")) {
                groupCount = Integer.parseInt(SpiderStrUtils.getMiddleText(selector, "[", "]"));
                selector = selector.substring(selector.indexOf("]")+1);
            }
            if (selector.startsWith(":")) {
                index = selector.indexOf(REGEX_KEY);
                if (index > 0) {
                    regex = selector.substring(1, index).trim();
                    selector = selector.substring(index);
                } else {
                    selector = selector.substring(1);
                    regex = selector.trim();
                }

                RegexExpression regexExpression = new RegexExpression();
                regexExpression.setGroupCount(groupCount);
                regexExpression.setRelative(isRelative);
                regexExpression.setRegex(regex);
                selectorList.add(regexExpression);
            } else {
                throw new IllegalArgumentException("illegal regex expression "+selector);
            }
        }

        return selectorList.size() > 0 ? selectorList : null;
    }

    public static List<String> matcher(String html, String childText, String selector) {
        List<RegexExpression> selectorList = parse(selector);
        if (selectorList == null || selectorList.size() == 0) {
            return null;
        }
        RegexExpression regexExpression = null;
        Matcher matcher = null;
        String text = null;
        if (selectorList.size() == 1) {
            regexExpression = selectorList.get(selectorList.size()-1);
            text = regexExpression.isRelative() ? childText : html;
            matcher = Pattern.compile(regexExpression.getRegex()).matcher(text);
        } else if (selectorList.size() > 1) {
            for (int i = 0; i < selectorList.size() - 1; i++) {
                regexExpression = selectorList.get(i);

                if (text == null) {
                    text = regexExpression.isRelative() ? childText : html;
                }

                matcher = Pattern.compile(regexExpression.getRegex()).matcher(text);
                if (matcher.find()) {
                    //中间过程文本
                    text = matcher.group(regexExpression.getGroupCount());
                } else {
                    //没有找到匹配
                    text = null;
                    break;
                }
            }

            if (text == null) {
                return null;
            }

            regexExpression = selectorList.get(selectorList.size()-1);
            matcher = Pattern.compile(regexExpression.getRegex()).matcher(text);
        }

        List<String> list = new LinkedList<String>();
        while (matcher.find()) {
            list.add(matcher.group(regexExpression.getGroupCount()));
        }
        return list.size() > 0 ? list : null;
    }
}

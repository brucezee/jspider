package com.brucezee.jspider.parser.expression;

import com.brucezee.jspider.common.utils.PrefixSuffix;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * 分段截取字符串
 * Created by zhoubing on 2016/12/1.
 */
public class SegmentExpression {
    private static final String SEGMENT_KEY = "segment";
    private static final String RELATIVE_PREFIX = " ";//相对路径 前面使用空格作为标志

//    segment:adfajsdfasdfasdf($TT)jjjjjjjjjjjjjjjjj segment:tttttttttttt($TF)mmmmmmmmmmmmmm
//    lazy=true表示找到之后即停止 lazy=false表示找到之后继续找直到再也找不到为止
//        ($TT)  lazyPrefix=true,   lazySuffix=true  最前最小
//        ($)    lazyPrefix=true,   lazySuffix=true  最前最小
//        ($TF)  lazyPrefix=true,   lazySuffix=false 最前最大
//        ($FF)  lazyPrefix=false,  lazySuffix=false 最后最大
//        ($FT)  lazyPrefix=false,  lazySuffix=true  最后最小
//    多个表达式用空格隔开
//    整个表达式的前面以空格开头表示相对表达式


    public static boolean isSegmentExpression(String selector) {
        return selector != null && selector.trim().startsWith(SEGMENT_KEY);
    }

    public static String matcher(String html, String childText, String selector) {
        List<PrefixSuffix> prefixSuffixList = parse(selector);
        if (prefixSuffixList == null || prefixSuffixList.size() == 0) {
            return null;
        }

        String text = selector.startsWith(RELATIVE_PREFIX) ? childText : html;
        return SpiderStrUtils.getMiddleText(text, prefixSuffixList.toArray(new PrefixSuffix[prefixSuffixList.size()]));
    }

    public static List<PrefixSuffix> parse(String selector) {
        String[] array = selector.split(SEGMENT_KEY+":");
        List<PrefixSuffix> list = new LinkedList<PrefixSuffix>();
        for (String str : array) {
            if (StringUtils.isBlank(str)) {
                continue;
            }
            str = str.trim();
            String[] strArray = str.split("\\(\\$TT\\)|\\(\\$FF\\)|\\(\\$TF\\)|\\(\\$FT\\)|\\(\\$\\)");
            if (strArray.length != 2) {
                throw new IllegalArgumentException("illegal segment expression "+str);
            }
            PrefixSuffix prefixSuffix = new PrefixSuffix(strArray[0], strArray[1]);
            boolean flag = str.contains("($TT)") || str.contains("($)");// ($) = ($TT)
            prefixSuffix.lazyPrefix = flag || str.contains("($TF)");
            prefixSuffix.lazySuffix = flag || str.contains("($FT)");
            list.add(prefixSuffix);
        }
        return list.size() > 0 ? list : null;
    }
}

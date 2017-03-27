package com.brucezee.jspider.paging;

import com.brucezee.jspider.common.utils.SpiderStrUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 条件占位符
 * Created by brucezee on 2017/1/19.
 */
public class DefaultValuesHolder {
    //处理如：{1->5}表示1到5，{A|B}表示A，B

    public boolean containsHolder(String text) {
        return text != null && text.contains("{") || text.contains("}");
    }

    public boolean isValidHolder(String holder) {
        return holder != null && holder.startsWith("{") && holder.endsWith("}");
    }

    public List<String> getValues(String holder) {
        if (!isValidHolder(holder)) {
            return Arrays.asList(holder);
        }

        if (holder.contains("|")) {
            String text = SpiderStrUtils.getMiddleText(holder, "{", "}", true, false);

            // {|A}和{A|} 两种情况在split的时候出来的结果不同
            String[] array = text.split("\\|");

            List<String> list = null;
            if (holder.contains("->")) {
                list = new ArrayList<String>();
                for (String each : array) {
                    if (!each.contains("->")) {
                        list.add(each);
                        continue;
                    }

                    Matcher matcher = Pattern.compile("(\\d+)->(\\d+)").matcher(each);
                    if (!matcher.find()) {
                        throw new IllegalArgumentException("Illegal expression : " + each);
                    }

                    list.addAll(makeStringList(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))));
                }
            } else {
                list = Arrays.asList(array).stream().collect(Collectors.toList());
            }

            if (text.endsWith("|")) {
                list.add("");
            }

            return list.stream().distinct().collect(Collectors.toList());
        } else {
            if (holder.contains("->")) {
                Matcher matcher = Pattern.compile("\\{(\\d+)->(\\d+)\\}").matcher(holder);
                if (!matcher.find()) {
                    throw new IllegalArgumentException("Illegal expression : " + holder);
                }
                return makeStringList(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
            } else {
                String text = SpiderStrUtils.getMiddleText(holder, "{", "}", true, false);
                return Arrays.asList(text);
            }
        }
    }

    private List<String> makeStringList(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("Illegal expression : " + min + "->" + max);
        }
        List<String> list = new ArrayList<String>(max - min + 1);
        for (int i = min; i <= max; i++) {
            list.add(i + "");
        }
        return list;
    }

    public Map<String, List<String>> getValuesMap(String holderText) {
        Map<String, List<String>> valuesMap = new HashMap<String, List<String>>();
        Matcher matcher = Pattern.compile("\\{(.*?)\\}").matcher(holderText);
        while (matcher.find()) {
            String text = matcher.group();
            if (!isValidHolder(text)) {
                continue;
            }
            List<String> list = getValues(text);
            valuesMap.put(text, list);
        }
        return valuesMap;
    }
}

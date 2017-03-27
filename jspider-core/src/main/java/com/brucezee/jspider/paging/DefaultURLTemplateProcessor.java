package com.brucezee.jspider.paging;

import com.google.common.collect.Lists;
import com.brucezee.jspider.common.utils.SpiderStrUtils;

import java.util.*;

/**
 * 有占位符的url处理器
 * Created by brucezee on 2017/1/19.
 */
public class DefaultURLTemplateProcessor implements URLTemplateProcessor {

//     URL占位符规则
//
//     {A|B|C} 多个参数合集 表示A，B，C
//     http://www.baidu.com/{user|home}/?type={1|2|4}
//     {1->10} 连续数字合集 表示[1,10]
//     http://www.baidu.com/{user|home}/?type={1->5}
//     {|A|B} {A|B|} 包含空串的多个参数合集 表示A，B以及空字符串
//     http://www.baidu.com/{user|home|}?type={1|2|4}
//     http://www.baidu.com/{|user|home}?type={1|2|4}
//     {0|3->5|7->8|} 单个参数与连续数字组合合集
//     http://www.baidu.com/{|user|home}?type={{0|3->5|7->8}}


    private DefaultValuesHolder valuesHolder;

    public DefaultURLTemplateProcessor(DefaultValuesHolder valuesHolder) {
        this.valuesHolder = valuesHolder;
    }

    public DefaultURLTemplateProcessor() {
        this(new DefaultValuesHolder());
    }

    /**
     * 根据有占位符的url模板生成实际的多个url列表
     * @param templateUrl 具有占位符的url模板
     * @return 多个url列表
     */
    @Override
    public List<String> process(String templateUrl) {
        if (!valuesHolder.containsHolder(templateUrl)) {
            return Arrays.asList(templateUrl);
        }

        Map<String, List<String>> valuesMap = getValuesMapFromUrls(templateUrl);
        List<String> urls = new LinkedList<String>();
        for (Map.Entry<String, List<String>> entry : valuesMap.entrySet()) {
            List<String> valueList = entry.getValue();

            if (urls.isEmpty()) {
                //初始化的时候 直接替换url后添加到列表
                for (String value : valueList) {
                    urls.add(templateUrl.replace(entry.getKey(), value));
                }
            } else {
                //保留原列表，后面需要删除原列表
                List<String> oldList = Lists.newCopyOnWriteArrayList(urls);
                for (int i = urls.size() - 1; i >= 0; i--) {
                    for (String value : valueList) {
                        //遍历元列表，并根据占位符对应的值列表进行替换，并添加的列表中
                        urls.add(urls.get(i).replace(entry.getKey(), value));
                    }
                }
                //删除原来的列表
                urls.removeAll(oldList);
            }
        }

        return urls;
    }

    private Map<String, List<String>> getValuesMapFromUrls(String templateUrl) {
        int index = templateUrl.indexOf("?");
        if (index < 0) {
            //没有参数的url，直接从url中获取占位符的值map
            return valuesHolder.getValuesMap(templateUrl);
        }

        //先获取没带参数的url的占位符的值map
        Map<String, List<String>> valuesMap = valuesHolder.getValuesMap(templateUrl.substring(0, index));

        //再处理参数部分的占位符的值map
        templateUrl = templateUrl.substring(index + 1);
        StringTokenizer tokenizer = new StringTokenizer(templateUrl, "&");
        while(tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String key = SpiderStrUtils.getMiddleText(token, null, "=");
            String value = SpiderStrUtils.getMiddleText(token, "=", null);
            if (!valuesHolder.isValidHolder(value)) {
                continue;
            }
            List<String> list = valuesHolder.getValues(value);
            addListItemPrefix(list, key + "=");
            valuesMap.put(token, list);
        }
        return valuesMap;
    }

    private void addListItemPrefix(List<String> list, String prefix) {
        //为整个列表的每一项添加统一前缀
        for (int i = list.size() - 1; i >= 0; i--) {
            list.set(i, prefix + list.get(i));
        }
    }

    public static void main(String[] args) throws Exception {
//        List<String> list = new DefaultURLTemplateProcessor().process("http://www.nianshao.{me|com}/{home|ok|cc}?stype={1->2}");
        List<String> list = new DefaultURLTemplateProcessor().process("http://www.nianshao.{me|com}/?stype={0|2->2|2->4}");
        System.out.println(list);
    }
}

package com.brucezee.jspider.common;

import com.brucezee.jspider.common.utils.SpiderUrlUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 各个网站对应的相应字符集
 * Created by brucezee on 2017/1/9.
 */
public class Charsets {
    private Map<String, String> charsets = new HashMap<String, String>(2);

    public static Charsets create() {
        return new Charsets();
    }

    public static Charsets create(String charset) {
        Charsets s = new Charsets();
        s.setCommonCharset(charset);
        return s;
    }

    /**
     * 设置域名对应字符集
     * @param host 域名关键字
     * @param charset 字符集
     */
    public void put(String host, String charset) {
        charsets.put(host, charset);
    }

    /**
     * 获取某个域名对应的字符集
     * @param host 域名关键字
     * @return 失败返回null
     */
    public String get(String host) {
        String ch = charsets.get(host);
        return ch != null ? ch : getCommonCharset();
    }

    /**
     * 设置通用字符集
     * @param charset 字符集
     */
    public void setCommonCharset(String charset) {
        put("*", charset);
    }

    /**
     * 获取通用字符集
     * @return 通用字符集
     */
    public String getCommonCharset() {
        return charsets.get("*");
    }

    /**
     * 匹配url获取相应字符集
     * @param url 网址
     * @return 失败返回null
     */
    public String match(String url) {
        String commonCharset = getCommonCharset();
        if (commonCharset != null) {
            return commonCharset;
        }
        String host = SpiderUrlUtils.getUrlHost(url);
        if (host != null) {
            for (Map.Entry<String, String> entry : charsets.entrySet()) {
                if (host.contains(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }
}

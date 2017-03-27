package com.brucezee.jspider.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URL;

/**
 * 地址相关工具类
 * Created by brucezee on 2017/1/9.
 */
public class SpiderUrlUtils {
    /**
     * 从网址中获取host
     * @param url 网址
     * @return 失败返回null
     */
    public static String getUrlHost(String url) {
        if (StringUtils.isNoneBlank(url)) {
            try {
                URL u = new URL(url);
                return u.getHost();
            } catch (Exception e) {
            }
        }
        return null;
    }
}

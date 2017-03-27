package com.brucezee.jspider.selenium;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.SiteConfig;

/**
 * 请求响应等待器
 * Created by brucezee on 2017/1/12.
 */
public abstract class RequestWaiter {
    /**
     * 请求发出后等待响应结果
     * @param siteConfig 网络请求配置
     * @param request 请求任务
     * @param webDriver 具体的WebDriver实例
     */
    public abstract void waitResponse(SiteConfig siteConfig, Request request, WebDriverEx webDriver);
}

package com.brucezee.jspider.selenium;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.SiteConfig;

/**
 * 默认请求响应等待器
 * Created by brucezee on 2017/1/12.
 */
public class DefaultRequestWaiter extends RequestWaiter {
    @Override
    public void waitResponse(SiteConfig siteConfig, Request request, WebDriverEx webDriver) {
        webDriver.waitWithTitleAndDelayed(null, siteConfig.getSocketTimeout(), 1000);
    }
}

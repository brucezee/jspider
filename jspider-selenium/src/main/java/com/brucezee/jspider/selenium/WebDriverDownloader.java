package com.brucezee.jspider.selenium;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.SiteConfig;
import com.brucezee.jspider.downloader.Downloader;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.openqa.selenium.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 基于Selenium的WebDriver下载器
 * Created by brucezee on 2017/1/11.
 */
public class WebDriverDownloader implements Downloader, Closeable {
    private static Logger logger = LoggerFactory.getLogger(WebDriverDownloader.class);
    private WebDriverPool webDriverPool;    //WebDriver池
    private RequestWaiter requestWaiter;    //请求等待器
    private DriverConfig driverConfig;

    public WebDriverDownloader() {
        this(new WebDriverPool(new WebDriverFactory()));
    }

    public WebDriverDownloader(WebDriverPool webDriverPool) {
        this(webDriverPool, DriverConfig.create());
    }

    public WebDriverDownloader(WebDriverPool webDriverPool, DriverConfig driverConfig) {
        this(webDriverPool, new DefaultRequestWaiter(), driverConfig);
    }

    public WebDriverDownloader(WebDriverPool webDriverPool, RequestWaiter requestWaiter, DriverConfig driverConfig) {
        this.webDriverPool = webDriverPool;
        this.requestWaiter = requestWaiter;
        this.driverConfig = driverConfig;
    }

    @Override
    public Page download(SiteConfig siteConfig, Request request) {
        WebDriverEx webDriver = null;
        try {
            webDriver = webDriverPool.getWebDriver(siteConfig, driverConfig, request);
        } catch (Exception e) {
            logger.error("Failed to get web driver from pool, url : {} {}", request.getUrl(), e);
        }

        if (webDriver == null) {
            return processFailedPage(request);
        }

        try {
            webDriver.get(request.getUrl());
            requestWaiter.waitResponse(
                    siteConfig, request, webDriver);
        } catch (Exception e) {
            logger.error("Failed to request by web driver, url : {} {}", request.getUrl(), e);
        }

        try {
            return processPage(request, webDriver);
        } catch (Exception e) {
            logger.error("Failed to process page by web driver, url : {} {}", request.getUrl(), e);
            return processFailedPage(request);
        } finally {
            webDriverPool.shutdownOrReturn(webDriver, request, driverConfig.getExpiresMillis());
        }
    }

    private Page processFailedPage(Request request) {
        return new Page(request.getUrl(), 0, null, null);
    }

    private Page processPage(Request request, WebDriverEx webDriver) {
        Header[] headers = getHeaderFromCookieSet(webDriver.manage().getCookies());
        String resource = webDriver.getPageSource();
        return new Page(request.getUrl(), 200, headers, resource);
    }

    private Header[] getHeaderFromCookieSet(Set<Cookie> cookieSet) {
        if (cookieSet != null && cookieSet.size() > 0) {
            List<Header> headers = new ArrayList<Header>(cookieSet.size());
            for (Cookie cookie : cookieSet) {
                headers.add(new BasicHeader("Set-Cookie", cookie.toString()));
            }
            return headers.toArray(new Header[headers.size()]);
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        webDriverPool.shutdown();
    }
}

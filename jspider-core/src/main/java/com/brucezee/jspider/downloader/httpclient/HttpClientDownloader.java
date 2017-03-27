package com.brucezee.jspider.downloader.httpclient;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.SiteConfig;
import com.brucezee.jspider.downloader.CookieStorePool;
import com.brucezee.jspider.downloader.Downloader;
import com.brucezee.jspider.downloader.httpclient.response.Response;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用HttpClient的下载器
 * Created by brucezee on 2017/1/6.
 */
public class HttpClientDownloader implements Downloader {
    private static Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);

    private HttpClientPool httpClientPool;
    private HttpProxyPool httpProxyPool;
    private CookieStorePool cookieStorePool;

    public HttpClientDownloader() {
        this(new DefaultHttpClientPool(new HttpClientFactory()));
    }
    public HttpClientDownloader(HttpClientPool httpClientPool) {
        this(httpClientPool, null, null);
    }
    public HttpClientDownloader(HttpClientPool httpClientPool, HttpProxyPool httpProxyPool, CookieStorePool cookieStorePool) {
        this.httpClientPool = httpClientPool;
        this.httpProxyPool = httpProxyPool;
        this.cookieStorePool = cookieStorePool;
    }

    @Override
    public Page download(SiteConfig siteConfig, Request request) {
        HttpClientExecutor executor = new HttpClientExecutor(
                httpClientPool,
                httpProxyPool,
                cookieStorePool,
                siteConfig,
                request);

        Response response = executor.execute();
        if (response.isException()) {
            logger.error("download exception, url : {} {}", request.getUrl(), response.getException().getMessage());
        } else if (!response.isSuccess()) {
            logger.error("download failed, url : {}", request.getUrl());
        }

        return new Page(request.getUrl(),
                response.getStatusCode(),
                response.getHeaders(),
                response.getResult());
    }

    public HttpClientPool getHttpClientPool() {
        return httpClientPool;
    }

    public HttpProxyPool getHttpProxyPool() {
        return httpProxyPool;
    }

    public void setHttpProxyPool(HttpProxyPool httpProxyPool) {
        this.httpProxyPool = httpProxyPool;
    }

    public CookieStorePool getCookieStorePool() {
        return cookieStorePool;
    }

    public void setCookieStorePool(CookieStorePool cookieStorePool) {
        this.cookieStorePool = cookieStorePool;
    }
}

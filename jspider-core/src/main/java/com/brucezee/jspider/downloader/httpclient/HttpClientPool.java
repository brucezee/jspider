package com.brucezee.jspider.downloader.httpclient;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.SiteConfig;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.Closeable;

/**
 * HttpClient池
 * Created by brucezee on 2017/1/6.
 */
public interface HttpClientPool extends Closeable {
    /**
     * 获取httpclient实例
     * @param siteConfig
     * @param request
     * @return
     */
    public CloseableHttpClient getHttpClient(SiteConfig siteConfig, Request request);

    /**
     * 创建请求
     * @param siteConfig
     * @param request
     * @param proxy
     * @return
     */
    public HttpUriRequest createHttpUriRequest(SiteConfig siteConfig, Request request, HttpHost proxy);
}

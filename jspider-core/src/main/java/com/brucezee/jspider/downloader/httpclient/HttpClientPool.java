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
     * @param siteConfig 网络请求配置
     * @param request 请求任务
     * @return HttpClient实例
     */
    public CloseableHttpClient getHttpClient(SiteConfig siteConfig, Request request);

    /**
     * 创建请求
     * @param siteConfig 网络请求配置
     * @param request 请求任务
     * @param proxy 代理
     * @return Http请求
     */
    public HttpUriRequest createHttpUriRequest(SiteConfig siteConfig, Request request, HttpHost proxy);
}

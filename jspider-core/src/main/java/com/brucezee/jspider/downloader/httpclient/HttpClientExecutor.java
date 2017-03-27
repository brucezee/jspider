package com.brucezee.jspider.downloader.httpclient;

import com.brucezee.jspider.SiteConfig;
import com.brucezee.jspider.downloader.httpclient.response.Response;
import com.brucezee.jspider.downloader.proxy.HttpProxy;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.downloader.CookieStorePool;
import com.brucezee.jspider.downloader.httpclient.response.ResponseFactory;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;

/**
 * 请求执行器
 * Created by brucezee on 2017/1/6.
 */
public class HttpClientExecutor {
    private HttpClientPool httpClientPool;
    private HttpProxyPool httpProxyPool;
    private CookieStorePool cookieStorePool;
    private SiteConfig siteConfig;
    private Request request;

    public HttpClientExecutor(HttpClientPool httpClientPool,
                              HttpProxyPool httpProxyPool,
                              CookieStorePool cookieStorePool,
                              SiteConfig siteConfig,
                              Request request) {
        this.httpClientPool = httpClientPool;
        this.httpProxyPool = httpProxyPool;
        this.cookieStorePool = cookieStorePool;
        this.siteConfig = siteConfig;
        this.request = request;
    }

    public <T> Response<T> execute() {
        HttpProxy httpProxy = getHttpProxyFromPool();
        CookieStore cookieStore = getCookieStoreFromPool();

        CloseableHttpClient httpClient = httpClientPool.getHttpClient(siteConfig, request);
        HttpUriRequest httpRequest = httpClientPool.createHttpUriRequest(siteConfig, request, createHttpHost(httpProxy));
        CloseableHttpResponse httpResponse = null;
        IOException executeException = null;
        try {
            HttpContext httpContext = createHttpContext(httpProxy, cookieStore);
            httpResponse = httpClient.execute(httpRequest, httpContext);
        } catch (IOException e) {
            executeException = e;
        }

        Response<T> response = ResponseFactory.createResponse(
                request.getResponseType(), siteConfig.getCharset(request.getUrl()));

        response.handleHttpResponse(httpResponse, executeException);

        return response;
    }

    private HttpProxy getHttpProxyFromPool() {
        return httpProxyPool != null ? httpProxyPool.getProxy(request) : null;
    }

    private CookieStore getCookieStoreFromPool() {
        return cookieStorePool != null ? cookieStorePool.getCookieStore(request) : null;
    }

    private HttpHost createHttpHost(HttpProxy httpProxy) {
        return httpProxy != null ? new HttpHost(httpProxy.getHost(), httpProxy.getPort()) : null;
    }

    protected HttpContext createHttpContext(HttpProxy httpProxy, CookieStore cookieStore) {
        HttpContext httpContext = new HttpClientContext();

        if (cookieStore != null) {
            httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
        }

        if (httpProxy != null && StringUtils.isNotBlank(httpProxy.getUsername())) {
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(new AuthScope(httpProxy.getHost(), httpProxy.getPort()),
                    new UsernamePasswordCredentials(httpProxy.getUsername(), httpProxy.getPassword()));
            httpContext.setAttribute(HttpClientContext.CREDS_PROVIDER, credentialsProvider);
        }

        return httpContext;
    }
}

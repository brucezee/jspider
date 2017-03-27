package com.brucezee.jspider.downloader.httpclient;

import com.brucezee.jspider.SiteConfig;
import com.brucezee.jspider.common.Config;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.downloader.httpclient.extended.BarrierCookieStore;
import com.brucezee.jspider.downloader.httpclient.extended.FormatLocationRedirectStrategy;
import com.brucezee.jspider.downloader.httpclient.extended.NoopCookieSpecProvider;
import com.brucezee.jspider.downloader.httpclient.extended.NoopTrustStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.RedirectStrategy;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.config.*;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLInitializationException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient的工厂
 * Created by brucezee on 2017/1/6.
 */
public class HttpClientFactory {
    public static HttpClientFactory create() {
        return new HttpClientFactory();
    }

    public CloseableHttpClient createHttpClient(SiteConfig siteConfig) {
        return createHttpClientBuilder(siteConfig).build();
    }

    protected HttpClientBuilder createHttpClientBuilder(SiteConfig siteConfig) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        httpClientBuilder.setConnectionTimeToLive(siteConfig.getConnTimeToLiveMillis(), TimeUnit.MILLISECONDS);
        httpClientBuilder.setMaxConnPerRoute(siteConfig.getMaxConnPerRoute());
        httpClientBuilder.setMaxConnTotal(siteConfig.getMaxConnTotal());
        httpClientBuilder.setUserAgent(siteConfig.getUserAgent());

        httpClientBuilder.setRetryHandler(createHttpRequestRetryHandler());
        httpClientBuilder.setRedirectStrategy(createRedirectStrategy());
        httpClientBuilder.setSSLContext(createSSLContext());
        httpClientBuilder.setSSLHostnameVerifier(createSSLHostnameVerifier());

        httpClientBuilder.setDefaultConnectionConfig(createConnectionConfig(siteConfig));
        httpClientBuilder.setDefaultSocketConfig(createSocketConfig(siteConfig));
        httpClientBuilder.setDefaultCookieSpecRegistry(createCookieSpecRegistry());
        httpClientBuilder.setDefaultCookieStore(createCookieStore());

        return httpClientBuilder;
    }

    protected CookieStore createCookieStore() {
        return new BarrierCookieStore();
    }

    protected Lookup<CookieSpecProvider> createCookieSpecRegistry() {
        CookieSpecProvider cookieSpecProvider = createCookieSpecProvider();
        Registry<CookieSpecProvider> cookieSpecRegistry = RegistryBuilder.<CookieSpecProvider>create()
                .register(NoopCookieSpecProvider.NAME, cookieSpecProvider)
                .build();
        return cookieSpecRegistry;
    }

    protected CookieSpecProvider createCookieSpecProvider() {
        return new NoopCookieSpecProvider();
    }

    protected SocketConfig createSocketConfig(SiteConfig siteConfig) {
        return createSocketConfigBuilder(siteConfig).build();
    }

    protected SocketConfig.Builder createSocketConfigBuilder(SiteConfig siteConfig) {
        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoTimeout(siteConfig.getSocketTimeout());
        socketConfigBuilder.setTcpNoDelay(siteConfig.isTcpNoDelay());
        socketConfigBuilder.setSoKeepAlive(siteConfig.isSoKeepAlive());
        return socketConfigBuilder;
    }

    protected ConnectionConfig createConnectionConfig(SiteConfig siteConfig) {
        return createConnectionConfigBuilder(siteConfig).build();
    }

    protected ConnectionConfig.Builder createConnectionConfigBuilder(SiteConfig siteConfig) {
        ConnectionConfig.Builder connectionConfigBuilder = ConnectionConfig.custom();
        //如果这里不设置字符集则在redirect的地址中如果有中文会出现乱码
        connectionConfigBuilder.setCharset(getConnectionCharset());
        connectionConfigBuilder.setBufferSize(siteConfig.getBufferSize());
        return connectionConfigBuilder;
    }

    protected Charset getConnectionCharset() {
        return Charset.forName(Config.DEFAULT_CHARSET);
    }

    protected Charset getDefaultCharset(String charset) {
        return StringUtils.isBlank(charset) ? Charset.forName(Config.DEFAULT_CHARSET) : Charset.forName(charset);
    }

    protected RedirectStrategy createRedirectStrategy() {
        return new FormatLocationRedirectStrategy();
    }

    protected HttpRequestRetryHandler createHttpRequestRetryHandler() {
        return new StandardHttpRequestRetryHandler();
    }

    protected SSLContext createSSLContext() {
        try {
            return createSSLContextBuilder().build();
        } catch (final NoSuchAlgorithmException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        } catch (final KeyManagementException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        }
    }
    
    protected SSLContextBuilder createSSLContextBuilder() {
        SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
        try {
            sslContextBuilder.loadTrustMaterial(null, createTrustStrategy());
        } catch (final NoSuchAlgorithmException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        } catch (final KeyStoreException ex) {
            throw new SSLInitializationException(ex.getMessage(), ex);
        }
        return sslContextBuilder;
    }

    protected HostnameVerifier createSSLHostnameVerifier() {
        return new NoopHostnameVerifier();
    }

    protected TrustStrategy createTrustStrategy() {
        return new NoopTrustStrategy();
    }

    public HttpUriRequest createHttpUriRequest(SiteConfig siteConfig, Request request, HttpHost proxy) {
        return createRequestBuilder(siteConfig, request, proxy).build();
    }

    public RequestBuilder createRequestBuilder(SiteConfig siteConfig, Request request, HttpHost proxy) {
        RequestConfig requestConfig = createRequestConfig(siteConfig, request, proxy);

        RequestBuilder requestBuilder = RequestBuilder.create(request.getMethod());
        requestBuilder.setConfig(requestConfig);
        requestBuilder.setCharset(getDefaultCharset(siteConfig.getCharset(request.getUrl())));
        requestBuilder.setUri(request.getUrl());
        requestBuilder.setEntity(request.entity());

        Map<String, String> parameters = request.getParameters();
        if (parameters != null && !parameters.isEmpty()) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                requestBuilder.addParameter(entry.getKey(), StringUtils.defaultString(entry.getValue()));
            }
        }

        Map<String, String> siteHeaders = siteConfig.getHeaders();
        Map<String, String> requestHeaders = request.getHeaders();
        Map<String, String> mergedHeaders = null;
        if (siteHeaders != null && requestHeaders != null) {
            siteHeaders.putAll(requestHeaders);
            mergedHeaders = siteHeaders;
        } else {
            mergedHeaders = siteHeaders != null ? siteHeaders : requestHeaders;
        }

        if (mergedHeaders != null && !mergedHeaders.isEmpty()) {
            for (Map.Entry<String, String> entry : mergedHeaders.entrySet()) {
                requestBuilder.addHeader(entry.getKey(), StringUtils.defaultString(entry.getValue()));
            }
        }

        return requestBuilder;
    }

    public RequestConfig createRequestConfig(SiteConfig siteConfig, Request request, HttpHost proxy) {
        return createRequestConfigBuilder(siteConfig, request, proxy).build();
    }

    public RequestConfig.Builder createRequestConfigBuilder(SiteConfig siteConfig, Request request, HttpHost proxy) {
        RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();

        requestConfigBuilder.setConnectTimeout(siteConfig.getConnectTimeout());
        requestConfigBuilder.setSocketTimeout(siteConfig.getSocketTimeout());
        requestConfigBuilder.setRedirectsEnabled(siteConfig.isRedirectsEnabled());
        requestConfigBuilder.setConnectionRequestTimeout(siteConfig.getConnectionRequestTimeout());
        requestConfigBuilder.setCircularRedirectsAllowed(siteConfig.isCircularRedirectsAllowed());
        requestConfigBuilder.setMaxRedirects(siteConfig.getMaxRedirects());
        requestConfigBuilder.setCookieSpec(siteConfig.getCookieSpec());
        requestConfigBuilder.setProxy(proxy);

        return requestConfigBuilder;
    }
}

package com.brucezee.jspider;

import com.brucezee.jspider.common.Charsets;
import com.brucezee.jspider.downloader.httpclient.extended.NoopCookieSpecProvider;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 网络请求配置
 * Created by brucezee on 2017/1/6.
 */
public class SiteConfig implements Serializable {
    private Map<String, String> headers = new HashMap<String, String>();    //通用请求头
    private Charsets charsets;                                              //各个网站对应字符集

    private int connectionRequestTimeout = 15000;   //连接请求超时毫秒数
    private int connectTimeout = 30000;             //连接超时毫秒数
    private int socketTimeout = 45000;              //读取超时毫秒数
    private boolean soKeepAlive = true;             //soKeepAlive
    private boolean tcpNoDelay = true;              //tcpNoDelay

    private int bufferSize = 8192;                  //缓冲字节数组大小
    private long maxIdleTimeMillis = 1000*60*60;    //最大空闲时间毫秒数
    private int maxConnTotal = 200;                 //连接池最大连接数
    private int maxConnPerRoute = 100;              //每个路由最大连接数
    private long connTimeToLiveMillis = -1;         //持久化连接最大存活时间毫秒数

    private String cookieSpec = NoopCookieSpecProvider.NAME;    //Cookie策略名
    private boolean redirectsEnabled = true;                    //是否允许自动跳转
    private boolean relativeRedirectsAllowed = true;            //是否允许相对路径跳转
    private boolean circularRedirectsAllowed = false;           //是否允许循环跳转
    private int maxRedirects = 3;                               //最大跳转数

    public static SiteConfig create() {
        return new SiteConfig()
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                .addHeader("Accept-Encoding", "gzip, deflate, sdch")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.8");
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public SiteConfig addHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public String getUserAgent() {
        return getHeader("User-Agent");
    }

    public String getHeader(String key) {
        return headers.get(key);
    }

    public Charsets getCharsets() {
        return charsets;
    }

    public SiteConfig setCharsets(Charsets charsets) {
        this.charsets = charsets;
        return this;
    }

    public String getCharset(String url) {
        if (charsets != null) {
            return charsets.match(url);
        }
        return null;
    }

    public SiteConfig putCharset(String host, String charset) {
        if (charsets == null) {
            charsets = new Charsets();
        }
        charsets.put(host, charset);
        return this;
    }

    public int getConnectionRequestTimeout() {
        return connectionRequestTimeout;
    }

    public SiteConfig setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public SiteConfig setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public SiteConfig setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
        return this;
    }

    public boolean isSoKeepAlive() {
        return soKeepAlive;
    }

    public SiteConfig setSoKeepAlive(boolean soKeepAlive) {
        this.soKeepAlive = soKeepAlive;
        return this;
    }

    public boolean isTcpNoDelay() {
        return tcpNoDelay;
    }

    public SiteConfig setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
        return this;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public SiteConfig setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public long getMaxIdleTimeMillis() {
        return maxIdleTimeMillis;
    }

    public SiteConfig setMaxIdleTimeMillis(long maxIdleTimeMillis) {
        this.maxIdleTimeMillis = maxIdleTimeMillis;
        return this;
    }

    public int getMaxConnTotal() {
        return maxConnTotal;
    }

    public SiteConfig setMaxConnTotal(int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
        return this;
    }

    public int getMaxConnPerRoute() {
        return maxConnPerRoute;
    }

    public SiteConfig setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
        return this;
    }

    public long getConnTimeToLiveMillis() {
        return connTimeToLiveMillis;
    }

    public SiteConfig setConnTimeToLiveMillis(long connTimeToLiveMillis) {
        this.connTimeToLiveMillis = connTimeToLiveMillis;
        return this;
    }

    public String getCookieSpec() {
        return cookieSpec;
    }

    public SiteConfig setCookieSpec(String cookieSpec) {
        this.cookieSpec = cookieSpec;
        return this;
    }

    public boolean isRedirectsEnabled() {
        return redirectsEnabled;
    }

    public SiteConfig setRedirectsEnabled(boolean redirectsEnabled) {
        this.redirectsEnabled = redirectsEnabled;
        return this;
    }

    public boolean isRelativeRedirectsAllowed() {
        return relativeRedirectsAllowed;
    }

    public SiteConfig setRelativeRedirectsAllowed(boolean relativeRedirectsAllowed) {
        this.relativeRedirectsAllowed = relativeRedirectsAllowed;
        return this;
    }

    public boolean isCircularRedirectsAllowed() {
        return circularRedirectsAllowed;
    }

    public SiteConfig setCircularRedirectsAllowed(boolean circularRedirectsAllowed) {
        this.circularRedirectsAllowed = circularRedirectsAllowed;
        return this;
    }

    public int getMaxRedirects() {
        return maxRedirects;
    }

    public SiteConfig setMaxRedirects(int maxRedirects) {
        this.maxRedirects = maxRedirects;
        return this;
    }
}

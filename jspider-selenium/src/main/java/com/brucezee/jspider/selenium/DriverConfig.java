package com.brucezee.jspider.selenium;

/**
 * Created by brucezee on 2017/1/12.
 */
public class DriverConfig {
    private long implicitlyWait = 10000;//查找对象的超时时间
    private long pageLoadTimeout = 45000;//页面完全加载的超时时间
    private long scriptTimeout = 45000;//异步脚本的超时时间
    private long expiresMillis = 60 * 60 * 1000;//一个web driver实例的过期时间，过期后重新创建

    public static DriverConfig create() {
        return new DriverConfig();
    }

    public long getImplicitlyWait() {
        return implicitlyWait;
    }

    public void setImplicitlyWait(long implicitlyWait) {
        this.implicitlyWait = implicitlyWait;
    }

    public long getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setPageLoadTimeout(long pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public long getScriptTimeout() {
        return scriptTimeout;
    }

    public void setScriptTimeout(long scriptTimeout) {
        this.scriptTimeout = scriptTimeout;
    }

    public long getExpiresMillis() {
        return expiresMillis;
    }

    public void setExpiresMillis(long expiresMillis) {
        this.expiresMillis = expiresMillis;
    }
}

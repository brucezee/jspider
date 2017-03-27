package com.brucezee.jspider.downloader.proxy;

import com.brucezee.jspider.Request;

import java.util.List;

/**
 * 默认从列表中获取代理的代理池
 * Created by brucezee on 2017/1/20.
 */
public class DefaultHttpProxyPool extends HttpProxyPool {
    private List<HttpProxy> httpProxies;    //可用代理列表
    private ProxyStrategy proxyStrategy;    //代理策略

    public DefaultHttpProxyPool(List<HttpProxy> httpProxies, ProxyStrategy proxyStrategy) {
        this.httpProxies = httpProxies;
        this.proxyStrategy = proxyStrategy;
    }

    @Override
    public HttpProxy getProxy(Request request) {
        return proxyStrategy.getProxy(httpProxies);
    }

    @Override
    public void returnProxy(Request request, int statusCode) {
    }
}

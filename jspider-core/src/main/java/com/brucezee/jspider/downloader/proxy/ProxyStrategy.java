package com.brucezee.jspider.downloader.proxy;

import java.util.List;

/**
 * 代理获取策略
 * Created by brucezee on 2017/1/20.
 */
public interface ProxyStrategy {
    /**
     * 根据策略在列表中获取代理
     * @param httpProxies 代理列表
     * @return 代理
     */
    public HttpProxy getProxy(List<HttpProxy> httpProxies);
}

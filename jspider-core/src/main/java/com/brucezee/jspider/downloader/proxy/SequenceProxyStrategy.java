package com.brucezee.jspider.downloader.proxy;

import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 顺序获取代理的策略
 * Created by brucezee on 2017/1/20.
 */
public class SequenceProxyStrategy implements ProxyStrategy {
    private AtomicInteger index = new AtomicInteger(0); //计数器

    @Override
    public HttpProxy getProxy(List<HttpProxy> httpProxies) {
        if (CollectionUtils.isNotEmpty(httpProxies)) {
            return httpProxies.get(index.getAndIncrement() % httpProxies.size());
        }
        return null;
    }
}

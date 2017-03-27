package com.brucezee.jspider.downloader.proxy;

import java.util.Arrays;
import java.util.List;

/**
 * 默认代理池工厂
 * Created by brucezee on 2017/1/20.
 */
public class HttpProxyPoolFactory {
    /**
     * 创建随机获取代理的代理池
     * @param httpProxies 代理列表
     * @return 随机获取的代理
     */
    public static HttpProxyPool createRandomHttpProxyPool(HttpProxy... httpProxies) {
        return createRandomHttpProxyPool(Arrays.asList(httpProxies));
    }

    /**
     * 创建随机获取代理的代理池
     * @param httpProxies 代理列表
     * @return 随机获取的代理
     */
    public static HttpProxyPool createRandomHttpProxyPool(List<HttpProxy> httpProxies) {
        return new DefaultHttpProxyPool(httpProxies, new RandomProxyStrategy());
    }

    /**
     * 创建顺序获取代理的代理池
     * @param httpProxies 代理列表
     * @return 顺序获取的代理
     */
    public static HttpProxyPool createSequenceHttpProxyPool(HttpProxy... httpProxies) {
        return createRandomHttpProxyPool(Arrays.asList(httpProxies));
    }

    /**
     * 创建顺序获取代理的代理池
     * @param httpProxies 代理列表
     * @return 顺序获取的代理
     */
    public static HttpProxyPool createSequenceHttpProxyPool(List<HttpProxy> httpProxies) {
        return new DefaultHttpProxyPool(httpProxies, new SequenceProxyStrategy());
    }


}

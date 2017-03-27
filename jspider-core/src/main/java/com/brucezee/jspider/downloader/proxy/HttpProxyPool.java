package com.brucezee.jspider.downloader.proxy;

import com.brucezee.jspider.Request;

/**
 * Http代理池
 * Created by brucezee on 2017/1/10.
 */
public abstract class HttpProxyPool {
    /**
     * 获取请求对应的代理
     * @param request 请求
     * @return 相应的代理
     */
    public abstract HttpProxy getProxy(Request request);

    /**
     * 将请求对应的代理还给代理池
     * @param request 请求
     * @param statusCode 状态码
     */
    public abstract void returnProxy(Request request, int statusCode);
}

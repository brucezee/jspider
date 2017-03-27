package com.brucezee.jspider.downloader;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.SiteConfig;
import com.brucezee.jspider.Request;

/**
 * 请求任务下载器
 * Created by brucezee on 2017/1/4.
 */
public interface Downloader {
    /**
     * 访问并下载页面
     * @param siteConfig 请求配置
     * @param request 请求
     * @return 请求响应内容
     */
    public Page download(SiteConfig siteConfig, Request request);
}

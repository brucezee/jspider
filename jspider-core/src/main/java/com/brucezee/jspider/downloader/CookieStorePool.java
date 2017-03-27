package com.brucezee.jspider.downloader;

import com.brucezee.jspider.Request;
import org.apache.http.client.CookieStore;

/**
 * CookieStore池
 * Created by brucezee on 2017/1/10.
 */
public abstract class CookieStorePool {
    /**
     * 根据请求任务获取CookieStore
     * @param request 请求
     * @return 合适的CookieStore
     */
    public abstract CookieStore getCookieStore(Request request);
}

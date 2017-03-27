package com.brucezee.jspider.monitor;

import com.brucezee.jspider.Result;
import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.SpiderListener;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 监控类型的爬虫监听
 * Created by brucezee on 2017/1/11.
 */
public class MonitorSpiderListener implements SpiderListener {
    private AtomicInteger successCount = new AtomicInteger(0);  //成功计数器
    private AtomicInteger errorCount = new AtomicInteger(0);    //失败计数器

    @Override
    public void onSuccess(Request request, Page page, Result result) {
        successCount.incrementAndGet();
    }

    @Override
    public void onError(Request request, Page page) {
        errorCount.incrementAndGet();
    }

    public int getSuccessCount() {
        return successCount.get();
    }

    public int getErrorCount() {
        return errorCount.get();
    }
}

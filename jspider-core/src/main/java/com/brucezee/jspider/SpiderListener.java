package com.brucezee.jspider;


/**
 * 爬虫任务监听器
 * Created by brucezee on 2017/1/5.
 */
public interface SpiderListener {
    /**
     * 请求任务成功执行
     * @param request 请求
     * @param page 响应内容
     * @param result 处理结果
     */
    public void onSuccess(Request request, Page page, Result result);

    /**
     * 请求任务执行失败
     * @param request 请求
     * @param page 响应内容
     */
    public void onError(Request request, Page page);
}

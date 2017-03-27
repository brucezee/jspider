package com.brucezee.jspider.scheduler.handler;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;

/**
 * 请求任务去重
 * Created by brucezee on 2017/1/7.
 */
public interface RepeatHandler {
    /**
     * 是否重复
     * @param task 爬虫任务
     * @param request 请求
     * @return 是否重复
     */
    public boolean isDuplicate(Task task, Request request);

    /**
     * 添加重复检查
     * @param task 爬虫任务
     * @param request 请求
     */
    public void addRepeatCheck(Task task, Request request);

    /**
     * 重置全部去重检查
     * @param task 爬虫任务
     */
    public void resetAllRepeatCheck(Task task);

    /**
     * 重置指定请求的去重检查
     * @param task 爬虫任务
     * @param request 请求
     */
    public void resetRequestRepeatCheck(Task task, Request request);


}

package com.brucezee.jspider.scheduler;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;

/**
 * 请求任务调度器
 * Created by brucezee on 2017/1/4.
 */
public interface Scheduler {
    /**
     * 发布请求任务
     * @param task 任务
     * @param request 请求
     * @return 发布任务成功返回true，失败返回false
     */
    public boolean push(Task task, Request request);

    /**
     * 获取请求任务
     * @param task 任务
     * @return 可用的请求任务
     */
    public Request poll(Task task);
}

package com.brucezee.jspider.monitor;

import com.brucezee.jspider.Task;

/**
 * 任务调度器监控
 * Created by brucezee on 2017/1/11.
 */
public interface SchedulerMonitor {
    /**
     * 获取总的请求数
     * @param task 任务
     * @return 总请求数
     */
    public int getTotalCount(Task task);

    /**
     * 获取剩余请求数
     * @param task 任务
     * @return 剩余请求数
     */
    public int getLeftCount(Task task);
}

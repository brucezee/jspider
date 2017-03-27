package com.brucezee.jspider.monitor;

import java.util.Date;

/**
 * 爬虫任务状态接口bean
 * Created by brucezee on 2017/1/11.
 */
public interface SpiderStatusMXBean {
    /**
     * 获取Bean的名称
     * @return Bean的名称
     */
    public String getName();

    /**
     * 获取状态描述
     * @return 状态描述
     */
    public String getStatus();

    /**
     * 获取线程数量
     * @return 线程数量
     */
    public int getThreadCount();

    /**
     * 获取总任务数
     * @return 总任务数
     */
    public int getTotalCount();

    /**
     * 获取剩余任务数
     * @return 剩余任务数
     */
    public int getLeftCount();

    /**
     * 获取成功任务数
     * @return 成功任务数
     */
    public int getSuccessCount();

    /**
     * 获取失败任务数
     * @return 失败任务数
     */
    public int getErrorCount();

    /**
     * 获取任务开始时间
     * @return 任务开始时间
     */
    public Date getStartTime();

    /**
     * 获取任务执行速度
     * @return 任务执行速度
     */
    public String getSpeed();

    /**
     * 开始爬虫任务
     */
    public void start();

    /**
     * 结束爬虫任务
     */
    public void stop();
}

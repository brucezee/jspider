package com.brucezee.jspider;

/**
 * 任务
 * Created by brucezee on 2017/1/6.
 */
public interface Task {
    /**
     * 获取任务唯一标志
     * @return
     */
    public String getUUID();

    /**
     * 开始任务
     */
    public void start();

    /**
     * 停止任务
     */
    public void stop();
}

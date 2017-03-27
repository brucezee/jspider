package com.brucezee.jspider;

import java.io.Serializable;

/**
 * 爬虫任务配置
 * Created by brucezee on 2017/1/6.
 */
public class SpiderConfig implements Serializable {
    private String uuid;                        //任务唯一标识
    private int threadCount = 1;                //线程数量
    private boolean exitWhenComplete = false;   //任务完成后是否退出循环
    private boolean destroyWhenExit = true;     //任务结束后是否销毁所有相关实体
    private long emptySleepMillis = 30000;      //任务为空时休眠间隔
    private long closeDelayMillis = 60000;      //任务结束后销毁相关实体最长等待时间

    public static SpiderConfig create(String uuid, int threadCount) {
        return new SpiderConfig(uuid, threadCount);
    }

    public SpiderConfig(String uuid, int threadCount) {
        this.uuid = uuid;
        this.threadCount = threadCount;
    }

    public String getUUID() {
        return uuid;
    }

    public SpiderConfig setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public SpiderConfig setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    public boolean isExitWhenComplete() {
        return exitWhenComplete;
    }

    public SpiderConfig setExitWhenComplete(boolean exitWhenComplete) {
        this.exitWhenComplete = exitWhenComplete;
        return this;
    }

    public boolean isDestroyWhenExit() {
        return destroyWhenExit;
    }

    public SpiderConfig setDestroyWhenExit(boolean destroyWhenExit) {
        this.destroyWhenExit = destroyWhenExit;
        return this;
    }

    public long getEmptySleepMillis() {
        return emptySleepMillis;
    }

    public SpiderConfig setEmptySleepMillis(long emptySleepMillis) {
        this.emptySleepMillis = emptySleepMillis;
        return this;
    }

    public long getCloseDelayMillis() {
        return closeDelayMillis;
    }

    public SpiderConfig setCloseDelayMillis(long closeDelayMillis) {
        this.closeDelayMillis = closeDelayMillis;
        return this;
    }
}

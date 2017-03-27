package com.brucezee.jspider.monitor;

import com.brucezee.jspider.Spider;
import com.brucezee.jspider.scheduler.Scheduler;

import java.util.Date;

/**
 * 爬虫任务状态实现类
 * Created by brucezee on 2017/1/11.
 */
public class SpiderStatus implements SpiderStatusMXBean {
    private Spider spider;                                  //爬虫任务
    private MonitorSpiderListener monitorSpiderListener;    //监控回调

    public SpiderStatus(Spider spider, MonitorSpiderListener monitorSpiderListener) {
        this.spider = spider;
        this.monitorSpiderListener = monitorSpiderListener;
    }

    @Override
    public String getName() {
        return spider.getUUID();
    }

    @Override
    public String getStatus() {
        return spider.getStatus().name();
    }

    @Override
    public int getThreadCount() {
        return spider.getThreadAlive();
    }

    @Override
    public int getTotalCount() {
        Scheduler scheduler = spider.getScheduler();
        if (scheduler != null && scheduler instanceof SchedulerMonitor) {
            return ((SchedulerMonitor) scheduler).getTotalCount(spider);
        }
        return 0;
    }

    @Override
    public int getLeftCount() {
        Scheduler scheduler = spider.getScheduler();
        if (scheduler != null && scheduler instanceof SchedulerMonitor) {
            return ((SchedulerMonitor) scheduler).getLeftCount(spider);
        }
        return 0;
    }

    @Override
    public int getSuccessCount() {
        return monitorSpiderListener.getSuccessCount();
    }

    @Override
    public int getErrorCount() {
        return monitorSpiderListener.getErrorCount();
    }

    @Override
    public Date getStartTime() {
        return spider.getStartTime();
    }


    private long lastTime = 0;          //上次统计速度的时间
    private int lastSuccessCount = 0;   //上次成功的数量
    private double lastSpeed = 0;       //上次统计的速度
    @Override
    public String getSpeed() {
        StringBuilder sb = new StringBuilder();
        int seconds = 0;
        long current = System.currentTimeMillis();
        int currentSuccessCount = getSuccessCount();
        int successCount = currentSuccessCount;
        if (lastTime == 0) {
            seconds = (int) ((current - getStartTime().getTime()) / 1000);
        } else {
            seconds = (int) ((current - lastTime) / 1000);
            successCount = successCount - lastSuccessCount;
        }
        if (seconds > 0) {
            lastTime = current;
            lastSuccessCount = currentSuccessCount;
            lastSpeed = successCount * 1.0d / seconds;
        }
        return sb.append(String.format("%.2f", lastSpeed)).append("/s").toString();
    }

    @Override
    public void start() {
        spider.start();
    }

    @Override
    public void stop() {
        spider.stop();
    }
}

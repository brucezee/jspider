package com.brucezee.jspider.berkeley;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.monitor.SchedulerMonitor;
import com.brucezee.jspider.scheduler.Scheduler;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 基于BDB文件数据库的请求任务调度器
 * Created by brucezee on 2017/1/13.
 */
public class BdbPersistentScheduler implements Scheduler, SchedulerMonitor, Closeable {
    private BdbPersistentQueue<Request> queue;              //基于BDB实现的队列
    private AtomicInteger count = new AtomicInteger(0);     //计数器

    /**
     * 构造方法
     * @param queue 基于BDB实现的队列
     */
    public BdbPersistentScheduler(BdbPersistentQueue<Request> queue) {
        this.queue = queue;
    }

    /**
     * 构造方法
     * @param dbDir 数据库文件路径
     * @param dbName 数据库名称
     */
    public BdbPersistentScheduler(String dbDir, String dbName) {
        this(new BdbPersistentQueue(dbDir, dbName, Request.class));
    }

    @Override
    public boolean push(Task task, Request request) {
        queue.add(request);
        count.incrementAndGet();
        return true;
    }

    @Override
    public Request poll(Task task) {
        return queue.poll();
    }

    @Override
    public int getTotalCount(Task task) {
        return count.get();
    }

    @Override
    public int getLeftCount(Task task) {
        return queue.size();
    }

    @Override
    public void close() throws IOException {
        queue.close();
    }
}

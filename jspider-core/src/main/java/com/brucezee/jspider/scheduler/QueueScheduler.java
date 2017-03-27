package com.brucezee.jspider.scheduler;

import com.brucezee.jspider.scheduler.handler.HashSetRepeatHandler;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.monitor.SchedulerMonitor;
import com.brucezee.jspider.scheduler.handler.RepeatHandler;
import com.brucezee.jspider.paging.PagingRequestFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 内存无界队列请求任务调度器，同时实现任务队列监控
 * Created by brucezee on 2017/1/5.
 */
public class QueueScheduler extends NoRepeatScheduler implements SchedulerMonitor {
    private BlockingQueue<Request> queue = new LinkedBlockingQueue<Request>();  //队列
    private AtomicInteger count = new AtomicInteger(0);                         //用于监控的计数器

    public QueueScheduler() {
        this(null, null);
    }

    /**
     * 构造方法
     * @param repeatHandler 请求任务去重
     */
    public QueueScheduler(RepeatHandler repeatHandler) {
        this(repeatHandler, null);
    }

    /**
     * 构造方法
     * @param pagingRequestFactory 分页请求任务生成器
     */
    public QueueScheduler(PagingRequestFactory pagingRequestFactory) {
        this(null, pagingRequestFactory);
    }

    /**
     * 构造方法
     * @param repeatHandler 请求任务去重
     * @param pagingRequestFactory 分页请求任务生成器
     */
    public QueueScheduler(RepeatHandler repeatHandler, PagingRequestFactory pagingRequestFactory) {
        super(repeatHandler != null ? repeatHandler : new HashSetRepeatHandler(), pagingRequestFactory);
    }

    @Override
    public void pushWhenNoRepeat(Task task, Request request) {
        queue.add(request);
        count.incrementAndGet();
    }

    @Override
    public Request doPoll(Task task) {
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

}

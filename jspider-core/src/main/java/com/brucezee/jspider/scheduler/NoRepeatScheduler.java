package com.brucezee.jspider.scheduler;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.paging.PagingRequestFactory;
import com.brucezee.jspider.scheduler.handler.RepeatHandler;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 具有去重功能的请求任务调度器
 * Created by brucezee on 2017/1/7.
 */
public abstract class NoRepeatScheduler implements Scheduler, RepeatHandler {
    private ReentrantLock lock = new ReentrantLock();   //锁
    private RepeatHandler repeatHandler;                //请求去重处理器
    private PagingRequestFactory pagingRequestFactory;  //分页请求任务生成器

    /**
     * 构造方法，默认没有实现去重
     */
    public NoRepeatScheduler() {
        this(null, null);
    }

    /**
     * 构造方法
     * @param repeatHandler 请求去重处理器
     */
    public NoRepeatScheduler(RepeatHandler repeatHandler) {
        this(repeatHandler, null);
    }

    /**
     * 构造方法
     * @param repeatHandler 请求去重处理器
     * @param pagingRequestFactory 分页请求任务生成器
     */
    public NoRepeatScheduler(RepeatHandler repeatHandler, PagingRequestFactory pagingRequestFactory) {
        this.repeatHandler = repeatHandler;
        this.pagingRequestFactory = pagingRequestFactory;
    }

    @Override
    public boolean push(Task task, Request request) {
        if (shouldReserved(task, request)) {
            pushWhenNoRepeat(task, request);
            return true;
        }

        if (!isDuplicate(task, request)) {
            pushWhenNoRepeat(task, request);
            addRepeatCheck(task, request);
            return true;
        }

        return false;
    }

    /**
     * 控制任务添加
     * @param task 任务
     * @param request 请求
     * @return 是否需要强制添加
     */
    protected boolean shouldReserved(Task task, Request request) {
        return false;
    }

    /**
     * 去重后添加到任务队列
     * @param task 任务
     * @param request 请求
     */
    protected abstract void pushWhenNoRepeat(Task task, Request request);

    @Override
    public boolean isDuplicate(Task task, Request request) {
        if (repeatHandler != null) {
            return repeatHandler.isDuplicate(task, request);
        }
        return false;
    }

    @Override
    public void addRepeatCheck(Task task, Request request) {
        if (repeatHandler != null) {
            repeatHandler.addRepeatCheck(task, request);
        }
    }

    @Override
    public void resetAllRepeatCheck(Task task) {
        if (repeatHandler != null) {
            repeatHandler.resetAllRepeatCheck(task);
        }
    }

    @Override
    public void resetRequestRepeatCheck(Task task, Request request) {
        if (repeatHandler != null) {
            repeatHandler.resetRequestRepeatCheck(task, request);
        }
    }

    @Override
    public Request poll(Task task) {
        //获取请求任务
        Request request = doPoll(task);
        if (request == null) {
            //如果任务为空，根据需要重新添加任务
            if (handleEmptyPoll(task)) {
                //再次获取
                request = doPoll(task);
            }
        }
        return request;
    }

    /**
     * 获取请求任务
     * @param task 爬虫任务
     * @return 请求任务
     */
    protected abstract Request doPoll(Task task);

    /**
     * 如果获取任务返回为空，是否处理（比如添加新的任务等）
     * @param task 爬虫任务
     * @return 处理返回true，不处理返回false。
     */
    protected boolean handleEmptyPoll(Task task) {
        if (pagingRequestFactory != null) {
            try {
                lock.lock();
                List<Request> requests = pagingRequestFactory.getRequests(task);
                if (requests != null && !requests.isEmpty()) {
                    boolean success = false;
                    for (Request request : requests) {
                        success = push(task, request) || success;
                    }
                    return success;
                }
            } finally {
                lock.unlock();
            }
        }
        return false;
    }

    public RepeatHandler getRepeatHandler() {
        return repeatHandler;
    }

    public void setRepeatHandler(RepeatHandler repeatHandler) {
        this.repeatHandler = repeatHandler;
    }

    public PagingRequestFactory getPagingRequestFactory() {
        return pagingRequestFactory;
    }

    public void setPagingRequestFactory(PagingRequestFactory pagingRequestFactory) {
        this.pagingRequestFactory = pagingRequestFactory;
    }
}

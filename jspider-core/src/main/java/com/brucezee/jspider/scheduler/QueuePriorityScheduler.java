package com.brucezee.jspider.scheduler;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.monitor.SchedulerMonitor;
import com.brucezee.jspider.paging.PagingRequestFactory;
import com.brucezee.jspider.scheduler.handler.HashSetRepeatHandler;
import com.brucezee.jspider.scheduler.handler.RepeatHandler;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现优先执行的请求任务调度器
 * Created by brucezee on 2017/1/16.
 */
public class QueuePriorityScheduler extends NoRepeatScheduler implements SchedulerMonitor {
    public static final int INITIAL_CAPACITY = 11;
    private Comparator<Request> priorityComparator = new Comparator<Request>() {
        @Override
        public int compare(Request o1, Request o2) {
            return -NumberUtils.compare(o1.getPriority(), o2.getPriority());
        }
    };
    private BlockingQueue<Request> noPriorityQueue = new LinkedBlockingQueue<Request>();
    private PriorityBlockingQueue<Request> priorityQueuePlus = new PriorityBlockingQueue<Request>(INITIAL_CAPACITY, priorityComparator);
    private PriorityBlockingQueue<Request> priorityQueueMinus = new PriorityBlockingQueue<Request>(INITIAL_CAPACITY, priorityComparator);
    private AtomicInteger count = new AtomicInteger(0);

    public QueuePriorityScheduler() {
        this(null, null);
    }

    public QueuePriorityScheduler(RepeatHandler repeatHandler) {
        this(repeatHandler, null);
    }

    public QueuePriorityScheduler(PagingRequestFactory pagingRequestFactory) {
        this(null, pagingRequestFactory);
    }

    public QueuePriorityScheduler(RepeatHandler repeatHandler, PagingRequestFactory pagingRequestFactory) {
        super(repeatHandler != null ? repeatHandler : new HashSetRepeatHandler(), pagingRequestFactory);
    }

    @Override
    public void pushWhenNoRepeat(Task task, Request request) {
        int priority = request.getPriority();
        if (priority == 0) {
            noPriorityQueue.add(request);
        } else if (priority > 0) {
            priorityQueuePlus.add(request);
        } else {
            priorityQueueMinus.add(request);
        }
        count.incrementAndGet();
    }

    @Override
    public Request doPoll(Task task) {
        Request poll = priorityQueuePlus.poll();
        if (poll != null) {
            return poll;
        }
        poll = noPriorityQueue.poll();
        if (poll != null) {
            return poll;
        }
        return priorityQueueMinus.poll();
    }

    @Override
    public int getTotalCount(Task task) {
        return count.get();
    }

    @Override
    public int getLeftCount(Task task) {
        return noPriorityQueue.size() + priorityQueuePlus.size() + priorityQueueMinus.size();
    }
}


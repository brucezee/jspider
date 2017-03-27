package com.brucezee.jspider.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 线程池包装类
 * Created by brucezee on 2017/1/5.
 */
public class ThreadPool implements ExecutorService, Closeable {
    private static Logger logger = LoggerFactory.getLogger(ThreadPool.class);
    private int threadCount;                                    //线程数量
    private ExecutorService executorService;                    //实际的线程池

    private AtomicInteger threadAlive = new AtomicInteger();    //活动线程计数器
    private ReentrantLock lock = new ReentrantLock();           //锁
    private Condition condition = lock.newCondition();          //条件

    public ThreadPool(int threadCount) {
        this.threadCount = threadCount;
        this.executorService = Executors.newFixedThreadPool(threadCount);
    }

    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executorService.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executorService.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executorService.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        return executorService.submit(task, result);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return executorService.submit(task);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return executorService.invokeAll(tasks);
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return executorService.invokeAll(tasks, timeout, unit);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return executorService.invokeAny(tasks);
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return executorService.invokeAny(tasks, timeout, unit);
    }

    @Override
    public void execute(Runnable command) {
        //判断当前线程池中活动线程是否达到容量
        if (threadAlive.get() >= threadCount) {
            try {
                lock.lock();
                while (threadAlive.get() >= threadCount) {
                    //等待可用线程
                    condition.await();
                }
            } catch (InterruptedException e) {
                logger.error("wait new runnable task interrupted, error {}", e);
                return;
            } finally {
                lock.unlock();
            }
        }
        //活动线程数增加
        threadAlive.incrementAndGet();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //执行任务
                    command.run();
                } finally {
                    try {
                        lock.lock();
                        //活动线程数减少
                        threadAlive.decrementAndGet();
                        //通知
                        condition.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });
    }

    public int getThreadAlive() {
        return threadAlive.get();
    }

    public int getThreadCount() {
        return threadCount;
    }

    @Override
    public void close() throws IOException {
        shutdown();
    }
}

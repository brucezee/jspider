package com.brucezee.jspider;

import com.brucezee.jspider.common.Config;
import com.brucezee.jspider.common.ThreadPool;
import com.brucezee.jspider.common.utils.SpiderIOUtils;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import com.brucezee.jspider.common.utils.SpiderTimeUtils;
import com.brucezee.jspider.downloader.CookieStorePool;
import com.brucezee.jspider.downloader.Downloader;
import com.brucezee.jspider.downloader.httpclient.DefaultHttpClientPool;
import com.brucezee.jspider.downloader.httpclient.HttpClientDownloader;
import com.brucezee.jspider.downloader.httpclient.HttpClientFactory;
import com.brucezee.jspider.downloader.httpclient.HttpClientPool;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;
import com.brucezee.jspider.pipeline.ConsolePipeline;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.scheduler.QueueScheduler;
import com.brucezee.jspider.scheduler.Scheduler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 爬虫任务
 * Created by brucezee on 2017/1/6.
 */
public class Spider implements Runnable, Task {
    private static final Logger logger = LoggerFactory.getLogger(Spider.class);

    public static void main(String[] args) {

    }

    @Override
    public void run() {
        init();
        working();
        cleanup();
    }

    @Override
    public String getUUID() {
        return spiderConfig.getUUID();
    }

    @Override
    public void start() {
        runAsync();
    }

    @Override
    public void stop() {
        if (stat.compareAndSet(Status.Running.getValue(), Status.Stopping.getValue())) {
            logger.info("Trying to stop Spider " + getUUID() + ".");
        } else {
            logger.info("Failed stopping Spider " + getUUID() + "!");
        }
    }

    protected PageProcessor pageProcessor;
    protected Downloader downloader;
    protected Scheduler scheduler;
    protected Pipeline pipeline;
    protected SiteConfig siteConfig;
    protected SpiderConfig spiderConfig;
    protected HttpClientPool httpClientPool;
    protected HttpProxyPool httpProxyPool;
    protected CookieStorePool cookieStorePool;

    protected String uuid;
    protected int threadCount;

    protected ThreadPool threadPool;

    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    protected AtomicInteger stat = new AtomicInteger(Status.Init.getValue());

    private List<SpiderListener> spiderListeners;
    private List<Request> startRequests;
    private Date startTime;

    public static Spider create(SpiderConfig spiderConfig, SiteConfig siteConfig, PageProcessor pageProcessor) {
        return new Spider(spiderConfig, siteConfig, pageProcessor);
    }
    public static Spider create(String uuid, int threadCount, PageProcessor pageProcessor) {
        return new Spider(SpiderConfig.create(uuid, threadCount), SiteConfig.create(), pageProcessor);
    }
    public static Spider create(PageProcessor pageProcessor) {
        return new Spider(SpiderConfig.create(SpiderStrUtils.getSpiderUUID(), 1), SiteConfig.create(), pageProcessor);
    }

    public Spider(SpiderConfig spiderConfig, SiteConfig siteConfig, PageProcessor pageProcessor) {
        this.spiderConfig = spiderConfig;
        this.siteConfig = siteConfig;
        this.pageProcessor = pageProcessor;
    }

    public Spider setStartRequests(List<Request> startRequests) {
        checkIfRunning();
        this.startRequests = startRequests;
        return this;
    }

    public Spider setScheduler(Scheduler scheduler) {
        checkIfRunning();
        Scheduler oldScheduler = this.scheduler;
        this.scheduler = scheduler;
        if (oldScheduler != null) {
            Request request;
            while ((request = oldScheduler.poll(this)) != null) {
                this.scheduler.push(this, request);
            }
        }
        return this;
    }

    public Spider setPipeline(Pipeline pipeline) {
        checkIfRunning();
        this.pipeline = pipeline;
        return this;
    }

    public Spider setSiteConfig(SiteConfig siteConfig) {
        checkIfRunning();
        this.siteConfig = siteConfig;
        return this;
    }

    public Spider setSpiderConfig(SpiderConfig spiderConfig) {
        checkIfRunning();
        this.spiderConfig = spiderConfig;
        return this;
    }

    public Spider setPageProcessor(PageProcessor pageProcessor) {
        checkIfRunning();
        this.pageProcessor = pageProcessor;
        return this;
    }

    public Spider setDownloader(Downloader downloader) {
        checkIfRunning();
        this.downloader = downloader;
        return this;
    }

    public Spider setHttpProxyPool(HttpProxyPool httpProxyPool) {
        checkIfRunning();
        this.httpProxyPool = httpProxyPool;
        return this;
    }

    public Spider setCookieStorePool(CookieStorePool cookieStorePool) {
        checkIfRunning();
        this.cookieStorePool = cookieStorePool;
        return this;
    }

    public Spider setHttpClientPool(HttpClientPool httpClientPool) {
        checkIfRunning();
        this.httpClientPool = httpClientPool;
        return this;
    }

    public Spider addStartRequests(String... urls) {
        checkIfRunning();
        if (startRequests == null) {
            startRequests = new ArrayList<Request>(urls.length);
        }
        for (String url : urls) {
            startRequests.add(new Request(url));
        }
        return this;
    }

    public Spider addStartRequests(Request... requests) {
        checkIfRunning();
        if (startRequests == null) {
            startRequests = new ArrayList<Request>(requests.length);
        }
        for (Request request : requests) {
            startRequests.add(request);
        }
        return this;
    }

    public Spider addTargetRequests(String... urls) {
        checkScheduler();
        for (String url : urls) {
            scheduler.push(this, new Request(url));
        }
        return this;
    }

    public Spider addTargetRequests(Request... requests) {
        checkScheduler();
        for (Request request : requests) {
            scheduler.push(this, request);
        }
        return this;
    }

    public Spider setSpiderListeners(List<SpiderListener> spiderListeners) {
        this.spiderListeners = spiderListeners;
        return this;
    }

    public Spider addSpiderListeners(SpiderListener ... spiderListeners) {
        if (this.spiderListeners == null) {
            this.spiderListeners = new LinkedList<SpiderListener>();
        }
        for (SpiderListener spiderListener : spiderListeners) {
            this.spiderListeners.add(spiderListener);
        }
        return this;
    }

    public Spider setUUID(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public Spider setThreadCount(int threadCount) {
        this.threadCount = threadCount;
        return this;
    }

    private void checkScheduler() {
        if (scheduler == null) {
            throw new IllegalStateException("Scheduler is required.");
        }
    }

    public Date getStartTime() {
        return startTime;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public int getThreadAlive() {
        return threadPool == null ? 0 : threadPool.getThreadAlive();
    }

    public Status getStatus() {
        return Status.parse(stat.get());
    }

    public void runAsync() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.setName(getUUID());
        thread.start();
    }

    protected void checkIfRunning() {
        if (stat.get() == Status.Running.getValue()) {
            throw new IllegalStateException("Spider is already running!");
        }
    }

    protected void init() {
        if (stat.get() != Status.Init.getValue() && stat.get() != Status.Stopped.getValue()) {
            throw new IllegalStateException("Spider " + getUUID() + " is busy now!");
        }
        if (pageProcessor == null) {
            throw new IllegalArgumentException("PageProcessor is required!");
        }
        if (threadCount > 0) {
            spiderConfig.setThreadCount(threadCount);
        }
        if (StringUtils.isNotBlank(uuid)) {
            spiderConfig.setUUID(uuid);
        }

        stat.set(Status.Running.getValue());

        if (downloader == null) {
            if (httpClientPool == null) {
                httpClientPool = new DefaultHttpClientPool(new HttpClientFactory());
            }
            downloader = new HttpClientDownloader(httpClientPool, httpProxyPool, cookieStorePool);
        } else if (downloader instanceof HttpClientDownloader) {
            HttpClientDownloader httpClientDownloader = (HttpClientDownloader) downloader;
            if (httpClientDownloader.getHttpProxyPool() != null) {
                httpProxyPool = httpClientDownloader.getHttpProxyPool();
            } else if (httpProxyPool != null) {
                httpClientDownloader.setHttpProxyPool(httpProxyPool);
            }

            if (httpClientDownloader.getCookieStorePool() != null) {
                cookieStorePool = httpClientDownloader.getCookieStorePool();
            } else if (cookieStorePool != null) {
                httpClientDownloader.setCookieStorePool(cookieStorePool);
            }

            if (httpClientDownloader.getHttpClientPool() != null) {
                httpClientPool = httpClientDownloader.getHttpClientPool();
            }
        }

        if (pipeline == null) {
            pipeline = new ConsolePipeline();
        }
        if (scheduler == null) {
            scheduler = new QueueScheduler();
        }

        if (threadPool == null || threadPool.isShutdown()) {
            threadPool = new ThreadPool(spiderConfig.getThreadCount());
        }

        if (startRequests != null && !startRequests.isEmpty()) {
            for (Request request : startRequests) {
                scheduler.push(this, request);
            }
            startRequests.clear();
        }

        startTime = new Date();

        logger.info("Spider {} started at {}.", getUUID(),
                SpiderTimeUtils.formatTime(startTime, Config.DATE_TIME_FORMAT));
    }

    protected void working() {
        while (!Thread.currentThread().isInterrupted() && stat.get() == Status.Running.getValue()) {
            Request request = scheduler.poll(this);
            if (request != null) {
                threadPool.execute(new SpiderExecutor(request));
            } else {
                if (isCompletedToExit()) {
                    break;
                }
                waitNewRequest();
            }
        }
    }

    private class SpiderExecutor implements Runnable {
        private Request request;

        public SpiderExecutor(Request request) {
            this.request = request;
        }

        @Override
        public void run() {
            Page page = null;
            try {
                page = downloader.download(siteConfig, request);
                if (page == null || !page.isSuccess()) {
                    //download failed
                    onError(request, page);
                    return;
                }

                Result result = pageProcessor.process(request, page);
                if (result != null) {
                    //no result
                    pipeline.persist(request, result);
                }
                onSuccess(request, page, result);
            } catch (Exception e) {
                logger.error("Spider {} execute task exception {}", getUUID(), e);
                onError(request, page);
            } finally {
                try {
                    if (page != null) {
                        extractAndAddRequests(page);
                        SpiderIOUtils.closeQuietly(page);//input stream page
                    }
                    if (httpProxyPool != null) {
                        httpProxyPool.returnProxy(request, page != null ? page.getStatusCode() : 0);
                    }
                } finally {
                    signalNewRequest();
                }
            }
        }
    }

    private void waitNewRequest() {
        try {
            lock.lock();
            if (isCompletedToExit()) {
                return;
            }
            logger.warn("Spider {} is waiting for new request.", getUUID());
            condition.await(spiderConfig.getEmptySleepMillis(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("Spider {} waiting for new request interrupted, error {}", getUUID(), e);
        } finally {
            lock.unlock();
        }
    }

    private void signalNewRequest() {
        try {
            lock.lock();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    private boolean isCompletedToExit() {
        return spiderConfig.isExitWhenComplete() && getThreadAlive() <= 0;
    }

    protected void cleanup() {
        stat.set(Status.Stopping.getValue());
        if (spiderConfig.isDestroyWhenExit()) {
            closeAll();
        }
        stat.set(Status.Stopped.getValue());

        Date now = new Date();
        logger.info("Spider {} shutdown at {}.", getUUID(),
                SpiderTimeUtils.formatTime(now, Config.DATE_TIME_FORMAT));
    }

    private void closeAll() {
        closeDelay();
        SpiderIOUtils.closeQuietly(downloader);
        SpiderIOUtils.closeQuietly(scheduler);
        SpiderIOUtils.closeQuietly(pipeline);
        SpiderIOUtils.closeQuietly(pageProcessor);
        SpiderIOUtils.closeQuietly(threadPool);
    }

    private void closeDelay() {
        try {
            long closeDelayMillis = spiderConfig.getCloseDelayMillis();
            while (getThreadAlive() > 0 && closeDelayMillis > 0) {
                logger.info("Spider {} active work left {}, waiting for shutdown.", getUUID(), getThreadAlive());
                Thread.sleep(1000);
                closeDelayMillis -= 1000;
            }
        } catch (InterruptedException e) {
        }
    }

    protected void onError(Request request, Page page) {
        if (spiderListeners != null && !spiderListeners.isEmpty()) {
            for (SpiderListener spiderListener : spiderListeners) {
                try {
                    spiderListener.onError(request, page);
                } catch (Exception e) {
                    logger.error("Spider {} listener on error process exception {}", getUUID(), e);
                }
            }
        }
    }

    protected void onSuccess(Request request, Page page, Result result) {
        if (spiderListeners != null && !spiderListeners.isEmpty()) {
            for (SpiderListener spiderListener : spiderListeners) {
                try {
                    spiderListener.onSuccess(request, page, result);
                } catch (Exception e) {
                    logger.error("Spider {} listener on success process exception {}", getUUID(), e);
                }
            }
        }
    }

    protected void extractAndAddRequests(Page page) {
        List<Request> targetRequests = page.getTargetRequests();
        if (CollectionUtils.isNotEmpty(targetRequests)) {
            for (Request request : targetRequests) {
                addTargetRequests(request);
            }
        }
    }

    public enum Status {
        Init(0),
        Running(1),
        Stopping(2),
        Stopped(3);

        Status(int value) {
            this.value = value;
        }

        private int value;
        int getValue() {
            return value;
        }
        public static Status parse(int value) {
            for (Status status : Status.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            //default value
            return Init;
        }
    }

}

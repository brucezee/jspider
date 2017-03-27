package com.brucezee.jspider.selenium;

import com.brucezee.jspider.selenium.common.LandlordBlockingQueue;
import com.brucezee.jspider.selenium.common.enums.DriverType;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.SiteConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * WebDriver池
 * Created by brucezee on 2017/1/11.
 */
public class WebDriverPool {
    private static final int DEFAULT_CAPACITY = 5;
    private WebDriverFactory factory;
    private WebDriverChooser chooser;
    private int capacity = DEFAULT_CAPACITY;
    private ReentrantLock lock = new ReentrantLock();
    private Map<DriverType, LandlordBlockingQueue<WebDriverEx>> queueMap = new HashMap<DriverType, LandlordBlockingQueue<WebDriverEx>>(3);

    public WebDriverPool(WebDriverFactory factory) {
        this(factory, DEFAULT_CAPACITY);
    }

    public WebDriverPool(WebDriverFactory factory, int capacity) {
        this(factory, new DefaultWebDriverChooser(DriverType.CHROME), capacity);
    }

    public WebDriverPool(WebDriverFactory factory, WebDriverChooser chooser, int capacity) {
        this.factory = factory;
        this.chooser = chooser;
        this.capacity = capacity;
    }

    public WebDriverEx getWebDriver(SiteConfig siteConfig, DriverConfig driverConfig, Request request) throws IOException, InterruptedException {
        DriverType driverType = chooser.choose(request);

        LandlordBlockingQueue<WebDriverEx> queue = null;

        queue = queueMap.get(driverType);
        if (queue == null) {
            lock.lockInterruptibly();
            try {
                queue = queueMap.get(driverType);
                if (queue == null) {
                    queue = new LandlordBlockingQueue<WebDriverEx>(capacity);
                    queueMap.put(driverType, queue);
                }
            } finally {
                lock.unlock();
            }
        }

        WebDriverEx poll = queue.poll();
        if (poll != null) {
            return poll;
        }

        if (queue.isNeedMore()) {
            queue.add(factory.createWebDriver(siteConfig, driverConfig, driverType));
        }

        return queue.poll(siteConfig.getConnectionRequestTimeout(), TimeUnit.MILLISECONDS);
    }

    public void returnWebDriver(WebDriverEx webDriver, Request request) {
        DriverType driverType = chooser.choose(request);
        BlockingQueue<WebDriverEx> queue = queueMap.get(driverType);
        if (queue != null) {
            try {
                queue.put(webDriver);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdownWebDriver(WebDriverEx webDriver, Request request) {
        DriverType driverType = chooser.choose(request);
        BlockingQueue<WebDriverEx> queue = queueMap.get(driverType);
        if (queue != null) {
            webDriver.shutdown();

            if (queue instanceof LandlordBlockingQueue) {
                ((LandlordBlockingQueue) queue).resetOne();
            }
        }
    }

    public void shutdown() {
        synchronized (queueMap) {
            for (Map.Entry<DriverType, LandlordBlockingQueue<WebDriverEx>> entry : queueMap.entrySet()) {
                shutdown(entry.getValue());
            }
        }
    }

    private void shutdown(BlockingQueue<WebDriverEx> queue) {
        if (queue != null) {
            while (!queue.isEmpty()) {
                try {
                    queue.take().shutdown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isWebDriverExpired(WebDriverEx webDriver, long expireMillis) {
        return expireMillis > 0 && System.currentTimeMillis() - webDriver.getCreatedTime().getTime() >= expireMillis;
    }

    public void shutdownOrReturn(WebDriverEx webDriver, Request request, long expireMillis) {
        if (isWebDriverExpired(webDriver, expireMillis)) {
            shutdownWebDriver(webDriver, request);
        } else {
            returnWebDriver(webDriver, request);
        }
    }
}

package com.brucezee.jspider.monitor;

import com.brucezee.jspider.Spider;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

/**
 * 爬虫任务监控
 * Created by brucezee on 2017/1/11.
 */
public class SpiderMonitor {
    public static final String JSPIDER_JMX_SERVER_NAME = "JSpider"; //名称
    private static SpiderMonitor instance;  //爬虫任务监控实例

    public static SpiderMonitor getInstance() {
        if (instance == null) {
            synchronized (SpiderMonitor.class) {
                if (instance == null) {
                    instance = new SpiderMonitor();
                }
            }
        }
        return instance;
    }

    private MBeanServer mbeanServer;
    private String jmxServerName;

    public SpiderMonitor() {
        jmxServerName = JSPIDER_JMX_SERVER_NAME;
        mbeanServer = ManagementFactory.getPlatformMBeanServer();
    }

    public synchronized static SpiderMonitor register(Spider... spiders) {
        SpiderMonitor spiderMonitor = SpiderMonitor.getInstance();
        for (Spider spider : spiders) {
            try {
                MonitorSpiderListener monitorSpiderListener = new MonitorSpiderListener();
                spider.addSpiderListeners(monitorSpiderListener);

                SpiderStatusMXBean spiderStatus = new SpiderStatus(spider, monitorSpiderListener);
                ObjectName objectName = new ObjectName(spiderMonitor.getJmxServerName() + ":name=" + spiderStatus.getName());
                spiderMonitor.getMbeanServer().registerMBean(spiderStatus, objectName);
            } catch (Exception e) {
            }
        }
        return spiderMonitor;
    }

    public String getJmxServerName() {
        return jmxServerName;
    }

    public MBeanServer getMbeanServer() {
        return mbeanServer;
    }
}

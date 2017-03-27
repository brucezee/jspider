package com.brucezee.jspider.test;

import com.brucezee.jspider.*;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import com.brucezee.jspider.downloader.proxy.HttpProxy;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.scheduler.QueueScheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by brucezee on 2017/1/7.
 */
public class ProxySpiderTest {
    public static void main(String[] args) {
        Spider spider = Spider.create(new Ip138PageProcessor())
                .setUUID("ip138")
                .setThreadCount(6)
                .setPipeline(new Ip138Pipeline())
                .setHttpProxyPool(new Ip138ProxyPool())
                .setScheduler(new Ip138Scheduler())
                .addStartRequests("http://1212.ip138.com/ic.asp");
        spider.run();
    }

    public static class Ip138ProxyPool extends HttpProxyPool {
        HttpProxy[] proxies = new HttpProxy[] {
            new HttpProxy("177.11.162.78", 8080),
            new HttpProxy("124.248.177.17", 8080),
            new HttpProxy("119.235.50.182", 8080),
            new HttpProxy("119.40.106.135", 8081),
            new HttpProxy("222.188.88.10", 8998),
            new HttpProxy("186.10.5.139", 8080),
            new HttpProxy("182.155.68.138", 3128),
            new HttpProxy("1.179.185.253", 8080),
            new HttpProxy("179.243.8.40", 8080),
            new HttpProxy("186.226.253.10", 3128),
            new HttpProxy("202.154.23.82", 8080),
            new HttpProxy("103.28.225.180", 8080),
            new HttpProxy("43.243.143.218", 8080),
            new HttpProxy("139.59.29.51", 8000),
            new HttpProxy("68.171.68.0", 8081),
            new HttpProxy("108.170.3.142", 8080),
            new HttpProxy("181.115.241.90", 8080),
            new HttpProxy("207.219.132.245", 8080),
            new HttpProxy("124.32.141.184", 3128),
            new HttpProxy("158.108.42.194", 33446),
            new HttpProxy("112.102.123.161", 8998),
            new HttpProxy("110.72.45.165", 8123),
            new HttpProxy("60.250.81.118", 80),
            new HttpProxy("114.34.119.244", 3128),
            new HttpProxy("78.193.56.236", 8118),
            new HttpProxy("171.97.81.104", 8080),
            new HttpProxy("91.98.143.85", 8080),
            new HttpProxy("108.170.3.139", 8080),
        };

        private Map<String, HttpProxy> proxyMap = new HashMap<String, HttpProxy>();

        @Override
        public HttpProxy getProxy(Request request) {
            HttpProxy httpProxy = proxies[new Random().nextInt(proxies.length-1)];
            proxyMap.put(request.toString(), httpProxy);
            System.out.println("borrow http proxy "+httpProxy);
            return httpProxy;
        }
        @Override
        public void returnProxy(Request request, int statusCode) {
            HttpProxy httpProxy = proxyMap.get(request.toString());
            System.out.println("return http proxy "+httpProxy+" statusCode="+statusCode);
            proxyMap.remove(request.toString());
        }
    }

    public static class Ip138PageProcessor implements PageProcessor {
        @Override
        public Result process(Request request, Page page) {
            Result result = new Result();
            String ip = SpiderStrUtils.getMiddleText(page.getTextResult(), "<center>", "</center>");
            result.put("ip", ip);
            return result;
        }
    }

    private static int count = 0;
    public static class Ip138Pipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            System.out.println((++count)+"\t"+result.get("ip"));
        }
    }

    public static class Ip138Scheduler extends QueueScheduler {
        @Override
        public Request poll(Task task) {
            Request request = super.poll(task);
            if (request == null) {
                request = new Request("http://1212.ip138.com/ic.asp");
                super.push(task, request);

                request = super.poll(task);
            }
            return request;
        }
    }
}

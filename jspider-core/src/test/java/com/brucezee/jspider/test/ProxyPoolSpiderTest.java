package com.brucezee.jspider.test;

import com.brucezee.jspider.*;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import com.brucezee.jspider.downloader.proxy.HttpProxy;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;
import com.brucezee.jspider.downloader.proxy.HttpProxyPoolFactory;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.scheduler.QueueScheduler;

/**
 * Created by brucezee on 2017/1/7.
 */
public class ProxyPoolSpiderTest {
    public static void main(String[] args) {
        HttpProxyPool httpProxyPool = HttpProxyPoolFactory.createSequenceHttpProxyPool(
                new HttpProxy("177.11.162.78", 8080),
                new HttpProxy("124.248.177.17", 8080),
                new HttpProxy("119.235.50.182", 8080)
        );
        Spider spider = Spider.create(new Ip138PageProcessor())
                .setUUID("ip138")
                .setThreadCount(6)
                .setPipeline(new Ip138Pipeline())
                .setHttpProxyPool(httpProxyPool)
                .setScheduler(new Ip138Scheduler())
                .addStartRequests("http://1212.ip138.com/ic.asp");
        spider.run();
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

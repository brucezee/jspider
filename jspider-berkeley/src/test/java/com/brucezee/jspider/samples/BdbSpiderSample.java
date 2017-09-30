package com.brucezee.jspider.samples;

import com.brucezee.jspider.*;
import com.brucezee.jspider.berkeley.BdbPersistentScheduler;
import com.brucezee.jspider.monitor.SpiderMonitor;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.scheduler.Scheduler;
import org.jsoup.select.Elements;

/**
 * 基于BDB文件数据库的请求任务调度器示例
 * Created by brucezee on 2017/1/13.
 */
public class BdbSpiderSample {
    public static void main(String[] args) {
        SpiderConfig spiderConfig = SpiderConfig.create("baidu", 5)
                .setEmptySleepMillis(1000)
                .setExitWhenComplete(true);
        SiteConfig siteConfig = SiteConfig.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(100);

        //基于BDB文件数据库的请求任务调度器
        Scheduler scheduler = new BdbPersistentScheduler("F:\\spider.db", "db_spider");

        Spider spider = Spider.create(spiderConfig, siteConfig, new BaiduPageProcessor())
                .setPipeline(new BaiduPipeline())
                .setScheduler(scheduler)                //设置任务调度器
                .addStartRequests("https://www.baidu.com/s?wd=ip");

        //监控
        SpiderMonitor.register(spider);

        //启动
        spider.start();
    }

    public static class BaiduPageProcessor implements PageProcessor {
        @Override
        public Result process(Request request, Page page) {
            Result result = new Result();

            //解析HTML采用jsoup框架，详见：https://jsoup.org/

            //解析页面标题
            result.put("title", page.document().title());

            //获取页面上的新的链接地址
            Elements elements = page.document().select("a");        //获取所有a标签
            for (int i = 0; i < elements.size(); i++) {
                String url = elements.get(i).absUrl("href");    //获取绝对url
                if (url != null && url.contains("baidu")) {
                    page.addTargetRequest(url);     //获取新url添加到任务队列
                }
            }

            return result;
        }
    }

    public static class BaiduPipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            System.out.println(request + "\t" + result);
        }
    }
}

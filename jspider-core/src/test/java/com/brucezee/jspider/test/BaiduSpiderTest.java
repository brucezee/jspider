package com.brucezee.jspider.test;

import com.brucezee.jspider.*;
import com.brucezee.jspider.common.utils.SpiderUrlUtils;
import com.brucezee.jspider.monitor.SpiderMonitor;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by brucezee on 2017/1/7.
 */
public class BaiduSpiderTest {
    public static void main(String[] args) {
        SpiderConfig spiderConfig = SpiderConfig.create("baidu", 5)
                .setEmptySleepMillis(1000)
                .setExitWhenComplete(true);
        SiteConfig siteConfig = SiteConfig.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(100);

        Spider spider = Spider.create(spiderConfig, siteConfig, new BaiduPageProcessor())
                .setPipeline(new BaiduPipeline())
                .addStartRequests("https://www.baidu.com/s?wd=ip");

        SpiderMonitor.register(spider);
        spider.start();
//        spider.run();
        System.out.println();
        try {
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class BaiduPageProcessor implements PageProcessor {
        @Override
        public Result process(Request request, Page page) {
            Result result = new Result();
            Elements elements = page.document().select(".c-container a");
            if (elements != null && elements.size() > 0 && count == 0) {
                List<String> links = new ArrayList<String>(elements.size());
                for (Element element : elements) {
                    String href = element.absUrl("href");
                    if (StringUtils.isNotBlank(href) && StringUtils.isNotBlank(SpiderUrlUtils.getUrlHost(href))) {
                        links.add(href);
                    }
                }
                page.addTargetRequests(links);
            }

            result.put("title", page.document().title());
            return result;
        }
    }

    private static int count = 0;
    public static class BaiduPipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            System.out.println((++count)+"\t"+(String)result.get("title")+"\t"+request.getUrl());
            try {
                Thread.sleep(1600);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

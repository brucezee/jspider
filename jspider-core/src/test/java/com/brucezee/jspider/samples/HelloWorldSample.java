package com.brucezee.jspider.samples;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;
import com.brucezee.jspider.Spider;
import com.brucezee.jspider.processor.PageProcessor;
import org.jsoup.select.Elements;

/**
 * 最简单的爬虫示例
 * Created by brucezee on 2017/9/23.
 */
public class HelloWorldSample {

    public static void main(String[] args) {
        //create, config and start
        Spider.create()                                                 //创建爬虫实例
                .addStartRequests("https://www.baidu.com")           //添加起始url
                .setPageProcessor(new HelloWorldPageProcessor())        //设置页面解析器
                .start();                                               //开始抓取
    }

    public static class HelloWorldPageProcessor implements PageProcessor {
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
}

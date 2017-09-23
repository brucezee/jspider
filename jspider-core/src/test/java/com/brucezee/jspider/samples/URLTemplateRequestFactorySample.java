package com.brucezee.jspider.samples;

import com.brucezee.jspider.*;
import com.brucezee.jspider.paging.PagingRequestFactory;
import com.brucezee.jspider.paging.URLTemplateRequestFactory;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.scheduler.QueueScheduler;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过模板的方式返回多个请求任务
 * Created by brucezee on 2017/9/23.
 */
public class URLTemplateRequestFactorySample {
//     URL占位符规则
//
//     {A|B|C} 多个参数合集 表示A，B，C
//     http://www.baidu.com/{user|home}/?type={1|2|4}
//     {1->10} 连续数字合集 表示[1,10]
//     http://www.baidu.com/{user|home}/?type={1->5}
//     {|A|B} {A|B|} 包含空串的多个参数合集 表示A，B以及空字符串
//     http://www.baidu.com/{user|home|}?type={1|2|4}
//     http://www.baidu.com/{|user|home}?type={1|2|4}
//     {0|3->5|7->8|} 单个参数与连续数字组合合集
//     http://www.baidu.com/{|user|home}?type={{0|3->5|7->8}}

    public static void main(String[] args) {
        //根据模板生成多个请求任务
        String templateUrl = "http://www.nianshao.me/?stype={1->10}&page={1->5}";
        PagingRequestFactory pagingRequestFactory = new URLTemplateRequestFactory(templateUrl);

        Spider.create()
                .setUUID("nianshao")
                .setThreadCount(5)
                .setSiteConfig(SiteConfig.create().putCharset("nianshao", "gbk"))
                .setPipeline(new NianshaoPipeline())
                .setPageProcessor(new NianshaoPageProcessor())
                .setScheduler(new QueueScheduler(pagingRequestFactory))  //设置PagingRequestFactory
                .start();
    }

    public static class NianshaoPageProcessor implements PageProcessor {
        @Override
        public Result process(Request request, Page page) {
            Result result = new Result();

            Elements elements = page.document().select("div.mainPanel table.table tr");

            List<Map> list = new ArrayList<Map>();
            for (Element element : elements) {
                Elements tdElements = elements.select("td");
                if (tdElements.size() > 5) {
                    Map<String, String> info = new HashMap<String, String>();

                    info.put("ip", tdElements.get(0).text());
                    info.put("port", tdElements.get(1).text());
                    info.put("country", tdElements.get(2).text());
                    info.put("anonymous", tdElements.get(3).text());
                    info.put("type", tdElements.get(4).text());

                    list.add(info);
                }
            }

            result.put("list", list);
            return result;
        }
    }

    public static class NianshaoPipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            System.out.println(request.getUrl() + "\t" + result);
        }
    }
}

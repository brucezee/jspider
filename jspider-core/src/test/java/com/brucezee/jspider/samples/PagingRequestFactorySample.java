package com.brucezee.jspider.samples;

import com.brucezee.jspider.*;
import com.brucezee.jspider.paging.PagingRequestFactory;
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
 * 通过手动的方式返回多个请求任务
 * Created by brucezee on 2017/9/23.
 */
public class PagingRequestFactorySample {

    public static void main(String[] args) {
        Spider.create()
                .setUUID("nianshao")
                .setThreadCount(5)
                .setSiteConfig(SiteConfig.create().putCharset("nianshao", "gbk"))
                .setPipeline(new NianshaoPipeline())
                .setPageProcessor(new NianshaoPageProcessor())
                .setScheduler(new QueueScheduler(new NianshaoPagingRequestFactory()))  //设置PagingRequestFactory
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

    public static class NianshaoPagingRequestFactory implements PagingRequestFactory {
        int[] types = new int[] {1, 2, 3, 4, 5};

        @Override
        public List<Request> getRequests(Task task) {
            //构造多个请求任务
            List<Request> requests = new ArrayList<Request>();
            for (int type : types) {
                for (int pageNo = 1; pageNo <= 10; pageNo++) {
                    String url = "http://www.nianshao.me/?stype=" + type + "&page=" + pageNo;
                    requests.add(new Request(url));
                }
            }
            return requests;
        }
    }
}

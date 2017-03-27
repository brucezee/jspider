package com.brucezee.jspider.test;

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
 * Created by brucezee on 2017/1/17.
 */
public class PagingSpiderTest {
    public static void main(String[] args) {
        Spider spider = Spider.create(new NianshaoPageProcessor())
                .setUUID("nianshao")
                .setThreadCount(5)
                .setSiteConfig(SiteConfig.create().putCharset("nianshao", "gbk"))
                .setPipeline(new NianshaoPipeline())
                .setScheduler(new QueueScheduler(new NianshaoPagingRequestFactory()));
        spider.run();
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

    private static int count = 0;
    public static class NianshaoPipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            System.out.println((++count) + "\t" + request.getUrl() + "\t" + result);
        }
    }

    public static class NianshaoPagingRequestFactory implements PagingRequestFactory {
        int[] types = new int[] {1,2,3,4,5};

        @Override
        public List<Request> getRequests(Task task) {
            List<Request> requests = new ArrayList<Request>();
            for (int type : types) {
                for (int pageNo = 1; pageNo <= 10; pageNo++) {
                    String url = "http://www.nianshao.me/?stype="+type+"&page="+pageNo;
                    requests.add(new Request(url));
                }
            }
            System.out.println("take.........");
            return requests;
        }
    }
}

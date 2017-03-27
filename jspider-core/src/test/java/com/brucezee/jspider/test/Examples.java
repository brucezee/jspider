package com.brucezee.jspider.test;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;
import com.brucezee.jspider.Spider;
import com.brucezee.jspider.processor.PageProcessor;
import org.jsoup.select.Elements;

/**
 * Created by brucezee on 2017/3/27.
 */
public class Examples {
    public static void main(String[] args) {
        //page processor
        PageProcessor pageProcessor = new PageProcessor() {
            @Override
            public Result process(Request request, Page page) {
                Result result = new Result();

                //parse html
                result.put("title", page.document().title());

                //find new urls
                Elements elements = page.document().select("a");
                for (int i = 0; i < 10 && i < elements.size(); i++) {
                    String url = elements.get(i).absUrl("href");
                    if (url.contains("baidu")) {
                        page.addTargetRequest(url);
                    }
                }

                return result;
            }
        };

        //create, config and start
        Spider.create()
            .setPageProcessor(pageProcessor)
            .addStartRequests("http://www.baidu.com")
            .start();
    }
}

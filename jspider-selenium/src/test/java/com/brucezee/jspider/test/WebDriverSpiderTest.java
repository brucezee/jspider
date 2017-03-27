package com.brucezee.jspider.test;

import com.brucezee.jspider.*;
import com.brucezee.jspider.common.utils.SpiderUrlUtils;
import com.brucezee.jspider.monitor.SpiderMonitor;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.selenium.WebDriverChooser;
import com.brucezee.jspider.selenium.WebDriverDownloader;
import com.brucezee.jspider.selenium.WebDriverFactory;
import com.brucezee.jspider.selenium.WebDriverPool;
import com.brucezee.jspider.selenium.common.enums.DriverType;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

import java.util.*;

/**
 * Created by brucezee on 2017/1/7.
 */
public class WebDriverSpiderTest {
    public static void main(String[] args) {
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "F:\\selenium\\chromedriver.exe");
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "F:\\selenium\\phantomjs.exe");

        int threadCount = 5;
        SpiderConfig spiderConfig = SpiderConfig.create("baidu", threadCount)
                .setEmptySleepMillis(1000)
                .setExitWhenComplete(true);
        SiteConfig siteConfig = SiteConfig.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(100);

//        http://cul.sohu.com/20150625/n415609856.shtml
        Spider spider = Spider.create(spiderConfig, siteConfig, new BaiduPageProcessor())
                .setPipeline(new BaiduPipeline())
                .setDownloader(new WebDriverDownloader(
                                new WebDriverPool(
                                        new WebDriverFactory(),
//                                        new DefaultWebDriverChooser(DriverType.PHANTOMJS),
                                        new BaiduWebDriverChooser(),
                                        threadCount)
                        )
                )
                .addStartRequests("https://www.baidu.com/s?wd=https");
//                .addStartRequests("http://cul.sohu.com/20150625/n415609856.shtml");

        SpiderMonitor.register(spider);
        spider.start();
        System.out.println();
        try {
            Thread.sleep(1000000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class BaiduWebDriverChooser implements WebDriverChooser {
        private Map<String, DriverType> map = new HashMap<String, DriverType>();

        @Override
        public DriverType choose(Request request) {
            DriverType driverType = map.get(request.getUrl());
            if (driverType == null) {
                if (new Random().nextInt(3) == 1) {
                    map.put(request.getUrl(), DriverType.PHANTOMJS);
                } else {
                    map.put(request.getUrl(), DriverType.CHROME);
                }
                driverType = map.get(request.getUrl());
            }
            return driverType;
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
        }
    }
}

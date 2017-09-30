package com.brucezee.jspider.samples;

import com.brucezee.jspider.*;
import com.brucezee.jspider.downloader.Downloader;
import com.brucezee.jspider.monitor.SpiderMonitor;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.selenium.DefaultWebDriverChooser;
import com.brucezee.jspider.selenium.WebDriverDownloader;
import com.brucezee.jspider.selenium.WebDriverFactory;
import com.brucezee.jspider.selenium.WebDriverPool;
import com.brucezee.jspider.selenium.common.enums.DriverType;
import org.jsoup.select.Elements;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;

/**
 * 基于Selenium的爬虫
 * Created by brucezee on 2017/1/7.
 */
public class WebDriverSpiderSample {
    public static void main(String[] args) {
        //设置相应的驱动程序的位置
        System.setProperty(ChromeDriverService.CHROME_DRIVER_EXE_PROPERTY, "F:\\selenium\\chromedriver.exe");
        System.setProperty(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "F:\\selenium\\phantomjs.exe");

        //任务执行线程池大小
        int threadCount = 5;
        SpiderConfig spiderConfig = SpiderConfig.create("baidu", threadCount)
                .setEmptySleepMillis(1000)
                .setExitWhenComplete(true);
        SiteConfig siteConfig = SiteConfig.create()
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(100);

        //定义WebDriver池
        WebDriverPool webDriverPool = new WebDriverPool(
                new WebDriverFactory(),
                new DefaultWebDriverChooser(DriverType.CHROME),
                threadCount);

        //使用WebDriverDownloader请求任务下载器
        Downloader downloader = new WebDriverDownloader(webDriverPool);

        Spider spider = Spider.create(spiderConfig, siteConfig, new BaiduPageProcessor())
                .setDownloader(downloader)          //设置请求任务下载器
                .setPipeline(new BaiduPipeline())
                .addStartRequests("https://www.baidu.com/s?wd=https");

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

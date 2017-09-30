package com.brucezee.jspider.samples;

import com.brucezee.jspider.*;
import com.brucezee.jspider.monitor.SpiderMonitor;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.scheduler.QueueScheduler;

/**
 * 完整配置爬虫的示例
 * Created by brucezee on 2017/9/23.
 */
public class FullConfigSample {

    public static void main(String[] args) {
        //爬虫任务相关配置
        SpiderConfig spiderConfig = SpiderConfig.create("MySpider", 5)  //配置爬虫任务标识和线程数量
                .setExitWhenComplete(false) //任务完成后是否退出循环
                .setDestroyWhenExit(true)   //任务结束后是否销毁所有相关实体
                .setEmptySleepMillis(30000) //任务为空时休眠间隔
                .setCloseDelayMillis(60000); //任务结束后销毁相关实体最长等待时间

        //网络相关配置
        SiteConfig siteConfig = SiteConfig.create()
                .setConnectionRequestTimeout(15000) //连接请求超时毫秒数
                .setConnectTimeout(30000)           //连接超时毫秒数
                .setSocketTimeout(45000)            //读取超时毫秒数
                .setSoKeepAlive(true)               //soKeepAlive
                .setTcpNoDelay(true)                //tcpNoDelay
                .setBufferSize(8192)                //缓冲字节数组大小
                .setMaxIdleTimeMillis(1000 * 60 * 60)//最大空闲时间毫秒数
                .setMaxConnTotal(200)               //连接池最大连接数
                .setMaxConnPerRoute(100)            //每个路由最大连接数
                .setConnTimeToLiveMillis(-1)        //持久化连接最大存活时间毫秒数
                .setCookieSpec("SomeCookieSpec")   //Cookie策略名
                .setRedirectsEnabled(true)          //是否允许自动跳转
                .setRelativeRedirectsAllowed(true)  //是否允许相对路径跳转
                .setCircularRedirectsAllowed(false) //是否允许循环跳转
                .setMaxRedirects(3)                 //最大跳转数
                .addHeader("User-Agent", "Chrome/55.0.2883.87 Safari/537.36")   //添加公共请求Header
                .putCharset("aweb.com", "UTF-8")    //添加不同网站的页面字符集
                .putCharset("bweb.net", "GBK");

        //create, config and start
        Spider spider = Spider.create(spiderConfig, siteConfig, new HelloWorldSample.HelloWorldPageProcessor())
                .setUUID("MySpider")                //爬虫任务标识
                .setThreadCount(5)                   //线程数量
                .setPipeline(new CustomPipeline())  //设置结果集处理器
                .setScheduler(new QueueScheduler()) //设置请求任务调度器
                .setCookieStorePool(null)   //设置CookieStore池
                .setDownloader(null)        //设置下载器
                .setHttpClientPool(null)    //设置HttpClient池
                .setHttpProxyPool(null)     //设置代理池
                .setSpiderListeners(null)   //设置爬虫过程监听
                .addStartRequests("https://www.baidu.com");  //添加初始url

        //监控
        SpiderMonitor.register(spider);

        //启动
        spider.start();
    }

    public static class CustomPipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            //从result对象中获取解析结果数据
            String title = result.getAs("title");
            //持久化数据到数据库或文件
            System.out.println("title=" + title);
        }
    }
}

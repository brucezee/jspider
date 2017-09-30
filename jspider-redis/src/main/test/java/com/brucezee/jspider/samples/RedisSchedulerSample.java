package com.brucezee.jspider.samples;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;
import com.brucezee.jspider.Spider;
import com.brucezee.jspider.common.utils.PrefixSuffix;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import com.brucezee.jspider.monitor.SpiderMonitor;
import com.brucezee.jspider.pipeline.Pipeline;
import com.brucezee.jspider.processor.PageProcessor;
import com.brucezee.jspider.redis.RedisScheduler;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import redis.clients.jedis.JedisPool;


/**
 * 使用Redis作为请求任务调度器的示例
 * Created by brucezee on 2017/9/30.
 */
public class RedisSchedulerSample {
    public static void main(String[] args) {
        //定义redis的连接池
        JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(),
                "127.0.0.1", 6379, 30000);
//        JedisPool jedisPool = new JedisPool(new GenericObjectPoolConfig(),
//                "127.0.0.1", 6379, 30000, "123456"); //有密码

        //create, config and start
        Spider spider = Spider.create()                                                 //创建爬虫实例
                .setThreadCount(10)                                     //设置任务线程池数量
                .addStartRequests("http://blog.csdn.net/")           //添加起始url
                .setPageProcessor(new HelloWorldPageProcessor())        //设置页面解析器
                .setScheduler(new RedisScheduler(jedisPool))            //设置请求任务调度器
                .setPipeline(new HelloWorldPipeline());                  //结果集处理器

        //监控
        SpiderMonitor.register(spider);

        //启动
        spider.start();
    }

    public static class HelloWorldPipeline implements Pipeline {
        @Override
        public void persist(Request request, Result result) {
            if (!result.isEmpty()) {
                System.out.println(result);
            }
        }
    }

    public static class HelloWorldPageProcessor implements PageProcessor {
        @Override
        public Result process(Request request, Page page) {
            Result result = new Result();

            //解析HTML采用jsoup框架，详见：https://jsoup.org/

            //解析页面中用户信息部分
            Element usernameElement = page.document().select("div#blog_userface a.user_name").first();
            if (usernameElement != null) {
                //获取用户名等信息
                String name = usernameElement.text();
                String visitor = SpiderStrUtils.getMiddleText(page.getTextResult(),
                        new PrefixSuffix("访问：", "</li>"),
                        new PrefixSuffix("<span>", "</span>"));
                String score = SpiderStrUtils.getMiddleText(page.getTextResult(),
                        new PrefixSuffix("积分：", "</li>"),
                        new PrefixSuffix("<span>", "</span>"));
                String rank = SpiderStrUtils.getMiddleText(page.getTextResult(),
                        new PrefixSuffix("排名：", "</li>"),
                        new PrefixSuffix("<span>", "</span>"));

                result.put("name", name);//用户名
                result.put("visitor", visitor);//访问
                result.put("score", score);//积分
                result.put("rank", rank);//排名
            }

            //获取页面上的新的链接地址
            Elements elements = page.document().select("a");        //获取所有a标签
            for (int i = 0; i < elements.size(); i++) {
                String url = elements.get(i).absUrl("href");    //获取绝对url
                if (url != null && url.contains("blog.csdn.net")) {
                    page.addTargetRequest(url);     //获取新url添加到任务队列
                }
            }

            return result;
        }
    }
}

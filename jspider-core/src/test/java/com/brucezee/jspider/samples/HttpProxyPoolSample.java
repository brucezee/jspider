package com.brucezee.jspider.samples;

import com.brucezee.jspider.Spider;
import com.brucezee.jspider.downloader.proxy.HttpProxy;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;
import com.brucezee.jspider.downloader.proxy.HttpProxyPoolFactory;

/**
 * 爬虫使用代理池示例
 * Created by brucezee on 2017/9/23.
 */
public class HttpProxyPoolSample {

    public static void main(String[] args) {
        //只有固定几个ip的场景，创建代理ip池
        //HttpProxyPoolFactory.createSequenceHttpProxyPool 创建按顺序使用代理ip的池
        //HttpProxyPoolFactory.createRandomHttpProxyPool 创建随机使用代理ip的池
        HttpProxyPool httpProxyPool = HttpProxyPoolFactory.createSequenceHttpProxyPool(
                new HttpProxy("177.11.162.78", 8080),
                new HttpProxy("124.248.177.17", 9000),
                new HttpProxy("119.235.50.182", 8998)
        );
        Spider.create()                                                 //创建爬虫实例
                .addStartRequests("https://www.baidu.com")           //添加起始url
                .setPageProcessor(new HelloWorldSample.HelloWorldPageProcessor())        //设置页面解析器
                .setHttpProxyPool(httpProxyPool)                        //设置代理ip池
                .start();                                               //开始抓取
    }
}

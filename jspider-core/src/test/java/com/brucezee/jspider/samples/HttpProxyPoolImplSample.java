package com.brucezee.jspider.samples;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Spider;
import com.brucezee.jspider.downloader.proxy.HttpProxy;
import com.brucezee.jspider.downloader.proxy.HttpProxyPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 简单代理ip池实现示例
 * Created by brucezee on 2017/9/23.
 */
public class HttpProxyPoolImplSample {

    public static void main(String[] args) {
        Spider.create()                                                 //创建爬虫实例
                .addStartRequests("https://www.baidu.com")           //添加起始url
                .setPageProcessor(new HelloWorldSample.HelloWorldPageProcessor())        //设置页面解析器
                .setHttpProxyPool(new HttpProxyPoolImpl())              //设置代理ip池
                .start();                                               //开始抓取
    }

    public static class HttpProxyPoolImpl extends HttpProxyPool {
        //所有可用的代理ip资源
        HttpProxy[] proxies = new HttpProxy[] {
                new HttpProxy("222.188.88.10", 8998),
                new HttpProxy("186.10.5.139", 8080),
                new HttpProxy("182.155.68.138", 3128),
        };

        //当前占用的代理ip的缓存
        private Map<String, HttpProxy> proxyMap = new HashMap<>();

        @Override
        public HttpProxy getProxy(Request request) {
            HttpProxy httpProxy = proxies[new Random().nextInt(proxies.length - 1)];
            proxyMap.put(request.toString(), httpProxy);
            System.out.println("从代理ip池中获取代理ip："+httpProxy);
            return httpProxy;
        }
        
        @Override
        public void returnProxy(Request request, int statusCode) {
            HttpProxy httpProxy = proxyMap.get(request.toString());
            System.out.println("将代理ip：" + httpProxy + "还回代理ip池，请求状态码：" + statusCode);
            proxyMap.remove(request.toString());
        }
    }
}

# JSpider
A simple, scalable, and highly efficient web crawler framework for Java.

## Quick start

Add dependencies:

```xml
<dependency>
    <groupId>com.brucezee.jspider</groupId>
    <artifactId>jspider-core</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Hello world:

```java
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
```

Customize spider with config:

```java
public class Examples2 {
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

        Pipeline pipeline = new Pipeline() {
            @Override
            public void persist(Request request, Result result) {
                //persist or print result
                System.out.println("Request: " + request.getUrl() + " result: " + result);
            }
        };

        //spider config
        SpiderConfig spiderConfig = SpiderConfig.create("custom spider name", 5)
                .setUUID("custom spider name")
                .setThreadCount(5)
                .setExitWhenComplete(false)
                .setDestroyWhenExit(true)
                .setEmptySleepMillis(30000)
                .setCloseDelayMillis(60000)
                ;

        //web site config
        SiteConfig siteConfig = SiteConfig.create()
                .setConnectionRequestTimeout(15000)
                .setConnectTimeout(30000)
                .setSocketTimeout(45000)
                .setSoKeepAlive(true)
                .setTcpNoDelay(true)
                .setBufferSize(8192)
                .setMaxIdleTimeMillis(1000 * 60 * 60)
                .setMaxConnTotal(200)
                .setMaxConnPerRoute(100)
                .setConnTimeToLiveMillis(-1)
                .setCookieSpec("custom cookie spec")
                .setRedirectsEnabled(true)
                .setRelativeRedirectsAllowed(true)
                .setCircularRedirectsAllowed(false)
                .setMaxRedirects(3)

                .addHeader("User-Agent", "custom user agent")
                .addHeader("Accept", "custom accept")

                .putCharset("baidu.com", "UTF-8")
                .putCharset("alibaba.com", "GBK");

        //create, config and start
        Spider.create(spiderConfig, siteConfig, pageProcessor)
                .setUUID()
                .setThreadCount()
                .setPipeline(pipeline)
                .setScheduler()
                .setCookieStorePool()
                .setDownloader()
                .setHttpClientPool()
                .setHttpProxyPool()
                .setSpiderListeners()
                .addSpiderListeners()
                .addStartRequests("http://www.baidu.com")
                .start();
    }
}
```


### Thanks

* **webmagic**

	A scalable web crawler framework for Java.

	[https://github.com/code4craft/webmagic](https://github.com/code4craft/webmagic)

### Mail list

* [brucezee#163.com](brucezee#163.com)

* QQ group: 513469028
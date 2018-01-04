# JSpider
A simple, scalable, and highly efficient web crawler framework for Java.

一个简单高效扩展性良好的Java网络爬虫框架。

## Quick start 快速开始

添加Maven依赖：

```xml
<dependency>
    <groupId>com.brucezee.jspider</groupId>
    <artifactId>jspider-core</artifactId>
    <version>1.0</version>
</dependency>
```

使用Redis作为请求任务调度器（非必须）：

```xml
<dependency>
    <groupId>com.brucezee.jspider</groupId>
    <artifactId>jspider-redis</artifactId>
    <version>1.0</version>
</dependency>
```

使用Selenium控制浏览器请求数据（非必须）：
```xml
<dependency>
    <groupId>com.brucezee.jspider</groupId>
    <artifactId>jspider-selenium</artifactId>
    <version>1.0</version>
</dependency>
```

Hello world:

```java
public class HelloWorldSample {

    public static void main(String[] args) {
        //create, config and start
        Spider.create()                                                 //创建爬虫实例
                .addStartRequests("https://www.baidu.com")           //添加起始url
                .setPageProcessor(new HelloWorldPageProcessor())        //设置页面解析器
                .start();                                               //开始抓取
    }

    public static class HelloWorldPageProcessor implements PageProcessor {
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
}
```

Customize spider with config:

```java
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
        Spider.create(spiderConfig, siteConfig, new HelloWorldSample.HelloWorldPageProcessor())
                .setUUID("MySpider")                //爬虫任务标识
                .setThreadCount(5)                   //线程数量
                .setPipeline(new CustomPipeline())  //设置结果集处理器
                .setScheduler(new QueueScheduler()) //设置请求任务调度器
                .setCookieStorePool(null)   //设置CookieStore池
                .setDownloader(null)        //设置下载器
                .setHttpClientPool(null)    //设置HttpClient池
                .setHttpProxyPool(null)     //设置代理池
                .setSpiderListeners(null)   //设置爬虫过程监听
                .addStartRequests("https://www.baidu.com")  //添加初始url
                .start();
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
```

爬虫核心项目内代码示例

* 	[完整配置爬虫的示例](https://github.com/brucezee/jspider/blob/master/jspider-core/src/test/java/com/brucezee/jspider/samples/FullConfigSample.java)
* 	[最简单的爬虫示例](https://github.com/brucezee/jspider/blob/master/jspider-core/src/test/java/com/brucezee/jspider/samples/HelloWorldSample.java)
* 	[简单代理ip池实现示例](https://github.com/brucezee/jspider/blob/master/jspider-core/src/test/java/com/brucezee/jspider/samples/HttpProxyPoolImplSample.java)
* 	[爬虫使用代理池示例](https://github.com/brucezee/jspider/blob/master/jspider-core/src/test/java/com/brucezee/jspider/samples/HttpProxyPoolSample.java)
* 	[通过手动的方式返回多个请求任务](https://github.com/brucezee/jspider/blob/master/jspider-core/src/test/java/com/brucezee/jspider/samples/PagingRequestFactorySample.java)
* 	[通过模板的方式返回多个请求任务](https://github.com/brucezee/jspider/blob/master/jspider-core/src/test/java/com/brucezee/jspider/samples/URLTemplateRequestFactorySample.java)

爬虫Redis项目内代码示例
* 	[使用Redis作为请求任务调度器的示例](https://github.com/brucezee/jspider/blob/master/jspider-redis/src/main/test/java/com/brucezee/jspider/samples/RedisSchedulerSample.java)

基于文件数据库的代码示例
* 	[基于BDB文件数据库的请求任务调度器示例](https://github.com/brucezee/jspider/blob/master/jspider-berkeley/src/test/java/com/brucezee/jspider/samples/BdbSpiderSample.java)

## 项目jspider-parser

该项目实现了通过配置表达式解析文本，并将最终的解析结果隐射成为结果对象。

* 抓取到的文本数据data1, data2 ....
* 最终获取到的完整数据对象ResultObject，其中包含了所有需要的字段
* ResultObject不同字段来源于不同的文本数据，不同的字段来源于文本中不同的位置
* 每个字段需要各自的表达式获取，表达式可以是jsonpath，xpath，正则，jsoup选择器等
* FieldDefine定义了结果对象ResultObject的字段结构以及每个字段映射到文本数据的解析规则

jspider-parser-spring是jspider-parser与spring结合的项目。

页面解析框架使用示例
* 	[使用jsoup解析文本](https://github.com/brucezee/jspider/blob/master/jspider-parser/src/test/java/com/brucezee/jspider/samples/ParseHtmlWithJsoupSample.java)
* 	[使用正则表达式解析文本](https://github.com/brucezee/jspider/blob/master/jspider-parser/src/test/java/com/brucezee/jspider/samples/ParseHtmlWithRegexSample.java)
* 	[使用jsonpath解析json](https://github.com/brucezee/jspider/blob/master/jspider-parser/src/test/java/com/brucezee/jspider/samples/ParseJsonWithJsonPathSample.java)
* 	[使用xpath解析xml](https://github.com/brucezee/jspider/blob/master/jspider-parser/src/test/java/com/brucezee/jspider/samples/ParseXmlWithXPathSample.java)

## 项目jspider-selenium

该项目实现了使用selenium控制浏览器请求页面获取响应文本数据的功能，即实现了另外一个Downloader，使用之前需要先下载相应浏览器的驱动程序。

selenium的驱动程序下载地址：[http://selenium-release.storage.googleapis.com/index.html?path=2.53/](http://selenium-release.storage.googleapis.com/index.html?path=2.53/)

* 	[基于Selenium的爬虫](https://github.com/brucezee/jspider/blob/master/jspider-selenium/src/test/java/com/brucezee/jspider/samples/WebDriverSpiderSample.java)


## Spider components 爬虫核心组件

### Spider 爬虫任务实例

Spider主要负责协调各个爬虫核心组件工作，一个Spider实例就是一个爬虫工作单元。
    
SpiderConfig和SiteConfig共同完成爬虫任务的配置工作。

### Scheduler 请求任务调度器

Scheduler是一个用于实现请求任务的保存和获取的接口。

通过BlockingQueue实现的内存请求任务调度器：

* **QueueScheduler**
* **QueuePriorityScheduler**

通过Redis的list和set实现的分布式请求任务调度器:

* **RedisScheduler**
* **RedisPriorityScheduler**
* **ShardedRedisScheduler**
* **ShardedRedisPriorityScheduler**

### Downloader 请求任务下载器

Downloader是一个用于实现Http请求的接口。

默认通过HttpClient实现的请求任务下载器：

* **HttpClientDownloader**

通过WebDriver控制浏览器实现的请求任务下载器：

* **WebDriverDownloader**

### PageProcessor 页面解析器

PageProcessor是一个实现响应数据的解析并返回解析结果的接口。
响应数据可以是文本，字节数组或输入流。
这个接口需要使用者手动实现。

为了满足对不同请求进行不同的处理，增加了SubPageProcessor接口和组合PageProcessor的实现类CompositePageProcessor，通过
将多个SubPageProcessor添加到CompositePageProcessor中完成PageProcessor接口的组合功能。

* **SubPageProcessor**
* **CompositePageProcessor**

实现根据url判断处理的SubPageProcessor实现：

* **UrlMatchSubPageProcessor**


### Pipeline 结果集处理器

Pipeline是一个用于对解析结果进行持久化的接口。

后台输出或日志输出结果的实现类：

* **ConsolePipeline**
* **LogPipeline**

为了满足对不同结果进行不同的处理，增加SubPipeline接口和组合Pipeline的实现类CompositePipeline，通过
将多个SubPipeline添加的CompositePipeline中完成Pipeline接口的组合功能。

* **SubPipeline**
* **CompositePipeline**

实现根据url判断处理的SubPipeline实现：

* **UrlMatchSubPipeline**

### Request 请求任务

Request 是一个包含请求相关信息（url，参数，任务标识，响应类型等）的包装类.

### Page 请求响应内容

Page 是一个包含响应相关信息的包装类，响应的结果可以是纯文本、字节数组或输入流，这取决于请求任务配置的响应类型。


### Thanks

* **webmagic**

	A scalable web crawler framework for Java.

	[https://github.com/code4craft/webmagic](https://github.com/code4craft/webmagic)

### Mail list

* [brucezee#163.com](brucezee#163.com)

* QQ group: 513469028

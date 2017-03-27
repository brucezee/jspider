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

### Thanks

* **webmagic**

	A scalable web crawler framework for Java.

	[https://github.com/code4craft/webmagic](https://github.com/code4craft/webmagic)

### Mail list

* [brucezee#163.com](brucezee#163.com)

* QQ group: 513469028
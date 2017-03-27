package com.brucezee.jspider.downloader.httpclient.extended;

import org.apache.http.ProtocolException;
import org.apache.http.impl.client.LaxRedirectStrategy;

import java.net.URI;

/**
 * 默认连接跳转策略，对跳转地址中包含特殊字符进行处理
 * Created by brucezee on 2017/1/9.
 */
public class FormatLocationRedirectStrategy extends LaxRedirectStrategy {
    private UrlFormatter urlFormatter;
    public FormatLocationRedirectStrategy(UrlFormatter urlFormatter) {
        this.urlFormatter = urlFormatter;
    }
    public FormatLocationRedirectStrategy() {
        this.urlFormatter = new BasicUrlFormatter();
    }

    @Override
    protected URI createLocationURI(String location) throws ProtocolException {
        return super.createLocationURI(urlFormatter.format(location));
    }

    public static class BasicUrlFormatter implements UrlFormatter {
        @Override
        public String format(String url) {
            return url.replaceAll("\u0002|\u0001|\u0003| ", "+").replace("|", "%7C");
        }
    }
}

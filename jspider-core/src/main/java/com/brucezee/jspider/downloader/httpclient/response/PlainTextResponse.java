package com.brucezee.jspider.downloader.httpclient.response;

import com.brucezee.jspider.common.Config;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.ContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

/**
 * 纯文本类型响应
 * Created by brucezee on 2017/1/4.
 */
public class PlainTextResponse extends Response<String> {
    private static Logger logger = LoggerFactory.getLogger(PlainTextResponse.class);

    private String charset;

    public PlainTextResponse(String charset) {
        this.charset = charset;
    }

    @Override
    protected String handleHttpResponseResult(CloseableHttpResponse httpResponse) throws Throwable {
        if (StringUtils.isNotBlank(charset)) {
            return IOUtils.toString(httpResponse.getEntity().getContent(), charset);
        }

        Charset contentTypeCharset = getCharsetFromContentType(httpResponse.getEntity());
        if (contentTypeCharset != null) {
            return IOUtils.toString(httpResponse.getEntity().getContent(), contentTypeCharset.name());
        }

        byte[] bytes = IOUtils.toByteArray(httpResponse.getEntity().getContent());
        String content = new String(bytes, Config.DEFAULT_CHARSET);
        Charset contentCharset = getCharsetFromContent(content);
        if (contentCharset == null || contentCharset.name().equalsIgnoreCase(Config.DEFAULT_CHARSET)) {
            return content;
        }

        return new String(bytes, contentCharset);
    }

    private Charset getCharsetFromContent(String content) {
        content = SpiderStrUtils.getMiddleText(content, null, "</head>");

        //<meta http-equiv="content-type" content="text/html;charset=utf-8">
        String charset = SpiderStrUtils.getMiddleText(content, "charset=", "\"");
        if (StringUtils.isEmpty(charset)) {
            //<meta charset="gb2312" />
            charset = SpiderStrUtils.getMiddleText(content, "charset=\"", "\"");
        }

        if (StringUtils.isNotBlank(charset)) {
            try {
                return Charset.forName(charset);
            } catch (Exception e) {
            }
        }

        return null;
    }

    private Charset getCharsetFromContentType(HttpEntity httpEntity) {
        try {
            ContentType contentType = ContentType.get(httpEntity);
            if (contentType != null && contentType.getCharset() != null) {
                return contentType.getCharset();
            }
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedCharsetException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void closeHttpResponse(CloseableHttpResponse httpResponse) throws Throwable {
        httpResponse.close();
    }
}

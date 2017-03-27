package com.brucezee.jspider;

import com.brucezee.jspider.common.enums.ResponseType;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 请求响应内容
 * Created by brucezee on 2017/1/4.
 */
public class Page implements Serializable, Closeable {
    private String currentUrl;      //当前请求内容对应网址
    private int statusCode;         //请求响应的状态码
    private Header[] headers;       //请求响应的头
    private Object result;          //响应内容 根据Request的responseType来决定类型

    private List<Request> targetRequests = new ArrayList<Request>();    //新的目标请求
    private Document document;          //请求响应文本转换成jsoup的document对象

    public Page(String currentUrl, int statusCode, Header[] headers, Object result) {
        this.currentUrl = currentUrl;
        this.statusCode = statusCode;
        this.headers = headers;
        this.result = result;
    }

    public String getTextResult() {
        return (String) result;
    }

    public byte[] getBytesResult() {
        return (byte[]) result;
    }

    public InputStream getStreamResult() {
        return (InputStream) result;
    }

    public Document document() {
        if (document == null) {
            String result = getTextResult();
            if (StringUtils.isBlank(result)) {
                throw new IllegalStateException("Response text is empty!");
            }
            document = Jsoup.parse(result, currentUrl);
        }
        return document;
    }

    public List<Request> getTargetRequests() {
        return targetRequests;
    }

    public void addTargetRequests(List<String> requests) {
        addTargetRequests(requests, ResponseType.TEXT);
    }

    public void addTargetRequests(List<String> requests, ResponseType responseType) {
        synchronized (targetRequests) {
            for (String url : requests) {
                if (isValidUrl(url)) {
                    targetRequests.add(new Request(StringUtil.resolve(currentUrl, url), responseType));
                }
            }
        }
    }

    public void addTargetRequest(String url) {
        addTargetRequest(url, ResponseType.TEXT);
    }

    public void addTargetRequest(String url, ResponseType responseType) {
        if (isValidUrl(url)) {
            synchronized (targetRequests) {
                targetRequests.add(new Request(StringUtil.resolve(currentUrl, url), responseType));
            }
        }
    }

    public void addTargetRequest(Request request) {
        synchronized (targetRequests) {
            targetRequests.add(request);
        }
    }

    private boolean isValidUrl(String url) {
        return StringUtils.isNotEmpty(url)
                && !"#".equals(url.trim())
                && !url.trim().toLowerCase().startsWith("javascript:");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public Object getResult() {
        return result;
    }

    public boolean isSuccess() {
        return statusCode == HttpStatus.SC_OK && result != null;
    }

    @Override
    public void close() throws IOException {
        if (result != null && result instanceof InputStream) {
            ((InputStream) result).close();
        }
    }

    @Override
    public String toString() {
        return "Page{" +
                ", currentUrl=" + currentUrl +
                ", statusCode=" + statusCode +
                ", headers=" + Arrays.toString(headers) +
                ", result=" + result +
                ", targetRequests=" + targetRequests +
                '}';
    }
}

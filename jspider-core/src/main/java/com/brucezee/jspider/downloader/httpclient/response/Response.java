package com.brucezee.jspider.downloader.httpclient.response;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.util.Arrays;

/**
 * 响应
 * Created by brucezee on 2017/1/6.
 */
public abstract class Response<T> {
    private int statusCode;         //响应状态码
    private Header[] headers;       //响应头
    private T result;               //响应结果
    private Throwable exception;    //异常

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public T getResult() {
        return result;
    }

    public Throwable getException() {
        return exception;
    }

    public boolean isException() {
        return exception != null;
    }

    public boolean isSuccess() {
        return result != null;
    }

    protected void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    protected void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    protected void setResult(T result) {
        this.result = result;
    }

    protected void setException(Throwable exception) {
        this.exception = exception;
    }

    /**
     * 处理响应
     * @param httpResponse 响应
     * @param executeException 执行异常
     */
    public void handleHttpResponse(CloseableHttpResponse httpResponse, Throwable executeException) {
        if (httpResponse != null) {
            try {
                setStatusCode(httpResponse.getStatusLine().getStatusCode());
                setHeaders(httpResponse.getAllHeaders());
                setResult(handleHttpResponseResult(httpResponse));
            } catch (Throwable e) {
                setStatusCode(0);
                setException(e);
            }
        } else {
            setStatusCode(0);
            setException(executeException);
        }

        try {
            closeHttpResponse(httpResponse);
        } catch (Throwable throwable) {
        }
    }

    /**
     * 处理响应结果
     * @param httpResponse 响应
     * @return 处理结果
     * @throws Throwable 异常
     */
    protected abstract T handleHttpResponseResult(CloseableHttpResponse httpResponse) throws Throwable;

    /**
     * 关闭请求响应
     * @param httpResponse 响应
     * @throws Throwable 异常
     */
    protected abstract void closeHttpResponse(CloseableHttpResponse httpResponse) throws Throwable;

    @Override
    public String toString() {
        return "Response{" +
                ", statusCode=" + statusCode +
                ", headers=" + Arrays.toString(headers) +
                ", result=" + result +
                ", exception=" + exception +
                '}';
    }
}

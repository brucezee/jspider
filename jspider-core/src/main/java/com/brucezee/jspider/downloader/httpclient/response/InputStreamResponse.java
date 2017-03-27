package com.brucezee.jspider.downloader.httpclient.response;

import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.InputStream;

/**
 * 输入流类型响应
 * Created by brucezee on 2017/1/4.
 */
public class InputStreamResponse extends Response<InputStream> {
    @Override
    public InputStream handleHttpResponseResult(CloseableHttpResponse httpResponse) throws Throwable {
        return httpResponse.getEntity().getContent();
    }

    @Override
    protected void closeHttpResponse(CloseableHttpResponse httpResponse) throws Throwable {
        //do nothing
    }
}

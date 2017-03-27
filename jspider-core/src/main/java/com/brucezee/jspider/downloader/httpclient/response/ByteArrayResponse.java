package com.brucezee.jspider.downloader.httpclient.response;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * 字节数组类型响应
 * Created by brucezee on 2017/1/4.
 */
public class ByteArrayResponse extends Response<byte[]> {
    @Override
    protected byte[] handleHttpResponseResult(CloseableHttpResponse httpResponse) throws Throwable {
        return IOUtils.toByteArray(httpResponse.getEntity().getContent());
    }

    @Override
    protected void closeHttpResponse(CloseableHttpResponse httpResponse) throws Throwable {
        httpResponse.close();
    }
}

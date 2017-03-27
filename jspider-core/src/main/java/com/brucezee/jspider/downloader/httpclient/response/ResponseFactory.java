package com.brucezee.jspider.downloader.httpclient.response;

import com.brucezee.jspider.common.enums.ResponseType;

/**
 * 响应工厂
 * Created by brucezee on 2017/1/6.
 */
public class ResponseFactory {
    /**
     * 根据响应类型创建响应实例
     * @param responseType 响应类型
     * @param charset 字符集
     * @return 响应实例
     */
    public static Response createResponse(ResponseType responseType, String charset) {
        if (responseType == ResponseType.STREAM) {
            return new InputStreamResponse();
        }
        if (responseType == ResponseType.BYTES) {
            return new ByteArrayResponse();
        }
        return new PlainTextResponse(charset);
    }
}

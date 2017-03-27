package com.brucezee.jspider.serializer;

import com.alibaba.fastjson.JSON;
import com.brucezee.jspider.Request;

/**
 * 请求任务的json类型序列化器
 * Created by brucezee on 2017/1/7.
 */
public class RequestJsonSerializer implements Serializer<Request> {
    @Override
    public String serialize(Request object) {
        return JSON.toJSONString(object);
    }

    @Override
    public Request deserialize(String text) {
        return JSON.parseObject(text, Request.class);
    }
}

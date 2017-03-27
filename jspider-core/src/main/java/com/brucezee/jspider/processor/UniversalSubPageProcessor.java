package com.brucezee.jspider.processor;

import com.brucezee.jspider.Request;

/**
 * 通用子页面解析器（对所有请求都可以进行处理）
 * Created by brucezee on 2017/1/13.
 */
public abstract class UniversalSubPageProcessor implements SubPageProcessor {
    @Override
    public boolean isMatch(Request request) {
        return true;
    }
}

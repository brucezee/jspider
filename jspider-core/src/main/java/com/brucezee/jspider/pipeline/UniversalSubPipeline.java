package com.brucezee.jspider.pipeline;

import com.brucezee.jspider.Request;

/**
 * 通用子结果集处理器（对所有请求结果都可以进行处理）
 * Created by brucezee on 2017/1/16.
 */
public abstract class UniversalSubPipeline implements SubPipeline {
    @Override
    public boolean isMatch(Request request) {
        return true;
    }
}

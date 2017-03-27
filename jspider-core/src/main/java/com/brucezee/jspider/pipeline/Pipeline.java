package com.brucezee.jspider.pipeline;

import com.brucezee.jspider.Result;
import com.brucezee.jspider.Request;

/**
 * 结果集处理器
 * Created by brucezee on 2017/1/5.
 */
public interface Pipeline {
    /**
     * 对结果进行持久化
     * @param request 请求
     * @param result 结果集
     */
    public void persist(Request request, Result result);
}

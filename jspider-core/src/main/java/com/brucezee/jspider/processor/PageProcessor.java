package com.brucezee.jspider.processor;

import com.brucezee.jspider.Page;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;

/**
 * 内容处理器
 * Created by brucezee on 2017/1/4.
 */
public interface PageProcessor {
    /**
     * 处理某个请求返回的内容
     * @param request 请求
     * @param page 内容
     * @return 处理完成的结果集
     */
    public Result process(Request request, Page page);
}

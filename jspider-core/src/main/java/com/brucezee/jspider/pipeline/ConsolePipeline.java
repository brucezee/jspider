package com.brucezee.jspider.pipeline;


import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;

/**
 * 控制台类型结果处理器，将结果输出到控制台中
 * Created by brucezee on 2017/1/5.
 */
public class ConsolePipeline implements Pipeline {
    @Override
    public void persist(Request request, Result result) {
        System.out.println("Request: " + request.getUrl() + " result: " + result);
    }
}

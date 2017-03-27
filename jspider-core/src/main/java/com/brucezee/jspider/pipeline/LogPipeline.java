package com.brucezee.jspider.pipeline;


import com.brucezee.jspider.Request;
import com.brucezee.jspider.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志类型结果处理器，将结果输出到日志中
 * Created by brucezee on 2017/1/13.
 */
public class LogPipeline implements Pipeline {
    private Logger logger = LoggerFactory.getLogger(LogPipeline.class);

    @Override
    public void persist(Request request, Result result) {
        logger.debug("Request: {} result: {}", request.getUrl(), result);
    }
}

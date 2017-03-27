package com.brucezee.jspider.pipeline;

import com.brucezee.jspider.Request;

import java.util.regex.Pattern;

/**
 * 通过正则匹配请求url的子结果集处理器
 * Created by brucezee on 2017/1/16.
 */
public abstract class UrlMatchSubPipeline implements SubPipeline {
    private Pattern pattern;
    public UrlMatchSubPipeline(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isMatch(Request request) {
        return pattern.matcher(request.getUrl()).matches();
    }
}

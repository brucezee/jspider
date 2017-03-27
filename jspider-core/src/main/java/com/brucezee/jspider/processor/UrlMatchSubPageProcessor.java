package com.brucezee.jspider.processor;

import com.brucezee.jspider.Request;

import java.util.regex.Pattern;

/**
 * 通过正则匹配请求url的子内容处理器
 * Created by brucezee on 2017/1/13.
 */
public abstract class UrlMatchSubPageProcessor implements SubPageProcessor {
    private Pattern pattern;
    public UrlMatchSubPageProcessor(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isMatch(Request request) {
        return pattern.matcher(request.getUrl()).matches();
    }
}

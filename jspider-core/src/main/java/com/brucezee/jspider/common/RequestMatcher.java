package com.brucezee.jspider.common;

import com.brucezee.jspider.Request;

/**
 * 请求匹配器
 * Created by brucezee on 2017/1/13.
 */
public interface RequestMatcher {
    /**
     * 判断请求是否满足某种条件
     * @param request 请求
     * @return 满足条件返回true，否则返回false
     */
    public boolean isMatch(Request request);
}

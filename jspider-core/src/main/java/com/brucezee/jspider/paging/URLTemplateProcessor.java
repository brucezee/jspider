package com.brucezee.jspider.paging;

import java.util.List;

/**
 * 有占位符的url处理器
 * Created by brucezee on 2017/1/19.
 */
public interface URLTemplateProcessor {
    /**
     * 根据有占位符的url模板生成实际的多个url列表
     * @param templateUrl 具有占位符的url模板
     * @return 多个url列表
     */
    public List<String> process(String templateUrl);
}

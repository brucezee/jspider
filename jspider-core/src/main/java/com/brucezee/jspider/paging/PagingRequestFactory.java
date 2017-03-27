package com.brucezee.jspider.paging;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;

import java.util.List;

/**
 * 分页请求任务生成器
 * Created by brucezee on 2017/1/17.
 */
public interface PagingRequestFactory {
    /**
     * 生成全部分页请求
     * @param task 爬虫任务
     * @return 全部分页请求
     */
    public List<Request> getRequests(Task task);
}

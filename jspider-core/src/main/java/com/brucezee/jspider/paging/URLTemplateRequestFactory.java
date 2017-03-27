package com.brucezee.jspider.paging;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.common.enums.ResponseType;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import org.apache.http.HttpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 有占位符的url请求任务生成工厂
 * Created by brucezee on 2017/1/20.
 */
public class URLTemplateRequestFactory implements PagingRequestFactory {
    private String templateUrl;             //模板url
    private URLTemplateProcessor processor; //模板url的处理器

    private String method;                  //请求的方法
    private ResponseType responseType;      //响应类型
    private Map<String, String> headers;    //请求的头
    private HttpEntity entity;              //请求的实体
    private Map<String, String> parameters; //请求参数
    private Map<String, String> extras;     //额外自定义参数

    /**
     * 构造方法，使用默认的模板处理方式
     * @param templateUrl 模板url
     */
    public URLTemplateRequestFactory(String templateUrl) {
        this(templateUrl, new DefaultURLTemplateProcessor());
    }

    /**
     * 构造方法
     * @param templateUrl 模板url
     * @param processor 模板url的处理器
     */
    public URLTemplateRequestFactory(String templateUrl, URLTemplateProcessor processor) {
        this.templateUrl = templateUrl;
        this.processor = processor;
    }

    @Override
    public List<Request> getRequests(Task task) {
        List<String> urls = processor.process(templateUrl);
        if (urls != null) {
            List<Request> requests = new ArrayList<Request>(urls.size());
            for (String url : urls) {
                requests.add(new Request(SpiderStrUtils.getUUID(),
                        method, url, responseType, headers,
                        parameters, entity, extras));
            }
            return requests;
        }
        return null;
    }


    public URLTemplateRequestFactory addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(name, value);
        return this;
    }

    public URLTemplateRequestFactory setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public URLTemplateRequestFactory addParameter(String name, String value) {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        parameters.put(name, value);
        return this;
    }

    public URLTemplateRequestFactory setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public URLTemplateRequestFactory addExtra(String name, String value) {
        if (extras == null) {
            extras = new HashMap<String, String>();
        }
        extras.put(name, value);
        return this;
    }

    public URLTemplateRequestFactory setExtras(Map<String, String> extras) {
        this.extras = extras;
        return this;
    }

    public URLTemplateRequestFactory setMethod(String method) {
        this.method = method;
        return this;
    }

    public URLTemplateRequestFactory setResponseType(ResponseType responseType) {
        this.responseType = responseType;
        return this;
    }

    public URLTemplateRequestFactory setEntity(HttpEntity entity) {
        this.entity = entity;
        return this;
    }

    public URLTemplateRequestFactory setPriority(int priority) {
        return addExtra(Request.EXTRA_KEY_PRIORITY, String.valueOf(priority));
    }
}

package com.brucezee.jspider;

import com.brucezee.jspider.common.enums.ResponseType;
import com.brucezee.jspider.common.utils.SpiderNumberUtils;
import com.brucezee.jspider.common.utils.SpiderStrUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求任务
 * Created by brucezee on 2017/1/6.
 */
public class Request implements Serializable {
    public static final String METHOD_GET = "GET";      //请求方式GET
    public static final String METHOD_POST = "POST";    //请求方式POST

    private String id;                  //请求标志
    private String method;              //请求方式GET POST等
    private String url;                 //请求地址
    private ResponseType responseType;  //响应类型
    private Map<String, String> headers;    //请求头
    private HttpEntity entity;              //请求实体
    private Map<String, String> parameters; //请求参数
    private Map<String, String> extras;     //额外自定义参数

    public Request() {
    }

    public Request(String id,
                   String method,
                   String url,
                   ResponseType responseType,
                   Map<String, String> headers,
                   Map<String, String> parameters,
                   HttpEntity entity,
                   Map<String, String> extras) {
        this.id = StringUtils.isNotBlank(id) ? id : SpiderStrUtils.getUUID();
        this.method = StringUtils.isNotBlank(method) ? method : Request.METHOD_GET;
        this.url = url;
        this.responseType = responseType != null ? responseType : ResponseType.TEXT;
        this.headers = headers;
        this.parameters = parameters;
        this.entity = entity;
        this.extras = extras;
    }

    public Request(String url) {
        this(null, url);
    }

    public Request(String method, String url) {
        this(method, url, null);
    }

    public Request(String url, ResponseType responseType) {
        this(null, url, responseType);
    }

    public Request(String method, String url, ResponseType responseType) {
        this(null, method, url, responseType, null, null, null, null);
    }

    public String getId() {
        return id;
    }

    public Request setId(String id) {
        this.id = id;
        return this;
    }

    public synchronized Request addHeader(String name, String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(name, value);
        return this;
    }

    public synchronized Request addHeaders(Map<String, String> headers) {
        if (this.headers == null) {
            this.headers = headers;
        } else {
            this.headers.putAll(headers);
        }
        return this;
    }

    public synchronized Request setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public synchronized Request addParameter(String name, String value) {
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        }
        parameters.put(name, value);
        return this;
    }

    public synchronized Request addParameters(Map<String, String> parameters) {
        if (this.parameters == null) {
            this.parameters = parameters;
        } else {
            this.parameters.putAll(parameters);
        }
        return this;
    }

    public synchronized Request setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
        return this;
    }

    public synchronized Request addExtra(String name, String value) {
        if (extras == null) {
            extras = new HashMap<String, String>();
        }
        extras.put(name, value);
        return this;
    }

    public synchronized Request addExtras(Map<String, String> extras) {
        if (this.extras == null) {
            this.extras = extras;
        } else {
            this.extras.putAll(extras);
        }
        return this;
    }

    public Map<String, String> getExtras() {
        return extras;
    }
    
    public String getExtra(String name) {
        return extras != null ? extras.get(name) : null;
    }

    public Request setExtras(Map<String, String> extras) {
        this.extras = extras;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public Request setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        this.url = url;
        return this;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Request setResponseType(ResponseType responseType) {
        this.responseType = responseType;
        return this;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public HttpEntity entity() {
        return entity;
    }

    public Request setEntity(HttpEntity entity) {
        this.entity = entity;
        return this;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public String key() {
        return url;
    }


    public static final String EXTRA_KEY_PRIORITY = "$extras.priority";

    /**
     * 设置请求的优先级
     * @param priority 优先级
     * @return 当前对象
     */
    public Request setPriority(int priority) {
        return addExtra(EXTRA_KEY_PRIORITY, String.valueOf(priority));
    }

    /**
     * 获取请求的优先级
     * @return 优先级
     */
    public int getPriority() {
        String value = getExtra(EXTRA_KEY_PRIORITY);
        return SpiderNumberUtils.valueOf(SpiderNumberUtils.parseInt(value));
    }

    @Override
    public String toString() {
        return "Request{" +
                ", id=" + id +
                ", method=" + method +
                ", url=" + url +
                ", entity=" + entity +
                ", parameters=" + parameters +
                ", extras=" + extras +
                '}';
    }
}

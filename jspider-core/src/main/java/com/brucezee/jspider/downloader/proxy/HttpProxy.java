package com.brucezee.jspider.downloader.proxy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Http代理
 * Created by brucezee on 2017/1/9.
 */
public class HttpProxy implements Serializable {
    private String host;                //ip
    private int port;                   //端口
    private String username;            //用户名
    private String password;            //密码
    private Map<String, String> extras; //额外的自定义参数

    public HttpProxy(String host, int port) {
        this(host, port, null, null);
    }

    public HttpProxy(String host, int port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Map<String, String> getExtras() {
        return extras;
    }

    public String getExtra(String name) {
        return extras != null ? extras.get(name) : null;
    }

    public void setExtras(Map<String, String> extras) {
        this.extras = extras;
    }

    public void addExtra(String name, String value) {
        if (extras == null) {
            extras = new HashMap<String, String>();
        }
        extras.put(name, value);
    }

    @Override
    public String toString() {
        return "HttpProxy{" +
                ", host=" + host +
                ", port=" + port +
                ", username=" + username +
                ", password=" + password +
                ", extras=" + extras +
                '}';
    }
}

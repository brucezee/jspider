package com.brucezee.jspider.common.enums;

/**
 * 请求响应结果的类型
 * Created by brucezee on 2017/1/6.
 */
public enum ResponseType {
    /**
     * 文本
     */
    TEXT(0, "文本"),
    /**
     * 字节数组
     */
    BYTES(1, "字节数组"),
    /**
     * 输入流
     */
    STREAM(2, "输入流"),
    ;

    private int value;
    private String description;

    ResponseType(int value, String description) {
        this.value = value;
        this.description = description;
    }

    public int getValue() {
        return value;
    }
    public String getDescription() {
        return description;
    }
}

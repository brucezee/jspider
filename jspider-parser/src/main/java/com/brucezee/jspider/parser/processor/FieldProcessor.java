package com.brucezee.jspider.parser.processor;

/**
 * 字段处理器
 * Created by zhoubing on 2016/6/16.
 */
public interface FieldProcessor<T> {
    /**
     * 处理字段值
     * @param rootObject 完整内容对象
     * @param value 字段值
     * @return
     */
    public T process(Object rootObject, String value);
}

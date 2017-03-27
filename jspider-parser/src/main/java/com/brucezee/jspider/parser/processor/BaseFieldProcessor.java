package com.brucezee.jspider.parser.processor;

/**
 * 字段处理器基类
 * Created by zhoubing on 2016/6/16.
 */
public abstract class BaseFieldProcessor<T> implements FieldProcessor<T> {
    /**
     * 将已经格式化好的对象转换成最终结果对象
     * @param rootObject 根对象
     * @param value 当前值
     * @return 处理结果
     */
    protected abstract T parse(Object rootObject, String value);

    /**
     * 格式化对象，将对象转换成最终需要的字段
     * @param rootObject 根对象
     * @param value 当前值
     * @return 格式化后的字符串
     */
    protected abstract String format(Object rootObject, String value);

    @Override
    public T process(Object rootObject, String value) {
        if (value == null) {
            return null;
        }

        String formattedValue = format(rootObject, value);
        if (formattedValue == null) {
            return null;
        }

        return parse(rootObject, formattedValue);
    }
}

package com.brucezee.jspider.serializer;

/**
 * 对象序列化器
 * Created by brucezee on 2017/1/7.
 */
public interface Serializer<T> {
    /**
     * 将对象序列化成字符串
     * @param object 对象
     * @return 序列化后的字符串
     */
    public String serialize(T object);

    /**
     * 将字符串反序列化为对象
     * @param text 字符串
     * @return 反序列化后的对象
     */
    public T deserialize(String text);
}

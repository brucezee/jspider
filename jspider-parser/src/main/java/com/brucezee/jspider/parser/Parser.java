package com.brucezee.jspider.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.TypeUtils;
import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.processor.FieldProcessor;
import com.brucezee.jspider.parser.define.FieldTypeEnum;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 页面解析基类
 * Created by zhoubing on 2016/12/1.
 */
public abstract class Parser {
    /**
     * 根据字段解析规则解析页面，输出结果对象
     * @param data 待解析的页面
     * @param fieldDefine 字段解析定义
     * @param clazz 解析结果类
     * @param <T> 对象泛型
     * @return 解析结果对象
     */
    public <T> T parse(String data, FieldDefine fieldDefine, Class<T> clazz) {
        return parse(data, fieldDefine, clazz, new HashMap());
    }

    /**
     * 根据字段解析规则解析页面，输出结果对象
     * @param data 待解析的页面
     * @param fieldDefine 字段解析定义
     * @param clazz 解析结果类
     * @param result 解析结果过程存储的Map
     * @param <T> 对象泛型
     * @return 解析结果对象
     */
    public <T> T parse(String data, FieldDefine fieldDefine, Class<T> clazz, Map result) {
        parseData(data, fieldDefine, result);
        return JSON.parseObject(JSON.toJSONString(result), clazz);
    }

    /**
     * 根据多个字段解析规则解析多个页面，输出结果对象
     * @param wraps 多个解析页面和解析定义的组合
     * @param clazz 解析结果类
     * @param <T> 对象泛型
     * @return 解析结果对象
     */
    public <T> T parse(ParserWrap[] wraps, Class<T> clazz) {
        Map result = new HashMap();
        for (int i = 0; i < wraps.length; i++) {
            parseData(wraps[i].getData(), wraps[i].getDefine(), result);
        }
        return JSON.parseObject(JSON.toJSONString(result), clazz);
    }

    /**
     * 根据多个字段解析规则解析多个页面，输出结果对象
     * @param wraps 多个解析页面和解析定义的组合
     * @param clazz 解析结果类
     * @param <T> 对象泛型
     * @return 解析结果对象
     */
    public <T> T parse(List<ParserWrap> wraps, Class<T> clazz) {
        Map result = new HashMap();
        for (int i = 0; i < wraps.size(); i++) {
            parseData(wraps.get(i).getData(), wraps.get(i).getDefine(), result);
        }
        return JSON.parseObject(JSON.toJSONString(result), clazz);
    }

    /**
     * 根据字段解析规则解析页面，输出Map类型的对象
     * @param data 待解析的页面
     * @param fieldDefine 字段解析定义
     * @return 解析结果Map类型的对象
     */
    public Map parse(String data, FieldDefine fieldDefine) {
        Map result = new HashMap();
        parseData(data, fieldDefine, result);
        return result;
    }

    /**
     * 定义的字段类型转换成相应的Class
     * @param type 字段类型字符串
     * @return 相应的Class
     */
    protected Class<?> typeToClass(String type) {
        FieldTypeEnum typeEnum = FieldTypeEnum.parse(type);
        if (typeEnum != null) {
            return typeEnum.getClazz();
        }
        return null;
    }

    /**
     * 根据字段定义将结果值转换成相应类型的值
     * @param define 字段解析定义
     * @param rootObject 根对象
     * @param value 当前对象
     * @return 相应类型的对象
     */
    protected Object castValue(FieldDefine define, Object rootObject, Object value) {
        //特殊处理
        FieldProcessor fieldProcessor = define.getProcessor();
        if (fieldProcessor != null) {
            value = fieldProcessor.process(rootObject, value != null ? value.toString() : null);
        }
        return TypeUtils.cast(value, typeToClass(define.getType()), null);
    }

    /**
     * 获取字符串列表第一项作为最终的值，并进行相应的类型转换
     * @param list 列表
     * @param rootObject 根对象
     * @param define 字段解析定义
     * @return 相应类型的对象
     */
    protected Object getValue(List<String> list, Object rootObject, FieldDefine define) {
        return castValue(define, rootObject, list != null && list.size() > 0 ? list.get(0) : null);
    }

    /**
     * 根据解析规则解析数据，并将相应解析结果存储于Map中
     * @param data 待解析的页面
     * @param fieldDefine 字段解析定义
     * @param result 解析结果Map类型的对象
     */
    public abstract void parseData(String data, FieldDefine fieldDefine, Map result);
}

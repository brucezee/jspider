package com.brucezee.jspider.parser.define;

import com.brucezee.jspider.parser.processor.FieldProcessor;

import java.io.Serializable;

/**
 * 字段解析定义
 * Created by zhoubing on 2016/11/22.
 */
public class FieldDefine implements Serializable {
    private String id;//唯一标识
    private String type;//字段类型
    private String name;//字段名称
    private String selector;//字段解析表达式
    private FieldDefine[] defines;//字段对象包含的字段数组
    private FieldProcessor processor;//字段特殊处理类

    public FieldDefine() {
    }

    public FieldDefine(String type, String name, String selector, FieldDefine[] defines, FieldProcessor processor) {
        this.type = type;
        this.name = name;
        this.selector = selector;
        this.defines = defines;
        this.processor = processor;
    }

    public FieldDefine(String type, String name, String selector, FieldDefine[] defines) {
        this(type, name, selector, defines, null);
    }

    public FieldDefine(String type, String name, String selector, FieldDefine define) {
        this(type, name, selector, define != null ? new FieldDefine[]{define} : null);
    }

    public FieldDefine(String name, String selector, FieldDefine[] defines) {
        this(FieldTypeEnum.Object.toString(), name, selector, defines, null);
    }

    public FieldDefine(String name, String selector, FieldDefine define) {
        this(FieldTypeEnum.Object.toString(), name, selector, define != null ? new FieldDefine[]{define} : null);
    }

    public FieldDefine(FieldDefine define) {
        this(null, null, define);
    }

    public FieldDefine(FieldDefine[] defines) {
        this(null, null, defines);
    }

    public FieldDefine firstDefine() {
        return defines != null && defines.length > 0 ? defines[0] : null;
    }

    public FieldDefine lastDefine() {
        return defines != null && defines.length > 0 ? defines[defines.length-1] : null;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public FieldDefine[] getDefines() {
        return defines;
    }

    public void setDefines(FieldDefine[] defines) {
        this.defines = defines;
    }

    public FieldProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(FieldProcessor processor) {
        this.processor = processor;
    }

    public static FieldDefine newString(String name, String selector) {
        return newString(name, selector, null);
    }
    public static FieldDefine newString(String name, FieldProcessor fieldProcessor) {
        return newString(name, null, fieldProcessor);
    }
    public static FieldDefine newString(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.String.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newDouble(String name, String selector) {
        return newDouble(name, selector, null);
    }
    public static FieldDefine newDouble(String name, FieldProcessor fieldProcessor) {
        return newDouble(name, null, fieldProcessor);
    }
    public static FieldDefine newDouble(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Double.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newFloat(String name, String selector) {
        return newFloat(name, selector, null);
    }
    public static FieldDefine newFloat(String name, FieldProcessor fieldProcessor) {
        return newFloat(name, null, fieldProcessor);
    }
    public static FieldDefine newFloat(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Float.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newInteger(String name, String selector) {
        return newInteger(name, selector, null);
    }
    public static FieldDefine newInteger(String name, FieldProcessor fieldProcessor) {
        return newInteger(name, null, fieldProcessor);
    }
    public static FieldDefine newInteger(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Integer.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newShort(String name, String selector) {
        return newShort(name, selector, null);
    }
    public static FieldDefine newShort(String name, FieldProcessor fieldProcessor) {
        return newShort(name, null, fieldProcessor);
    }
    public static FieldDefine newShort(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Short.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newBoolean(String name, String selector) {
        return newBoolean(name, selector, null);
    }
    public static FieldDefine newBoolean(String name, FieldProcessor fieldProcessor) {
        return newBoolean(name, null, fieldProcessor);
    }
    public static FieldDefine newBoolean(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Boolean.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newByte(String name, String selector) {
        return newByte(name, selector, null);
    }
    public static FieldDefine newByte(String name, FieldProcessor fieldProcessor) {
        return newByte(name, null, fieldProcessor);
    }
    public static FieldDefine newByte(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Byte.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }
    
    public static FieldDefine newLong(String name, String selector) {
        return newLong(name, selector, null);
    }
    public static FieldDefine newLong(String name, FieldProcessor fieldProcessor) {
        return newLong(name, null, fieldProcessor);
    }
    public static FieldDefine newLong(String name, String selector, FieldProcessor fieldProcessor) {
        return new FieldDefine(FieldTypeEnum.Long.toString(), name, selector, (FieldDefine[])null, fieldProcessor);
    }

    public static FieldDefine newObject(String name, String selector, FieldDefine[] defines) {
        return new FieldDefine(FieldTypeEnum.Object.toString(), name, selector, defines);
    }
    public static FieldDefine newObject(String name, String selector) {
        return newObject(name, selector, null);
    }
    public static FieldDefine newObject(String name, FieldDefine[] defines) {
        return newObject(name, null, defines);
    }
    public static FieldDefine newObject(FieldDefine[] defines) {
        return newObject(null, null, defines);
    }
    public static FieldDefine newObject(FieldDefine define) {
        return newObject(null, null, new FieldDefine[]{define});
    }

    public static FieldDefine newArray(String name, String selector, FieldDefine define) {
        return new FieldDefine(FieldTypeEnum.Array.toString(), name, selector, define);
    }
    public static FieldDefine newArray(String name, String selector) {
        return newArray(name, selector, null);
    }
    public static FieldDefine newArray(FieldDefine define) {
        return newArray(null, null, define);
    }

    public static FieldDefine newList(String name, String selector, FieldDefine define) {
        return new FieldDefine(FieldTypeEnum.List.toString(), name, selector, define);
    }
    public static FieldDefine newList(String name, String selector) {
        return newList(name, selector, null);
    }
    public static FieldDefine newList(FieldDefine define) {
        return newList(null, null, define);
    }

    public static FieldDefine newMap(String name, String selector, FieldDefine[] defines) {
        return new FieldDefine(FieldTypeEnum.Map.toString(), name, selector, defines);
    }
    public static FieldDefine newMap(String name, String selector) {
        return newMap(name, selector, null);
    }
    public static FieldDefine newMap(String name, FieldDefine[] defines) {
        return newMap(name, null, defines);
    }
    public static FieldDefine newMap(FieldDefine[] defines) {
        return newMap(null, null, defines);
    }
    public static FieldDefine newMap(FieldDefine define) {
        return newMap(null, null, new FieldDefine[]{define});
    }
}

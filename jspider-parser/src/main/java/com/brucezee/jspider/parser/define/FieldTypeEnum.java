package com.brucezee.jspider.parser.define;

/**
 * 字段类型枚举
 * Created by zhoubing on 2016/11/28.
 */
public enum FieldTypeEnum {
    Object  ("Object",   java.lang.Object.class),
    Map     ("Map",      java.util.Map.class),
    Array   ("Array",    java.lang.reflect.Array.class),
    List    ("List",     java.util.List.class),

    String  ("String",   java.lang.String.class),
    Double  ("Double",   java.lang.Double.class),
    Float   ("Float",    java.lang.Float.class),
    Integer ("Integer",  java.lang.Integer.class),
    Long    ("Long",     java.lang.Long.class),
    Boolean ("Boolean",  java.lang.Boolean.class),
    Short   ("Short",    java.lang.Short.class),
    Byte    ("Byte",     java.lang.Byte.class),
    ;

    /**
     * 类名简写
     */
    private String name;
    /**
     * 对应的类
     */
    private Class<?> clazz;

    FieldTypeEnum(String name, Class<?> clazz) {
        this.name = name;
        this.clazz = clazz;
    }

    public boolean isEqual(String type) {
        return type != null && type.equals(name);
    }

    public static FieldTypeEnum parse(String value) {
        if (value != null) {
            FieldTypeEnum[] values = values();
            for (FieldTypeEnum each : values) {
                if (each.name.equals(value)) {
                    return each;
                }
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        return name;
    }
}

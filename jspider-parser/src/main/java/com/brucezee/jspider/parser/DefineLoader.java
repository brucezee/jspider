package com.brucezee.jspider.parser;

import com.alibaba.fastjson.JSON;
import com.brucezee.jspider.parser.processor.FieldProcessor;
import com.brucezee.jspider.parser.define.FieldDefine;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.*;

/**
 * 页面解析定义加载器
 * Created by zhoubing on 2016/11/24.
 */
public class DefineLoader {
    public static void main(String[] args) {
        FieldDefine fieldDefine = DefineLoader.getFieldDefine("");
        String xml = "";
        fieldDefine = DefineLoader.parseFieldDefine(xml);

        System.out.println(JSON.toJSONString(fieldDefine));
    }

    /**
     * 从文件中解析xml并获取第一个字段解析定义
     * @param path 文件路径
     * @return
     */
    public static FieldDefine getFieldDefine(String path) {
        return getFieldDefine(path, null);
    }

    /**
     * 从文件中解析xml并获取第一个字段解析定义
     * @param file 文件
     * @return
     */
    public static FieldDefine getFieldDefine(File file) {
        return getFieldDefine(file, null);
    }

    /**
     * 从流中解析xml并获取第一个字段解析定义
     * @param is 数据流
     * @return
     */
    public static FieldDefine getFieldDefine(InputStream is) {
        return getFieldDefine(is, null);
    }

    /**
     * 从Reader中解析xml并获取第一个字段解析定义
     * @param reader Reader
     * @return
     */
    public static FieldDefine getFieldDefine(Reader reader) {
        return getFieldDefine(reader, null);
    }

    /**
     * 从文件中解析xml并获取指定id的字段解析定义
     * @param path 文件路径
     * @param id 字段解析定义的id
     * @return
     */
    public static FieldDefine getFieldDefine(String path, String id) {
        return getFieldDefine(new File(path), id);
    }

    /**
     * 从文件中解析xml并获取指定id的字段解析定义
     * @param file 文件
     * @param id 字段解析定义的id
     * @return
     */
    public static FieldDefine getFieldDefine(File file, String id) {
        return getFieldDefine(file, null, null, id);
    }

    /**
     * 从流中解析xml并获取指定id的字段解析定义
     * @param is 数据流
     * @param id 字段解析定义的id
     * @return
     */
    public static FieldDefine getFieldDefine(InputStream is, String id) {
        return getFieldDefine(null, is, null, id);
    }

    /**
     * 从Reader中解析xml并获取指定id的字段解析定义
     * @param reader Reader
     * @param id 字段解析定义的id
     * @return
     */
    public static FieldDefine getFieldDefine(Reader reader, String id) {
        return getFieldDefine(null, null, reader, id);
    }

    private static FieldDefine getFieldDefine(File file, InputStream inputStream, Reader reader, String id) {
        Map<String, FieldDefine> defineMap = getFieldDefines(file, inputStream, reader);
        if (StringUtils.isNotBlank(id)) {
            return defineMap.get(id);
        }

        //如果不指定id，则返回第一个
        if (defineMap.size() > 0) {
            String key = DEFAULT_ID_PREFIX + "0";
            if (defineMap.containsKey(key)) {
                return defineMap.get(key);
            }
            return defineMap.entrySet().iterator().next().getValue();
        }
        return null;
    }

    /**
     * 从文件中解析xml返回多个以id为key的字段解析定义映射
     * @param path 文件路径
     * @return
     */
    public static Map<String, FieldDefine> getFieldDefines(String path) {
        return getFieldDefines(new File(path));
    }

    /**
     * 从文件中解析xml返回多个以id为key的字段解析定义映射
     * @param file 文件
     * @return
     */
    public static Map<String, FieldDefine> getFieldDefines(File file) {
        return getFieldDefines(file, null, null);
    }

    /**
     * 从流中解析xml返回多个以id为key的字段解析定义映射
     * @param is 流
     * @return
     */
    public static Map<String, FieldDefine> getFieldDefines(InputStream is) {
        return getFieldDefines(null, is, null);
    }

    /**
     * 从Reader中解析xml返回多个以id为key的字段解析定义映射
     * @param reader Reader
     * @return
     */
    public static Map<String, FieldDefine> getFieldDefines(Reader reader) {
        return getFieldDefines(null, null, reader);
    }

    private static Map<String, FieldDefine> getFieldDefines(File file, InputStream inputStream, Reader reader) {
        SAXReader saxReader = new SAXReader();
        Document document = null;
        try {
            if (file != null) {
                document = saxReader.read(file);
            } else if (inputStream != null) {
                document = saxReader.read(inputStream);
            } else if (reader != null) {
                document = saxReader.read(reader);
            } else {
                throw new IllegalArgumentException("all arguments is null");
            }
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }
        return parseRootElements(document.getRootElement());
    }

    /**
     * 解析xml文本并获取第一个字段解析定义
     * @param xml xml定义
     * @return
     */
    public static FieldDefine parseFieldDefine(String xml) {
        return parseFieldDefine(xml, null);
    }

    /**
     * 解析xml文本并获取指定id的字段解析定义
     * @param xml xml定义
     * @param id 字段解析定义的id
     * @return
     */
    public static FieldDefine parseFieldDefine(String xml, String id) {
        Map<String, FieldDefine> defineMap = parseFieldDefines(xml);
        if (StringUtils.isNotBlank(id)) {
            return defineMap.get(id);
        }

        //如果不指定id，则返回第一个
        if (defineMap.size() > 0) {
            String key = DEFAULT_ID_PREFIX + "0";
            if (defineMap.containsKey(key)) {
                return defineMap.get(key);
            }
            return defineMap.entrySet().iterator().next().getValue();
        }
        return null;
    }

    public static Map<String, FieldDefine> parseFieldDefines(String xml) {
        Document document = null;
        try {
            document = DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            throw new IllegalArgumentException(e);
        }
        return parseRootElements(document.getRootElement());
    }

    private static Map<String, FieldDefine> parseRootElements(Element rootElement) {
        Map<String, FieldDefine> fieldDefineMap = new HashMap<String, FieldDefine>();
        if ("defines".equals(rootElement.getName())) {
            Iterator<Element> rootDefineIterator = rootElement.elementIterator();
            int count = 0;
            while (rootDefineIterator.hasNext()) {
                count++;

                Element defineElement = rootDefineIterator.next();
                String id = getAttribute(defineElement, "id");
                if (StringUtils.isBlank(id)) {
                    id = DEFAULT_ID_PREFIX + count;
                }

                if (fieldDefineMap.containsKey(id)) {
                    throw new IllegalArgumentException("duplicated field define id[" + id + "].");
                }
                fieldDefineMap.put(id, parseFieldDefine(defineElement));
            }
        } else if ("define".equals(rootElement.getName())) {
            String id = getAttribute(rootElement, "id");
            if (StringUtils.isBlank(id)) {
                id = DEFAULT_ID_PREFIX + "0";
            }

            fieldDefineMap.put(id, parseFieldDefine(rootElement));
        } else {
            throw new IllegalArgumentException("xml parse error.");
        }

        return fieldDefineMap;
    }

    private static final String DEFAULT_ID_PREFIX = "field-define-";

    private static FieldDefine parseFieldDefine(Element defineElement) {
        String type = getAttribute(defineElement, "type");
        String name = getAttribute(defineElement, "name");
        String selector = getAttribute(defineElement, "selector");

        FieldDefine fieldDefine = new FieldDefine();
        fieldDefine.setType(type);
        fieldDefine.setName(name);
        fieldDefine.setSelector(selector);

        parseFieldDefines(fieldDefine, defineElement);

        return fieldDefine;
    }

    private static void parseFieldDefines(FieldDefine fieldDefine, Element defineElement) {
        Iterator<Element> defineIterator = defineElement.elementIterator();
        if (defineIterator.hasNext()) {
            List<FieldDefine> list = new LinkedList<FieldDefine>();
            while (defineIterator.hasNext()) {
                Element element = defineIterator.next();
                if ("selector".equals(element.getName())) {
                    fieldDefine.setSelector(element.getText());
                } else if ("processor".equals(element.getName())) {
                    fieldDefine.setProcessor(getFieldProcessor(element));
                } else {
                    list.add(parseFieldDefine(element));
                }
            }

            if (list.size() > 0) {
                fieldDefine.setDefines(list.toArray(new FieldDefine[list.size()]));
            }
        }
    }

    private static FieldProcessor getFieldProcessor(Element element) {
        Object[] arguments = getFieldProcessorArguments(element);
        String clazz = getAttribute(element, "class");

        try {
            if (arguments == null || arguments.length == 0) {
                return (FieldProcessor) Class.forName(clazz).newInstance();
            }

            Constructor<?>[] constructors = Class.forName(clazz).getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (arguments.length == constructor.getParameterCount()) {
                    return (FieldProcessor) constructor.newInstance(arguments);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private static Object[] getFieldProcessorArguments(Element element) {
        Iterator<Element> iterator = element.elementIterator();
        List<Object> list = new LinkedList<Object>();
        if (iterator.hasNext()) {
            while (iterator.hasNext()) {
                list.add(iterator.next().getText());
            }
        }
        return list.toArray(new Object[list.size()]);
    }

    private static String getAttribute(Element element, String name) {
        Attribute attribute = element.attribute(name);
        return attribute != null ? attribute.getValue() : null;
    }
}

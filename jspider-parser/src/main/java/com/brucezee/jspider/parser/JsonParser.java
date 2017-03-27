package com.brucezee.jspider.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.util.TypeUtils;
import com.brucezee.jspider.parser.processor.FieldProcessor;
import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.define.FieldTypeEnum;
import com.brucezee.jspider.parser.expression.RegexExpression;
import com.brucezee.jspider.parser.expression.SegmentExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于jsonpath解析json类型数据的解析器
 * Created by zhoubing on 2016/11/21.
 */
public class JsonParser extends Parser {
    public void parseData(String data, FieldDefine fieldDefine, Map result) {
        parseJson(JSON.parseObject(data), fieldDefine, result);
    }

    public void parseJson(JSONObject jsonObject, FieldDefine fieldDefine, Map result) {
        parseJson(jsonObject, jsonObject, fieldDefine, result);
    }

    private void parseJson(JSONObject rootJsonObject, JSONObject jsonObject, FieldDefine fieldDefine, Map result) {
        FieldDefine[] defines = fieldDefine.getDefines();
        if (defines == null || defines.length == 0) {
            return;
        }

        for (FieldDefine define : defines) {
            String type = define.getType();
            String selector = define.getSelector();

            if (StringUtils.isBlank(selector)) {
                if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                    Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                    parseJson(rootJsonObject, rootJsonObject, define, child);
                    result.put(define.getName(), child);
                    continue;
                }
                if (define.getProcessor() != null) {
                    Object value = define.getProcessor().process(rootJsonObject, rootJsonObject.toString());
                    result.put(define.getName(), TypeUtils.cast(value, typeToClass(define.getType()), null));
                    continue;
                }

                throw new IllegalArgumentException("unhandled json parser define type ["+type+"] when selector is empty.");
            }

            if (RegexExpression.isRegexExpression(selector)) {
                List<String> list = RegexExpression.matcher(rootJsonObject.toJSONString(), jsonObject.toJSONString(), selector);
                result.put(define.getName(), getValue(list, rootJsonObject, define));
                continue;
            }
            if (SegmentExpression.isSegmentExpression(selector)) {
                String value = SegmentExpression.matcher(rootJsonObject.toJSONString(), jsonObject.toJSONString(), selector);
                result.put(define.getName(), castValue(define, rootJsonObject, value));
                continue;
            }

            Object value = null;
            if (isRelativeSelector(selector)) {
                value = getJsonValue(rootJsonObject, jsonObject, selector, define);
            } else if (isAbsoluteSelector(selector)) {
                value = getJsonValue(rootJsonObject, rootJsonObject, selector, define);
            } else {
                throw new IllegalArgumentException("unsupported json selector ["+selector+"].");
            }

            if (value == null) {
                continue;
            }

            if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                if (value instanceof JSONObject) {
                    Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                    parseJson(rootJsonObject, (JSONObject) value, define, child);
                    result.put(define.getName(), child);
                    continue;
                }

                throw new IllegalArgumentException("the parser define type is Object or Map, but the result is NOT a JSONObject. It is "+value.getClass().getSimpleName()+".");
            }

            if (FieldTypeEnum.Array.isEqual(type) || FieldTypeEnum.List.isEqual(type)) {
                if (value instanceof JSONArray) {
                    JSONArray jsonArray = (JSONArray) value;
                    int size = jsonArray.size();
                    Object[] array = result.containsKey(define.getName()) ? (Object[]) result.get(define.getName()) : new Object[size];
                    int min = Math.min(size, array.length);
                    for (int i = 0; i < min; i++) {
                        Map item = array[i] != null ? (Map) array[i] : new HashMap();
                        parseJson(rootJsonObject, jsonArray.getJSONObject(i), define.firstDefine(), item);
                        array[i] = item;
                    }

                    result.put(define.getName(), array);
                    continue;
                }

                throw new IllegalArgumentException("the parser define type is Array or List, but the result is NOT a JSONArray. It is "+value.getClass().getSimpleName()+".");
            }

            result.put(define.getName(), value);
        }
    }

    private boolean isAbsoluteSelector(String selector) {
        return selector != null && !isRelativeSelector(selector) && selector.startsWith("$.");
    }
    private boolean isRelativeSelector(String selector) {
        return selector != null && selector.startsWith("$..");
    }
    private boolean isJsonPathSelector(String selector) {
        return selector != null && selector.startsWith("$");
    }

    private Object getJsonValue(JSONObject rootJsonObject, JSONObject jsonObject, String selector, FieldDefine define) {
        Object result = null;
        if (!isJsonPathSelector(selector)) {
            result = jsonObject.get(selector);
        } else {
            result = JSONPath.compile(selector).eval(jsonObject);
        }

        FieldProcessor fieldProcessor = define.getProcessor();
        if (fieldProcessor != null) {
            if (result instanceof List) {
                List list = (List) result;
                result = list.size() > 0 ? list.get(0) : null;
            }
            return fieldProcessor.process(rootJsonObject, result != null ? result.toString() : null);
        }

        if (result instanceof JSONObject || result instanceof JSONArray) {
            return result;
        }
        if (result instanceof List) {
            List list = (List) result;
            return list.size() > 0 ? TypeUtils.cast(list.get(0), typeToClass(define.getType()), null) : null;
        }
        return TypeUtils.cast(result, typeToClass(define.getType()), null);
    }
}

package com.brucezee.jspider.parser;

import com.alibaba.fastjson.util.TypeUtils;
import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.define.FieldTypeEnum;
import com.brucezee.jspider.parser.expression.JsoupExpression;
import com.brucezee.jspider.parser.expression.RegexExpression;
import com.brucezee.jspider.parser.expression.SegmentExpression;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Jsoup或正则表达式解析html类型数据的解析器
 * Created by zhoubing on 2016/11/29.
 */
public class JsoupParser extends Parser {
    public void parseData(String html, FieldDefine fieldDefine, Map result) {
        parseHtml(Jsoup.parse(html), fieldDefine, result);
    }

    public void parseHtml(Document document, FieldDefine fieldDefine, Map result) {
        parseHtml(document, document, fieldDefine, result);
    }

    private void parseHtml(Document document, Element element, FieldDefine fieldDefine, Map result) {
        FieldDefine[] defines = fieldDefine.getDefines();
        if (defines == null || defines.length == 0) {
            return;
        }

        JsoupExpression jsoupExpression = new JsoupExpression();//复用对象
        for (FieldDefine define : defines) {
            String type = define.getType();
            String selector = define.getSelector();

            if (StringUtils.isBlank(selector)) {
                if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                    Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                    parseHtml(document, document, define, child);
                    result.put(define.getName(), child);
                    continue;
                }
                if (define.getProcessor() != null) {
                    Object value = define.getProcessor().process(document, document.html());
                    result.put(define.getName(), TypeUtils.cast(value, typeToClass(define.getType()), null));
                    continue;
                }

                throw new IllegalArgumentException("unhandled html parser define type ["+type+"] when selector is empty.");
            }

            if (RegexExpression.isRegexExpression(selector)) {
                List<String> list = RegexExpression.matcher(document.html(), element.html(), selector);
                result.put(define.getName(), getValue(list, document, define));
                continue;
            }
            if (SegmentExpression.isSegmentExpression(selector)) {
                String value = SegmentExpression.matcher(document.html(), element.html(), selector);
                result.put(define.getName(), castValue(define, document, value));
                continue;
            }

            jsoupExpression.parse(selector);
            Elements elements = null;
            if (jsoupExpression.isRelative()) {
                elements = element.select(jsoupExpression.getCssQuery());
            } else {
                elements = document.select(jsoupExpression.getCssQuery());
            }

            if (elements == null || elements.isEmpty()) {
                continue;
            }

            if (FieldTypeEnum.Object.isEqual(type) || FieldTypeEnum.Map.isEqual(type)) {
                Map child = result.containsKey(define.getName()) ? (Map) result.get(define.getName()) : new HashMap();
                parseHtml(document, elements.first(), define, child);
                result.put(define.getName(), child);
                continue;
            }

            if (FieldTypeEnum.Array.isEqual(type) || FieldTypeEnum.List.isEqual(type)) {
                int size = elements.size();
                Object[] array = result.containsKey(define.getName()) ? (Object[]) result.get(define.getName()) : new Object[size];
                int min = Math.min(size, array.length);
                for (int i = jsoupExpression.getSkip(); i < min; i++) {
                    int j = i - jsoupExpression.getSkip();
                    Map item = array[j] != null ? (Map) array[j] : new HashMap();
                    parseHtml(document, elements.get(i), define.firstDefine(), item);
                    array[j] = item;
                }

                result.put(define.getName(), array);
                continue;
            }

            result.put(define.getName(), getElementValue(document, elements, jsoupExpression, define));
        }
    }

    private Object getElementValue(Document document, Elements elements, JsoupExpression jsoupExpression, FieldDefine define) {
        return castValue(define, document, getValueText(elements, jsoupExpression));
    }

    private Object getValueText(Elements elements, JsoupExpression jsoupExpression) {
        if (elements == null || elements.isEmpty()) {
            return null;
        }
        Element element = elements.get(0);

        if (jsoupExpression.isTextMethod()) {
            return StringUtils.trim(element.text());
        }
        if (jsoupExpression.isValMethod()) {
            return StringUtils.trim(element.val());
        }
        if (jsoupExpression.isAttrMethod()) {
            return StringUtils.trim(element.attr(jsoupExpression.getParameter()));
        }
        if (jsoupExpression.isOuterHtmlMethod()) {
            return StringUtils.trim(element.outerHtml());
        }
        if (jsoupExpression.isOwnTextMethod()) {
            return StringUtils.trim(element.ownText());
        }
        if (jsoupExpression.isHtmlMethod()) {
            return StringUtils.trim(element.html());
        }
        return StringUtils.trim(element.text());
    }
}

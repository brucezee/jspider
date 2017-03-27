package com.brucezee.jspider.parser.expression;

import com.brucezee.jspider.common.utils.SpiderStrUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 扩展的css query
 * Created by zhoubing on 2016/11/29.
 */
public class JsoupExpression {
    private String cssQuery;
    private boolean isRelative = false;
    private String method;
    private String parameter;
    private int skip = 0;

    public static final String METHOD_TEXT = ".text()";
    public static final String METHOD_HTML = ".html()";
    public static final String METHOD_OUTER_HTML = ".outerHtml()";
    public static final String METHOD_OWN_TEXT = ".ownText()";
    public static final String METHOD_VAL = ".val()";

    private static final String METHOD_ATTR_PREFIX = ".attr(";
    private static final String METHOD_ATTR_SUFFIX = ")";
    public static final String METHOD_ATTR = METHOD_ATTR_PREFIX+METHOD_ATTR_SUFFIX;

    private static final String METHOD_SKIP_PREFIX = ".skip(";
    private static final String METHOD_SKIP_SUFFIX = ")";
    public static final String METHOD_SKIP = METHOD_SKIP_PREFIX+METHOD_SKIP_SUFFIX;


    private static final String RELATIVE_PREFIX = " ";//相对路径的css查询器 前面使用空格作为标志

    public JsoupExpression reset() {
        //复用对象
        this.cssQuery = null;
        this.isRelative = false;
        this.method = null;
        this.parameter = null;
        this.skip = 0;
        return this;
    }



    public void parse(String selector) {
        reset();

        setRelative(selector.startsWith(RELATIVE_PREFIX));
        //.text()
        //.attr("href")
        //.html()
        int index = 0;
        index = selector.indexOf(METHOD_TEXT);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_TEXT);
            return;
        }

        index = selector.indexOf(METHOD_HTML);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_HTML);
            return;
        }

        index = selector.indexOf(METHOD_ATTR_PREFIX);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_ATTR);
            setParameter(getParameterFromSelector(selector));
            return;
        }

        index = selector.indexOf(METHOD_SKIP_PREFIX);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_SKIP);
            setSkip(getSkipFromSelector(selector));
            return;
        }

        index = selector.indexOf(METHOD_OUTER_HTML);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_OUTER_HTML);
            return;
        }

        index = selector.indexOf(METHOD_OWN_TEXT);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_OWN_TEXT);
            return;
        }

        index = selector.indexOf(METHOD_VAL);
        if (index > 0) {
            setCssQuery(selector.substring(0, index));
            setMethod(METHOD_VAL);
            return;
        }

        setCssQuery(selector);
    }

    private String getParameterFromSelector(String text) {
        return trimQuotation(SpiderStrUtils.getMiddleText(text, METHOD_ATTR_PREFIX, METHOD_ATTR_SUFFIX));
    }

    private int getSkipFromSelector(String text) {
        text = SpiderStrUtils.getMiddleText(text, METHOD_SKIP_PREFIX, METHOD_SKIP_SUFFIX);
        text = SpiderStrUtils.getFirstNumberFromText(text, true);
        if (text != null) {
            return Integer.parseInt(text);
        }
        return 0;
    }

    /**
     * 去除字符串两端的空格及双引号
     * @param text 文本
     * @return 处理后的字符串
     */
    private String trimQuotation(String text) {
        text = StringUtils.trim(text);
        if (text.startsWith("\"")) {
            text = text.substring(1);
        }
        if (text.endsWith("\"")) {
            text = text.substring(0, text.length()-1);
        }
        return StringUtils.trim(text);
    }

    public boolean isTextMethod() {
        return METHOD_TEXT.equals(this.method);
    }
    public boolean isHtmlMethod() {
        return METHOD_HTML.equals(this.method);
    }
    public boolean isAttrMethod() {
        return METHOD_ATTR.equals(this.method);
    }
    public boolean isOuterHtmlMethod() {
        return METHOD_OUTER_HTML.equals(this.method);
    }
    public boolean isOwnTextMethod() {
        return METHOD_OWN_TEXT.equals(this.method);
    }
    public boolean isValMethod() {
        return METHOD_VAL.equals(this.method);
    }
    public boolean isSkipMethod() {
        return METHOD_SKIP.equals(this.method);
    }

    public String getCssQuery() {
        return cssQuery;
    }

    public void setCssQuery(String cssQuery) {
        this.cssQuery = cssQuery;
    }

    public boolean isRelative() {
        return isRelative;
    }

    public void setRelative(boolean isRelative) {
        this.isRelative = isRelative;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public int getSkip() {
        return skip;
    }

    public void setSkip(int skip) {
        this.skip = skip;
    }
}

package com.brucezee.jspider.parser.define;

/**
 * Created by zhoubing on 2016/11/30.
 */
public enum SelectorTypeEnum {
    CSS("css", "解析html的jsoup css query"),
    XPATH("xpath", "解析xml的xpath"),
    JPATH("jpath", "解析json的json path"),
    REGEX("regex", "正则表达式"),
    SEGMENT("segment", "分段表达式"),
    ;

    SelectorTypeEnum(String name, String comment) {
        this.name = name;
        this.comment = comment;
    }

    private String comment;
    private String name;

    public String getComment() {
        return comment;
    }

    public String getName() {
        return name;
    }

}

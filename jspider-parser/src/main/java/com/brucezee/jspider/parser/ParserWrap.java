package com.brucezee.jspider.parser;

import com.brucezee.jspider.parser.define.FieldDefine;

/**
 * Created by zhoubing on 2016/12/1.
 */
public class ParserWrap {
    private String data;
    private FieldDefine define;

    public ParserWrap(String data, FieldDefine define) {
        this.data = data;
        this.define = define;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public FieldDefine getDefine() {
        return define;
    }

    public void setDefine(FieldDefine define) {
        this.define = define;
    }
}

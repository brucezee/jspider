package com.brucezee.jspider.parser.processor.date;

import com.brucezee.jspider.parser.processor.BaseFieldProcessor;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * Created by zhoubing on 2016/6/16.
 */
public abstract class BaseDateFieldProcessor<T> extends BaseFieldProcessor<T> {
    //字段日期的格式
    private String format;

    public BaseDateFieldProcessor(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    @Override
    protected String format(Object rootObject, String value) {
        return StringUtils.trim(value);
    }
}

package com.brucezee.jspider.parser.processor.date;

import com.brucezee.jspider.common.utils.SpiderTimeUtils;

import java.util.Date;

/**
 * 日期字符串转Date
 * Created by zhoubing on 2016/6/16.
 */
public class DateFieldProcessor extends BaseDateFieldProcessor<Date> {
    public DateFieldProcessor() {
        this(null);
    }
    public DateFieldProcessor(String format) {
        super(format);
    }

    @Override
    protected Date parse(Object rootObject, String value) {
        return SpiderTimeUtils.parseDate(value, getFormat());
    }
}

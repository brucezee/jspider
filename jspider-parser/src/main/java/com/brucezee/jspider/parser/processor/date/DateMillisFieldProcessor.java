package com.brucezee.jspider.parser.processor.date;

import com.brucezee.jspider.common.utils.SpiderTimeUtils;

/**
 * 日期字符串转时间毫秒数
 * Created by zhoubing on 2016/6/16.
 */
public class DateMillisFieldProcessor extends BaseDateFieldProcessor<Long> {
    public DateMillisFieldProcessor(String format) {
        super(format);
    }

    @Override
    protected Long parse(Object rootObject, String value) {
        return SpiderTimeUtils.parseTime(value, getFormat());
    }
}

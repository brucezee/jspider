package com.brucezee.jspider.parser.processor.number;

import com.brucezee.jspider.common.utils.SpiderNumberUtils;

/**
 * Long类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class LongFieldProcessor extends NumberFieldProcessor<Long> {
    @Override
    protected Long parse(Object rootObject, String value) {
        return SpiderNumberUtils.parseLong(value);
    }
}

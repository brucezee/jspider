package com.brucezee.jspider.parser.processor.number;

import com.brucezee.jspider.common.utils.SpiderNumberUtils;

/**
 * Double类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class DoubleFieldProcessor extends NumberFieldProcessor<Double> {
    @Override
    protected Double parse(Object rootObject, String value) {
        return SpiderNumberUtils.parseDouble(value);
    }
}

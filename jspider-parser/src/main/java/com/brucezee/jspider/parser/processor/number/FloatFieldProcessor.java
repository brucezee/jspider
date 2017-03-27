package com.brucezee.jspider.parser.processor.number;

import com.brucezee.jspider.common.utils.SpiderNumberUtils;

/**
 * Float类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class FloatFieldProcessor extends NumberFieldProcessor<Float> {
    @Override
    protected Float parse(Object rootObject, String value) {
        return SpiderNumberUtils.parseFloat(value);
    }
}

package com.brucezee.jspider.parser.processor.number;

import com.brucezee.jspider.common.utils.SpiderNumberUtils;

/**
 * Integer类型的字段处理
 * Created by zhoubing on 2016/6/16.
 */
public class IntegerFieldProcessor extends NumberFieldProcessor<Integer> {
    @Override
    protected Integer parse(Object rootObject, String value) {
        return SpiderNumberUtils.parseInt(value);
    }
}

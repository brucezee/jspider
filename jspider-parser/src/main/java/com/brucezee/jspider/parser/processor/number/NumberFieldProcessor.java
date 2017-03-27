package com.brucezee.jspider.parser.processor.number;

import com.brucezee.jspider.parser.processor.BaseFieldProcessor;
import com.brucezee.jspider.common.utils.SpiderStrUtils;

/**
 * 数字类型的字段处理器
 * Created by zhoubing on 2016/6/16.
 */
public abstract class NumberFieldProcessor<T> extends BaseFieldProcessor<T> {
    @Override
    protected String format(Object rootObject, String value) {
        return SpiderStrUtils.getFirstNumberFromText(value, false);
    }
}

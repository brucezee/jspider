package com.brucezee.jspider.parser.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Created by zhoubing on 2016/12/8.
 */
public class FieldDefineParserNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("define", new FieldDefineBeanDefinitionParser());
    }
}

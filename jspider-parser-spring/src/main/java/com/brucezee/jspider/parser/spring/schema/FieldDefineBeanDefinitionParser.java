package com.brucezee.jspider.parser.spring.schema;

import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.processor.FieldProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by zhoubing on 2016/12/8.
 */
public class FieldDefineBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        FieldDefine fieldDefine = parseFieldDefine(element);
        String id = fieldDefine.getId();
        if (StringUtils.isEmpty(id)) {
            id = fieldDefine.toString()+"-"+System.currentTimeMillis();
        }
        
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        beanDefinition.setBeanClass(FieldDefine.class);
        beanDefinition.setLazyInit(false);

        BeanDefinitionRegistry registry = parserContext.getRegistry();
        if (registry.containsBeanDefinition(id)) {
            throw new IllegalStateException("Duplicate spring bean id " + id);
        }
        registry.registerBeanDefinition(id, beanDefinition);

        MutablePropertyValues propertyValues = beanDefinition.getPropertyValues();
        propertyValues.addPropertyValue("id", id);
        propertyValues.addPropertyValue("name", fieldDefine.getName());
        propertyValues.addPropertyValue("type", fieldDefine.getType());
        propertyValues.addPropertyValue("selector", fieldDefine.getSelector());
        propertyValues.addPropertyValue("processor", fieldDefine.getProcessor());
        propertyValues.addPropertyValue("defines", fieldDefine.getDefines());

        return beanDefinition;
    }

    private FieldDefine parseFieldDefine(Element defineElement) {
        String id = defineElement.getAttribute("id");
        String type = defineElement.getAttribute("type");
        String name = defineElement.getAttribute("name");
        String selector = defineElement.getAttribute("selector");

        FieldDefine fieldDefine = new FieldDefine();
        fieldDefine.setId(id);
        fieldDefine.setType(type);
        fieldDefine.setName(name);
        fieldDefine.setSelector(selector);

        parseFieldDefines(fieldDefine, defineElement);

        return fieldDefine;
    }

    private void parseFieldDefines(FieldDefine fieldDefine, Element defineElement) {
        NodeList nodeList = defineElement.getChildNodes();
        if (nodeList != null && nodeList.getLength() > 0) {
            int length = nodeList.getLength();
            List<FieldDefine> list = new LinkedList<FieldDefine>();
            for (int i = 0; i < length; i++) {
                Node node = nodeList.item(i);
                if (node instanceof Element) {
                    Element element = (Element) node;
                    if ("selector".equals(element.getLocalName())) {
                        fieldDefine.setSelector(element.getTextContent());
                    } else if ("processor".equals(element.getLocalName())) {
                        fieldDefine.setProcessor(getFieldProcessor(element));
                    } else {
                        list.add(parseFieldDefine(element));
                    }
                }
            }

            if (list.size() > 0) {
                fieldDefine.setDefines(list.toArray(new FieldDefine[list.size()]));
            }
        }
    }

    private FieldProcessor getFieldProcessor(Element element) {
        Object[] arguments = getFieldProcessorArguments(element);
        String clazz = element.getAttribute("class");

        try {
            if (arguments == null || arguments.length == 0) {
                return (FieldProcessor) Class.forName(clazz).newInstance();
            }

            Constructor<?>[] constructors = Class.forName(clazz).getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (arguments.length == constructor.getParameterCount()) {
                    return (FieldProcessor) constructor.newInstance(arguments);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private Object[] getFieldProcessorArguments(Element element) {
        NodeList nodeList = element.getChildNodes();
        List<Object> list = new LinkedList<Object>();
        if (nodeList != null && nodeList.getLength() > 0) {
            int length = nodeList.getLength();
            for (int i = 0; i < length; i++) {
                list.add(nodeList.item(i).getTextContent());
            }
        }
        return list.toArray(new Object[list.size()]);
    }
}

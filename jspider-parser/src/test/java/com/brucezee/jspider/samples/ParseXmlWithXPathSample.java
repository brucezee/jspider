package com.brucezee.jspider.samples;

import com.alibaba.fastjson.JSON;
import com.brucezee.jspider.parser.*;
import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.processor.FieldProcessor;
import com.brucezee.jspider.parser.processor.date.DateFormatFieldProcessor;

/**
 * xpath的解析器
 * Created by brucezee on 2017/09/30.
 */
public class ParseXmlWithXPathSample {
    /**
     * 抓取到的文本数据data1, data2 ....
     * 最终获取到的完整数据对象ResultObject，其中包含了所有需要的字段
     * ResultObject不同字段来源于不同的文本数据，不同的字段来源于文本中不同的位置
     * 每个字段需要各自的表达式获取，表达式可以是json path,xpath,正则，jsoup选择器等
     * FieldDefine定义了结果对象ResultObject的字段结构及每个字段映射到文本数据的解析规则
     */

    public static void main(String[] args) {
        String data1 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "    <retCode>100</retCode>\n" +
                "    <retMsg>OK</retMsg>\n" +
                "    <data>\n" +
                "        <store>\n" +
                "            <bookStore>\n" +
                "                <manager>John</manager>\n" +
                "                <customer>Justin</customer>\n" +
                "                <name>xinhua shudian</name>\n" +
                "                <book>\n" +
                "                    <category>reference</category>\n" +
                "                    <author>Nigel Rees</author>\n" +
                "                    <title>Sayings of the Century</title>\n" +
                "                    <price>8.95</price>\n" +
                "                    <time>20150311</time>\n" +
                "                </book>\n" +
                "                <book>\n" +
                "                    <category>fiction</category>\n" +
                "                    <author>Evelyn Waugh</author>\n" +
                "                    <title>Sword of Honour</title>\n" +
                "                    <price>12.99</price>\n" +
                "                    <time>20150924</time>\n" +
                "                </book>\n" +
                "                <book>\n" +
                "                    <category>fiction</category>\n" +
                "                    <author>Herman Melville</author>\n" +
                "                    <title>Moby Dick</title>\n" +
                "                    <isbn>0-553-21311-3</isbn>\n" +
                "                    <price>8.99</price>\n" +
                "                    <time>20151212</time>\n" +
                "                </book>\n" +
                "                <book>\n" +
                "                    <category>fiction</category>\n" +
                "                    <author>J. R. R. Tolkien</author>\n" +
                "                    <title>The Lord of the Rings</title>\n" +
                "                    <isbn>0-395-19395-8</isbn>\n" +
                "                    <price>22.99</price>\n" +
                "                    <time>20161124</time>\n" +
                "                </book>\n" +
                "            </bookStore>\n" +
                "            <bicycleStore>\n" +
                "                <manager>Mike</manager>\n" +
                "                <bicycle>\n" +
                "                    <color>red</color>\n" +
                "                    <price>19.95</price>\n" +
                "                </bicycle>\n" +
                "                <factory>\n" +
                "                    <factoryName>Beijing Book Ltd.Coxxxxx</factoryName>\n" +
                "                </factory>\n" +
                "            </bicycleStore>\n" +
                "            <address>zhejiang hangzhou</address>\n" +
                "        </store>\n" +
                "        <product>\n" +
                "            <sellCount>100</sellCount>\n" +
                "            <expensive>10</expensive>\n" +
                "        </product>\n" +
                "    </data>\n" +
                "</root>";

        String data2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<root>\n" +
                "    <paymentList>\n" +
                "        <payment>\n" +
                "            <value>77788</value>\n" +
                "            <value>abc company</value>\n" +
                "            <value>55.55</value>\n" +
                "        </payment>\n" +
                "        <payment>\n" +
                "            <value>4124</value>\n" +
                "            <value>nanjing co</value>\n" +
                "            <value>19.14</value>\n" +
                "        </payment>\n" +
                "        <payment>\n" +
                "            <value>2588</value>\n" +
                "            <value>abc company</value>\n" +
                "            <value>62.09</value>\n" +
                "        </payment>\n" +
                "    </paymentList>\n" +
                "    <user channelCode=\"djd\">\n" +
                "        <authItem>security</authItem>\n" +
                "        <cityCode>621000</cityCode>\n" +
                "        <idcard>431381198709106573</idcard>\n" +
                "        <name>test</name>\n" +
                "        <phone>15800000000</phone>\n" +
                "        <provinceCode>620000</provinceCode>\n" +
                "        <token>wfTnwkbYl3My2wpbTqd</token>\n" +
                "        <userId>1</userId>\n" +
                "    </user>\n" +
                "</root>";

        //定义解析器
        //这里选择xpath的解析器
        Parser parser = new XmlParser();

        //解析得到结果

        //1 通过代码的方式定义解析映射
        ResultObject object = parser.parse(
                new ParserWrap[] {
                        new ParserWrap(data1, getFieldDefine1()),
                        new ParserWrap(data2, getFieldDefine2()),
                },
                ResultObject.class);

        System.out.println(JSON.toJSONString(object));

        //2 加载xml中定义的解析映射
        ResultObject object2 = parser.parse(
                new ParserWrap[] {
                        new ParserWrap(data1, DefineLoader.getFieldDefine(ParseJsonWithJsonPathSample.class.getResourceAsStream("/parser-defines.xml"), "bookParser-xml-part1")),
                        new ParserWrap(data2, DefineLoader.getFieldDefine(ParseJsonWithJsonPathSample.class.getResourceAsStream("/parser-defines.xml"), "bookParser-xml-part2")),
                },
                ResultObject.class);

        System.out.println(JSON.toJSONString(object2));
    }

    private static FieldDefine getFieldDefine1() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newInteger("expensive", "/root/data/product/expensive"),
                FieldDefine.newObject("store", "/root/data/store/bookStore", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", "/root/data/store/bicycleStore/bicycle", new FieldDefine[]{
                                FieldDefine.newString("color", "./color"),
                                FieldDefine.newDouble("price", "./price"),
                                FieldDefine.newString("bicycleStoreManager", "/root/data/store/bicycleStore/manager"),
                        }),
                        FieldDefine.newArray("book", "/root/data/store/bookStore/book", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", "./category"),
                                FieldDefine.newString("author", "./author"),
                                FieldDefine.newString("title", "./title"),
                                FieldDefine.newString("isbnNo", "./isbn"),//名称不一致
                                FieldDefine.newDouble("price", "./price"),
                                FieldDefine.newString("createTime", "./time", new DateFormatFieldProcessor("yyyy-MM-dd")),
                        })),
                        FieldDefine.newString("manager", "/root/data/store/bookStore/manager", new FieldProcessor<String>() {
                            @Override
                            public String process(Object rootObject, String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "/root/data/store/address"),
                        FieldDefine.newString("factoryName", "/root/data/store/bicycleStore/factory/factoryName"),
                        FieldDefine.newString("storeName", "/root/data/store/bookStore/name"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "/root/paymentList/payment", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "/root/user/cityCode"),
                                FieldDefine.newDouble("baseAmount", "./value[1]"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "/root/user/@channelCode"),//获取属性
                }),
        });

        return root;
    }

}

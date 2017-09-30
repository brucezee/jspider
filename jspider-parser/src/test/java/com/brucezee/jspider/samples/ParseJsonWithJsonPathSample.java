package com.brucezee.jspider.samples;

import com.alibaba.fastjson.JSON;
import com.brucezee.jspider.parser.*;
import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.processor.FieldProcessor;
import com.brucezee.jspider.parser.processor.date.DateFormatFieldProcessor;

/**
 * json path的解析器
 * Created by brucezee on 2017/09/30.
 */
public class ParseJsonWithJsonPathSample {
    /**
     * 抓取到的文本数据data1, data2 ....
     * 最终获取到的完整数据对象ResultObject，其中包含了所有需要的字段
     * ResultObject不同字段来源于不同的文本数据，不同的字段来源于文本中不同的位置
     * 每个字段需要各自的表达式获取，表达式可以是json path,xpath,正则，jsoup选择器等
     * FieldDefine定义了结果对象ResultObject的字段结构及每个字段映射到文本数据的解析规则
     */

    public static void main(String[] args) {
        String data1 = "{\n" +
                "  \"retCode\": 100,\n" +
                "  \"retMsg\": \"OK\",\n" +
                "  \"data\": {\n" +
                "    \"store\": {\n" +
                "      \"bookStore\": {\n" +
                "        \"manager\": \"John\",\n" +
                "        \"customer\": \"Justin\",\n" +
                "        \"name\": \"xinhua shudian\",\n" +
                "        \"book\": [\n" +
                "          {\n" +
                "            \"category\": \"reference\",\n" +
                "            \"author\": \"Nigel Rees\",\n" +
                "            \"title\": \"Sayings of the Century\",\n" +
                "            \"price\": 8.95,\n" +
                "            \"time\": \"20150311\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"category\": \"fiction\",\n" +
                "            \"author\": \"Evelyn Waugh\",\n" +
                "            \"title\": \"Sword of Honour\",\n" +
                "            \"price\": 12.99,\n" +
                "            \"time\": \"20150924\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"category\": \"fiction\",\n" +
                "            \"author\": \"Herman Melville\",\n" +
                "            \"title\": \"Moby Dick\",\n" +
                "            \"isbn\": \"0-553-21311-3\",\n" +
                "            \"price\": 8.99,\n" +
                "            \"time\": \"20151212\"\n" +
                "          },\n" +
                "          {\n" +
                "            \"category\": \"fiction\",\n" +
                "            \"author\": \"J. R. R. Tolkien\",\n" +
                "            \"title\": \"The Lord of the Rings\",\n" +
                "            \"isbn\": \"0-395-19395-8\",\n" +
                "            \"price\": 22.99,\n" +
                "            \"time\": \"20161124\"\n" +
                "          }\n" +
                "        ]\n" +
                "      },\n" +
                "      \"bicycleStore\": {\n" +
                "        \"manager\": \"Mike\",\n" +
                "        \"bicycle\": {\n" +
                "          \"color\": \"red\",\n" +
                "          \"price\": 19.95\n" +
                "        },\n" +
                "        \"factory\": {\n" +
                "          \"factoryName\": \"Beijing Book Ltd.Coxxxxx\"\n" +
                "        }\n" +
                "      },\n" +
                "      \"address\": \"zhejiang hangzhou\"\n" +
                "    },\n" +
                "    \"product\": {\n" +
                "      \"sellCount\": 100,\n" +
                "      \"expensive\": 10\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String data2 = "{\n" +
                "  \"paymentList\": [\n" +
                "    {\n" +
                "      \"baseAmount\": 77788,\n" +
                "      \"company\": \"abc company\",\n" +
                "      \"companyAmount\": 55.55\n" +
                "    },\n" +
                "    {\n" +
                "      \"baseAmount\": 446,\n" +
                "      \"company\": \"guangzhou co\",\n" +
                "      \"companyAmount\": 55.55\n" +
                "    },\n" +
                "    {\n" +
                "      \"baseAmount\": 11224,\n" +
                "      \"company\": \"nanjing co\",\n" +
                "      \"companyAmount\": 55.55\n" +
                "    }\n" +
                "  ],\n" +
                "  \"user\": {\n" +
                "    \"authItem\": \"security\",\n" +
                "    \"channelCode\": \"djd\",\n" +
                "    \"cityCode\": \"621000\",\n" +
                "    \"idcard\": \"431381198709106573\",\n" +
                "    \"name\": \"test\",\n" +
                "    \"phone\": \"15800000000\",\n" +
                "    \"provinceCode\": \"620000\",\n" +
                "    \"token\": \"wfTnwkbYl3My2wpbTqd\",\n" +
                "    \"userId\": \"1\"\n" +
                "  }\n" +
                "}";

        //定义解析器
        //这里选择json的解析器
        Parser parser = new JsonParser();

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
                        new ParserWrap(data1, DefineLoader.getFieldDefine(ParseJsonWithJsonPathSample.class.getResourceAsStream("/parser-defines.xml"), "bookParser-json-part1")),
                        new ParserWrap(data2, DefineLoader.getFieldDefine(ParseJsonWithJsonPathSample.class.getResourceAsStream("/parser-defines.xml"), "bookParser-json-part2")),
                },
                ResultObject.class);

        System.out.println(JSON.toJSONString(object2));
    }

    private static FieldDefine getFieldDefine1() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newShort("expensive", "$.data.product.expensive"),
                FieldDefine.newObject("store", "$.data.store.bookStore", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", "$.data.store.bicycleStore.bicycle", new FieldDefine[]{
                                FieldDefine.newString("color", "$..color"),
                                FieldDefine.newDouble("price", "$..price"),
                                FieldDefine.newString("bicycleStoreManager", "$.data.store.bicycleStore.manager"),
                        }),
                        FieldDefine.newArray("book", "$.data.store.bookStore.book", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", "$..category"),
                                FieldDefine.newString("author", "$..author"),
                                FieldDefine.newString("title", "$..title"),
                                FieldDefine.newString("isbnNo", "$..isbn"),//名称不一致
                                FieldDefine.newDouble("price", "$..price"),
                                FieldDefine.newString("createTime", "$..time", new DateFormatFieldProcessor("yyyy-MM-dd")),
                        })),
                        FieldDefine.newString("manager", "$.data.store.bookStore.manager", new FieldProcessor<String>() {
                            @Override
                            public String process(Object rootObject, String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "$.data.store.address"),
                        FieldDefine.newString("factoryName", "$.data.store.bicycleStore.factory.factoryName"),
                        FieldDefine.newString("storeName", "$.data.store.bookStore.name"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "$.paymentList", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "$.user.cityCode"),
                                FieldDefine.newDouble("baseAmount", "$..baseAmount"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "$.user.channelCode"),
                }),
        });

        return root;
    }

}

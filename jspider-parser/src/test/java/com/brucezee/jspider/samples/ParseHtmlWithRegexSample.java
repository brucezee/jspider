package com.brucezee.jspider.samples;

import com.alibaba.fastjson.JSON;
import com.brucezee.jspider.common.utils.SpiderTimeUtils;
import com.brucezee.jspider.parser.*;
import com.brucezee.jspider.parser.define.FieldDefine;
import com.brucezee.jspider.parser.processor.FieldProcessor;

/**
 * 正则的解析器
 * Created by brucezee on 2017/09/30.
 */
public class ParseHtmlWithRegexSample {
    /**
     * 抓取到的文本数据data1, data2 ....
     * 最终获取到的完整数据对象ResultObject，其中包含了所有需要的字段
     * ResultObject不同字段来源于不同的文本数据，不同的字段来源于文本中不同的位置
     * 每个字段需要各自的表达式获取，表达式可以是json path,xpath,正则，jsoup选择器等
     * FieldDefine定义了结果对象ResultObject的字段结构及每个字段映射到文本数据的解析规则
     */

    public static void main(String[] args) {
        String data1 = "<?xml version=\"1.0\"?>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Document</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div class=\"data\">\n" +
                "            <div class=\"store\">\n" +
                "                <div id=\"bookStore\">\n" +
                "                    <span class=\"manager\">John</span>\n" +
                "                    <span class=\"customer\">Justin</span>\n" +
                "                    <span class=\"name\">xinhua shudian</span>\n" +
                "                    <table class=\"tb\">\n" +
                "                        <tr>\n" +
                "                            <td>reference</td>\n" +
                "                            <td>Nigel Rees</td>\n" +
                "                            <td>Sayings of the Century</td>\n" +
                "                            <td>0-553-21311-3</td>\n" +
                "                            <td>8.95</td>\n" +
                "                            <td>20150311</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>fiction</td>\n" +
                "                            <td>Evelyn Waugh</td>\n" +
                "                            <td>Sword of Honour</td>\n" +
                "                            <td>0-553-21311-3</td>\n" +
                "                            <td>12.99</td>\n" +
                "                            <td>20150924</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>fiction</td>\n" +
                "                            <td>Herman Melville</td>\n" +
                "                            <td>Moby Dick</td>\n" +
                "                            <td>0-553-21311-3</td>\n" +
                "                            <td>8.99</td>\n" +
                "                            <td>20151212</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>fiction</td>\n" +
                "                            <td>J. R. R. Tolkien</td>\n" +
                "                            <td>The Lord of the Rings</td>\n" +
                "                            <td>0-395-19395-8</td>\n" +
                "                            <td>22.99</td>\n" +
                "                            <td>20161124</td>\n" +
                "                        </tr>\n" +
                "                    </table>\n" +
                "                </div>\n" +
                "                <div id=\"bicycleStore\">\n" +
                "                    <span class=\"manager\">Mike</span>\n" +
                "                    <div>\n" +
                "                        <span class=\"color\">red</span>\n" +
                "                        <span class=\"price\">19.95</span>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <span class=\"factoryName\">Beijing Book Ltd.Coxxxxx</span>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <span>zhejiang hangzhou</span>\n" +
                "            </div>\n" +
                "            <span id=\"product\">\n" +
                "                <span class=\"sellCount\">100</span>\n" +
                "                <span class=\"expensive\">10</span>\n" +
                "            </span>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>";

        String data2 = "<?xml version=\"1.0\"?>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Document</title>\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <div>\n" +
                "            <table id=\"paymentList\">\n" +
                "                <tr>\n" +
                "                    <td>77788</td>\n" +
                "                    <td>abc company</td>\n" +
                "                    <td>55.55</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>446</td>\n" +
                "                    <td>guangzhou co</td>\n" +
                "                    <td>55.55</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <td>11224</td>\n" +
                "                    <td>nanjing co</td>\n" +
                "                    <td>55.55</td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "            <div class=\"user\" channelCode=\"djd\">\n" +
                "                <label class=\"authItem\">security</label>\n" +
                "                <span class=\"cityCode\">621000</span>\n" +
                "                <label class=\"idcard\">431381198709106573</label>\n" +
                "                <label class=\"name\">test</label>\n" +
                "                <label class=\"phone\">15800000000</label>\n" +
                "                <label class=\"provinceCode\">620000</label>\n" +
                "                <label class=\"token\">wfTnwkbYl3My2wpbTqd</label>\n" +
                "                <label class=\"userId\">1</label>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </body>\n" +
                "</html>";



        //定义解析器
        //这里选择正则的解析器
        Parser parser = new RegexParser();

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
                        new ParserWrap(data1, DefineLoader.getFieldDefine(ParseHtmlWithRegexSample.class.getResourceAsStream("/parser-defines.xml"), "bookParser-regex-part1")),
                        new ParserWrap(data2, DefineLoader.getFieldDefine(ParseHtmlWithRegexSample.class.getResourceAsStream("/parser-defines.xml"), "bookParser-regex-part2")),
                },
                ResultObject.class);

        System.out.println(JSON.toJSONString(object2));
    }

    private static FieldDefine getFieldDefine1() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newInteger("expensive", "regex[1]:class=\"expensive\">(.*?)</span>"),
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newObject("bicycle", new FieldDefine[]{
                                FieldDefine.newString("color", "regex[1]:class=\"color\">(.*?)</span>"),
                                FieldDefine.newDouble("price", "regex[1]:class=\"price\">(.*?)</span>"),
                                FieldDefine.newString("bicycleStoreManager", "regex:id=\"bicycleStore\">([\\w\\W]*)</span> regex[1]:class=\"manager\">(.*?)</span>"),
                        }),
                        FieldDefine.newArray("book", "regex:<table([\\w\\W]*)</table> regex:<tr([\\w\\W]*?)</tr>", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("category", " regex[1]:<td>(.*?)</td>"),
                                FieldDefine.newString("author", " regex[3]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),
                                FieldDefine.newString("title", " regex[5]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),
                                FieldDefine.newString("isbnNo", " regex[7]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),//名称不一致
                                FieldDefine.newDouble("price", " regex[9]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>"),
                                FieldDefine.newString("createTime", " regex[11]:<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>([\\w\\W]*?)<td>(.*?)</td>", new FieldProcessor<String>() {
                                    @Override
                                    public String process(Object rootObject, String value) {
                                        return SpiderTimeUtils.parseDateString(value, "yyyy-MM-dd");
                                    }
                                }),
                        })),
                        FieldDefine.newString("manager", "regex:id=\"bookStore\">([\\w\\W]*)table regex[1]:class=\"manager\">(.*?)</span>", new FieldProcessor<String>() {
                            @Override
                            public String process(Object rootObject, String value) {
                                return value != null ? value + " Joden" : "";
                            }
                        }),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("address", "regex:id=\"bicycleStore\">([\\w\\W]*)id=\"product\"> regex[1]:<span>(.*)</span>"),
                        FieldDefine.newString("factoryName", "regex:id=\"bicycleStore\">([\\w\\W]*)id=\"product\"> regex[1]:class=\"factoryName\">(.*?)</span>"),
                        FieldDefine.newString("storeName", "regex:id=\"bookStore\">([\\w\\W]*)class=\"tb\"> regex[1]:class=\"name\">(.*?)</span>"),
                }),
        });

        return root;
    }
    private static FieldDefine getFieldDefine2() {
        FieldDefine root = new FieldDefine(new FieldDefine[]{
                FieldDefine.newObject("store", new FieldDefine[]{
                        FieldDefine.newArray("book", "<table([\\w\\W]*?)</table> regex:<tr([\\w\\W]*?)</tr>", FieldDefine.newObject(new FieldDefine[]{
                                FieldDefine.newString("cityCode", "regex[1]:class=\"cityCode\">(.*?)</span>"),
                                FieldDefine.newDouble("baseAmount", " regex[1]:<td>(.*?)</td>([\\w\\W]*)<td>(.*?)</td>([\\w\\W]*)<td>(.*?)</td>"),
                        })),
                }),
                FieldDefine.newObject("mixInfo", new FieldDefine[]{
                        FieldDefine.newString("channelCode", "regex:class=\"user\"([\\w\\W]*?)</div> regex[1]:channelCode=\"(.*?)\""),//获取属性
                }),
        });

        return root;
    }

}

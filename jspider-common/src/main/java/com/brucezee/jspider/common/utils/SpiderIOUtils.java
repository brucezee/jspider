package com.brucezee.jspider.common.utils;

import java.io.*;

/**
 * IO相关工具类
 * Created by brucezee on 2017/1/10.
 */
public class SpiderIOUtils {
    /**
     * 关闭资源
     * @param object 可被关闭的对象
     */
    public static void closeQuietly(Object object) {
        if (object != null) {
            try {
                if (object instanceof Closeable) {
                    ((Closeable) object).close();
                } else if (object instanceof InputStream) {
                    ((InputStream) object).close();
                } else if (object instanceof OutputStream) {
                    ((OutputStream) object).close();
                } else if (object instanceof Reader) {
                    ((Reader) object).close();
                } else if (object instanceof Writer) {
                    ((Writer) object).close();
                }
            } catch (Exception e) {
            }
        }
    }
}

package com.brucezee.jspider;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理结果集
 * Created by brucezee on 2017/1/5.
 */
public class Result extends HashMap<String, Object> {
    public Result(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public Result(int initialCapacity) {
        super(initialCapacity);
    }

    public Result() {
        super();
    }

    public Result(Map<? extends String, ?> m) {
        super(m);
    }

    /**
     * 获取指定key对应的指定类型的对象
     * @param key 缓存key
     * @param <T> 对象类型
     * @return 失败返回null
     */
    public <T> T getAs(String key) {
        return (T) super.get(key);
    }
}

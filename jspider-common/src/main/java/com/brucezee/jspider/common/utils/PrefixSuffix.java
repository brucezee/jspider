package com.brucezee.jspider.common.utils;

/**
 * 前缀和后缀的配置
 * Created by brucezee on 2016/5/17.
 */
public class PrefixSuffix {
    public String prefix;               //前缀
    public String suffix;               //后缀
    public boolean lazyPrefix = true;   //是否使用第一次找到的前缀作为截取前缀
    public boolean lazySuffix = true;   //是否使用第一次找到的后缀作为截取后缀

    public PrefixSuffix(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }
    public PrefixSuffix(String prefix, String suffix, boolean lazyPrefix, boolean lazySuffix) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.lazyPrefix = lazyPrefix;
        this.lazySuffix = lazySuffix;
    }
}

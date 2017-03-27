package com.brucezee.jspider.scheduler.handler;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.common.Config;

import java.nio.charset.Charset;

/**
 * 基于BloomFilter的去重处理器
 * Created by brucezee on 2017/1/16.
 */
public class BloomFilterRepeatHandler implements RepeatHandler {
    private BloomFilter bloomFilter;    //BloomFilter
    private int expectedInsertions;     //expectedInsertions
    private double fpp;                 //fpp

    public BloomFilterRepeatHandler(int expectedInsertions) {
        this(expectedInsertions, 0.01);
    }

    public BloomFilterRepeatHandler(int expectedInsertions, double fpp) {
        this.expectedInsertions = expectedInsertions;
        this.fpp = fpp;
        this.bloomFilter = createBloomFilter(expectedInsertions, fpp);
    }

    protected BloomFilter createBloomFilter(int expectedInsertions, double fpp) {
        return BloomFilter.create(Funnels.stringFunnel(Charset.forName(Config.DEFAULT_CHARSET)), expectedInsertions, fpp);
    }
    
    @Override
    public boolean isDuplicate(Task task, Request request) {
        return bloomFilter.mightContain(request.key());
    }

    @Override
    public void addRepeatCheck(Task task, Request request) {
        bloomFilter.put(request.key());
    }

    @Override
    public void resetAllRepeatCheck(Task task) {
        bloomFilter = createBloomFilter(expectedInsertions, fpp);
    }

    @Override
    public void resetRequestRepeatCheck(Task task, Request request) {
        throw new UnsupportedOperationException();
    }
}

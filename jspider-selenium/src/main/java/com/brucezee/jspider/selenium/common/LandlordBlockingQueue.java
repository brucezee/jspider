package com.brucezee.jspider.selenium.common;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by brucezee on 2017/1/12.
 */
public class LandlordBlockingQueue<T> extends LinkedBlockingQueue<T> {
    private AtomicInteger count = new AtomicInteger(0);
    private int capacity;

    public LandlordBlockingQueue(int capacity) {
        super(capacity);
        this.capacity = capacity;
    }

    @Override
    public boolean add(T t) {
        if (isNeedMore()) {
            count.incrementAndGet();
            return super.add(t);
        }
        return false;
    }

    public synchronized boolean isNeedMore() {
        return count.get() < capacity;
    }

    public void resetOne() {
        count.decrementAndGet();
    }
}

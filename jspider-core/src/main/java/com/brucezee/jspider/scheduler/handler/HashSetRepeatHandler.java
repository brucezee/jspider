package com.brucezee.jspider.scheduler.handler;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 基于HashSet的去重处理器
 * Created by brucezee on 2017/1/16.
 */
public class HashSetRepeatHandler implements RepeatHandler {
    private Set<String> urls = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>()); //根据url去重

    @Override
    public boolean isDuplicate(Task task, Request request) {
        return urls.contains(request.key());
    }

    @Override
    public void addRepeatCheck(Task task, Request request) {
        urls.add(request.key());
    }

    @Override
    public void resetAllRepeatCheck(Task task) {
        urls.clear();
    }

    @Override
    public void resetRequestRepeatCheck(Task task, Request request) {
        urls.remove(request.key());
    }
}

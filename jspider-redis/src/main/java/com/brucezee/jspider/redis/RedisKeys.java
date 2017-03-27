package com.brucezee.jspider.redis;

import com.brucezee.jspider.Task;

/**
 * Created by brucezee on 2017/1/7.
 */
public class RedisKeys {
    private static final String SET_KEY_SUFFIX = ".scheduler.set";
    private static final String QUEUE_KEY_SUFFIX = ".scheduler.queue";

    private static final String ZSET_PREFIX = ".scheduler.zset";
    private static final String NO_PRIORITY_SUFFIX = ".zero";
    private static final String PLUS_PRIORITY_SUFFIX = ".plus";
    private static final String MINUS_PRIORITY_SUFFIX = ".minus";

    /**
     * 用于去重的集合
     * @param task 爬虫任务
     * @return 用于去重的集合key
     */
    public static String getSetKey(Task task) {
        return task.getUUID() + SET_KEY_SUFFIX;
    }

    /**
     * 用于任务分发的队列
     * @param task 爬虫任务
     * @return 用于任务分发的队列的key
     */
    public static String getQueueKey(Task task) {
        return task.getUUID() + QUEUE_KEY_SUFFIX;
    }

    /**
     * 优先级大于0的有序队列
     * @param task 爬虫任务
     * @return 优先级大于0的有序队列key
     */
    public static String getZsetPlusPriorityKey(Task task) {
        return task.getUUID() + ZSET_PREFIX + PLUS_PRIORITY_SUFFIX;
    }

    /**
     * 优先级等于0的队列
     * @param task 爬虫任务
     * @return 优先级等于0的队列key
     */
    public static String getQueueNoPriorityKey(Task task) {
        return getQueueKey(task) + NO_PRIORITY_SUFFIX;
    }

    /**
     * 优先级小于0的有序队列
     * @param task 爬虫任务
     * @return 优先级小于0的有序队列key
     */
    public static String getZsetMinusPriorityKey(Task task) {
        return ZSET_PREFIX + task.getUUID() + MINUS_PRIORITY_SUFFIX;
    }
}

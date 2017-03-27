package com.brucezee.jspider.redis;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.scheduler.handler.RepeatHandler;
import com.brucezee.jspider.paging.PagingRequestFactory;
import com.brucezee.jspider.serializer.Serializer;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.Set;

/**
 * 实现redis优先执行的请求任务调度器
 * Created by brucezee on 2017/1/16.
 */
public class ShardedRedisPriorityScheduler extends ShardedRedisScheduler {
    public ShardedRedisPriorityScheduler(ShardedJedisPool jedisPool) {
        super(jedisPool);
    }

    public ShardedRedisPriorityScheduler(ShardedJedisPool jedisPool, Serializer<Request> serializer) {
        super(jedisPool, serializer);
    }

    public ShardedRedisPriorityScheduler(ShardedJedisPool jedisPool, Serializer<Request> serializer, RepeatHandler repeatHandler, PagingRequestFactory pagingRequestFactory) {
        super(jedisPool, serializer, repeatHandler, pagingRequestFactory);
    }

    @Override
    protected void pushWhenNoRepeat(Task task, Request request) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            String content = serializer.serialize(request);
            if(request.getPriority() == 0) {
                jedis.rpush(RedisKeys.getQueueNoPriorityKey(task), content);
            } else if (request.getPriority() > 0) {
                jedis.zadd(RedisKeys.getZsetPlusPriorityKey(task), request.getPriority(), content);
            } else {
                jedis.zadd(RedisKeys.getZsetMinusPriorityKey(task), request.getPriority(), content);
            }

            jedis.sadd(RedisKeys.getSetKey(task), request.key());
        } finally {
            jedis.close();
        }
    }

    @Override
    public Request doPoll(Task task) {
        ShardedJedis jedis = jedisPool.getResource();
        try {
            String content = null;
            Set<String> contents = jedis.zrevrange(RedisKeys.getZsetPlusPriorityKey(task), 0, 0);
            if (contents != null && !contents.isEmpty()) {
                content = contents.toArray(new String[0])[0];
                jedis.zrem(RedisKeys.getZsetPlusPriorityKey(task), content);
            } else {
                content = jedis.lpop(RedisKeys.getQueueNoPriorityKey(task));
                if (StringUtils.isBlank(content)) {
                    contents = jedis.zrevrange(RedisKeys.getZsetMinusPriorityKey(task), 0, 0);
                    if (contents != null && !contents.isEmpty()) {
                        content = contents.toArray(new String[0])[0];
                        jedis.zrem(RedisKeys.getZsetPlusPriorityKey(task), content);
                    }
                }
            }

            return StringUtils.isNotBlank(content) ? serializer.deserialize(content) : null;
        } finally {
            jedis.close();
        }
    }
}

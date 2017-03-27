package com.brucezee.jspider.redis;

import com.brucezee.jspider.Request;
import com.brucezee.jspider.Task;
import com.brucezee.jspider.monitor.SchedulerMonitor;
import com.brucezee.jspider.scheduler.NoRepeatScheduler;
import com.brucezee.jspider.scheduler.handler.RepeatHandler;
import com.brucezee.jspider.paging.PagingRequestFactory;
import com.brucezee.jspider.serializer.RequestJsonSerializer;
import com.brucezee.jspider.serializer.Serializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by brucezee on 2017/1/7.
 */
public class RedisScheduler extends NoRepeatScheduler implements SchedulerMonitor {
    protected JedisPool jedisPool;
    protected Serializer<Request> serializer;

    public RedisScheduler(JedisPool jedisPool) {
        this(jedisPool, new RequestJsonSerializer());
    }

    public RedisScheduler(JedisPool jedisPool, Serializer<Request> serializer) {
        this(jedisPool, serializer, new RedisRepeatHandler(jedisPool), null);
    }

    public RedisScheduler(JedisPool jedisPool, Serializer<Request> serializer,
                          RepeatHandler repeatHandler, PagingRequestFactory pagingRequestFactory) {
        super(repeatHandler, pagingRequestFactory);
        this.jedisPool = jedisPool;
        this.serializer = serializer;
    }

    @Override
    protected void pushWhenNoRepeat(Task task, Request request) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.rpush(RedisKeys.getQueueKey(task), serializer.serialize(request));
        } finally {
            jedis.close();
        }
    }

    @Override
    public Request doPoll(Task task) {
        Jedis jedis = jedisPool.getResource();
        try {
            return serializer.deserialize(jedis.lpop(RedisKeys.getQueueKey(task)));
        } finally {
            jedis.close();
        }
    }

    @Override
    public int getTotalCount(Task task) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long size = jedis.scard(RedisKeys.getSetKey(task));
            return size.intValue();
        } finally {
            jedis.close();
        }
    }

    @Override
    public int getLeftCount(Task task) {
        Jedis jedis = jedisPool.getResource();
        try {
            Long size = jedis.llen(RedisKeys.getQueueKey(task));
            return size.intValue();
        } finally {
            jedis.close();
        }
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public Serializer<Request> getSerializer() {
        return serializer;
    }

    public void setSerializer(Serializer<Request> serializer) {
        this.serializer = serializer;
    }

    public static class RedisRepeatHandler implements RepeatHandler {
        protected JedisPool jedisPool;

        public RedisRepeatHandler(JedisPool jedisPool) {
            this.jedisPool = jedisPool;
        }

        @Override
        public boolean isDuplicate(Task task, Request request) {
            Jedis jedis = jedisPool.getResource();
            try {
                return jedis.sismember(RedisKeys.getSetKey(task), request.key());
            } finally {
                jedis.close();
            }
        }

        @Override
        public void addRepeatCheck(Task task, Request request) {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.sadd(RedisKeys.getSetKey(task), request.key());
            } finally {
                jedis.close();
            }
        }

        @Override
        public void resetAllRepeatCheck(Task task) {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.del(RedisKeys.getSetKey(task));
            } finally {
                jedis.close();
            }
        }

        @Override
        public void resetRequestRepeatCheck(Task task, Request request) {
            Jedis jedis = jedisPool.getResource();
            try {
                jedis.srem(RedisKeys.getSetKey(task), request.key());
            } finally {
                jedis.close();
            }
        }
    }
    
}

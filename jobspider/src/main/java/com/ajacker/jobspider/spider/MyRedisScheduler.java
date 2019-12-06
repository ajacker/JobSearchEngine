package com.ajacker.jobspider.spider;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.scheduler.DuplicateRemovedScheduler;
import us.codecraft.webmagic.scheduler.MonitorableScheduler;
import us.codecraft.webmagic.scheduler.component.DuplicateRemover;

/**
 * @author ajacker
 * @date 2019/10/28 22:15
 */
@Slf4j
@Component
public class MyRedisScheduler extends DuplicateRemovedScheduler implements MonitorableScheduler, DuplicateRemover {

    private static final String QUEUE_PREFIX = "queue_";
    private static final String SET_PREFIX = "set_";
    private static final String ITEM_PREFIX = "item_";
    @Value("${spider.startUrl}")
    private String exceptUrl;
    @Autowired
    private JedisPool pool;

    @Override
    public void resetDuplicateCheck(Task task) {
        try (Jedis jedis = this.pool.getResource()) {
            jedis.del(this.getSetKey(task));
        }

    }

    @Override
    public boolean isDuplicate(Request request, Task task) {
        boolean isDuplicate;
        try (Jedis jedis = this.pool.getResource()) {
            isDuplicate = jedis.sadd(this.getSetKey(task), request.getUrl()) == 0L;
        }
        return isDuplicate;
    }

    @Override
    protected void pushWhenNoDuplicate(Request request, Task task) {
        try (Jedis jedis = this.pool.getResource()) {
            jedis.rpush(this.getQueueKey(task), request.getUrl());
            if (request.getExtras() != null) {
                String field = DigestUtils.shaHex(request.getUrl());
                String value = JSON.toJSONString(request);
                jedis.hset(ITEM_PREFIX + task.getUUID(), field, value);
            }
        }
    }

    @Override
    public synchronized Request poll(Task task) {

        String key;
        try (Jedis jedis = this.pool.getResource()) {
            String url = jedis.lpop(this.getQueueKey(task));
            if (url != null) {
                key = ITEM_PREFIX + task.getUUID();
                String field = DigestUtils.shaHex(url);
                byte[] bytes = jedis.hget(key.getBytes(), field.getBytes());
                Request request;
                Request tmp;
                if (bytes != null) {
                    request = JSON.parseObject(new String(bytes), Request.class);
                    tmp = request;
                    return tmp;
                }

                request = new Request(url);
                tmp = request;
                return tmp;
            }
        }
        return new Request(exceptUrl);
    }

    private String getSetKey(Task task) {
        return SET_PREFIX + task.getUUID();
    }

    private String getQueueKey(Task task) {
        return QUEUE_PREFIX + task.getUUID();
    }

    protected String getItemKey(Task task) {
        return ITEM_PREFIX + task.getUUID();
    }

    @Override
    public int getLeftRequestsCount(Task task) {
        int count;
        try (Jedis jedis = this.pool.getResource()) {
            Long size = jedis.llen(this.getQueueKey(task));
            count = size.intValue();
        }
        return count;
    }

    @Override
    public int getTotalRequestsCount(Task task) {
        int count;
        try (Jedis jedis = this.pool.getResource()) {
            Long size = jedis.scard(this.getSetKey(task));
            count = size.intValue();
        }
        return count;
    }
}

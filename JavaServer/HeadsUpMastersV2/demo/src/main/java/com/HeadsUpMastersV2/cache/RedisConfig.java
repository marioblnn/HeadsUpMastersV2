package com.HeadsUpMastersV2.cache;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;;

public class RedisConfig {
    private static JedisPool pool;

    public static synchronized JedisPool getPool(){
        if (pool == null) {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(32);
            config.setMaxIdle(16);
            pool = new JedisPool(config, "localhost", 6379);
        }
        return pool;
    }
}

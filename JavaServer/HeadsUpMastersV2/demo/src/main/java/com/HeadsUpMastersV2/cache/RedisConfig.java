package com.HeadsUpMastersV2.cache;


import redis.clients.jedis.ConnectionPoolConfig;
import redis.clients.jedis.JedisPooled;

public class RedisConfig {
    private static final JedisPooled jedisPooled;
    

    static {
        ConnectionPoolConfig poolConf = new ConnectionPoolConfig();
        poolConf.setMaxTotal(32);
        poolConf.setMaxIdle(16);
        poolConf.setMinIdle(4);
        jedisPooled = new JedisPooled(poolConf, "localhost", 6379);;

    }
    


    public static JedisPooled getJedis(){
        return jedisPooled;
    }
    
}

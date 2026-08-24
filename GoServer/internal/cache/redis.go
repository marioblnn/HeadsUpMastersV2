package cache

import (
	"fmt"
	"github.com/redis/go-redis/v9"
	"GoServer/config"
)

type AppRedis struct{
	AppCache *redis.Client
}

func NewCache(r *redis.Client) *AppRedis {
	return &AppRedis{
		AppCache: r,
	}
}

func GetRedisClient() *redis.Client {
	client := redis.NewClient(&redis.Options{
		Addr: config.LoadConfig().RedisServerURL,
	})
	fmt.Println("Connected to redis")
	return client;
}
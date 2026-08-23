package cache

import (
	"fmt"
	"github.com/redis/go-redis/v9"
	"GoServer/config"
)

func GetRedisClient() *redis.Client {
	client := redis.NewClient(&redis.Options{
		Addr: config.LoadConfig().RedisServerURL,
	})
	fmt.Println("Connected to redis")
	return client
}
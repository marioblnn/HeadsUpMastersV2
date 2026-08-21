package main

import (
	"GoServer/internal/client/actions"
	"fmt"
	//"GoServer/internal/client/network"
	"GoServer/internal/redis"
	//"fmt"
)


func main() {
	rediscon := redis.RedisClient()
	tables, _ := actions.ViewTables(rediscon)
	fmt.Println(tables)
}
package actions

import (
	tablepb "GoServer/proto/table/v1"

	"github.com/redis/go-redis/v9"
)



type GameEngine struct {
	RedisClient redis.Client
	TableClient tablepb.TableServiceClient
}
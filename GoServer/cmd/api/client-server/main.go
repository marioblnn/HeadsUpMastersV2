package main

import (
	"GoServer/internal/api"
	"GoServer/internal/backend"
	"GoServer/internal/cache"
	"GoServer/internal/worker"
	"GoServer/internal/ws"
	"GoServer/proto/table/v1"
)

func main() {
	redisConn := cache.GetRedisClient()
	grpcConn := backend.InitGRPC()
	defer grpcConn.Close()
	wsHub := ws.NewWSHub()
	
	go worker.SendLobbyUpdates(redisConn, wsHub)

	gateway := api.APIGateway{
		Hub: wsHub,
		AppCache: cache.NewCache(redisConn),
		Engine: &backend.GameEngine{
			TableClient: table.NewTableServiceClient(grpcConn),
		},
	}

	

	gateway.Start(":8090")
	
}
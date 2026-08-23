package main

import (
	"GoServer/auth"
	"GoServer/internal/cache"
	clientNetwork "GoServer/internal/client/network"
	serverNetwork "GoServer/internal/server/network"
	tablepb "GoServer/proto/table/v1"
	"context"
	"fmt"

	//"GoServer/internal/server/handlers"
	"GoServer/internal/client/actions"
	"GoServer/internal/server/handlers"
	"net/http"
)

func main() {
	conn := clientNetwork.InitGRPC()

	tableClient := tablepb.NewTableServiceClient(conn)
	cache := cache.GetRedisClient()

	Api := &handlers.HTTPHandler{
		Handler: &actions.GameEngine{
			RedisClient: *cache,
			TableClient: tableClient,
		},
	}

	port := ":8080"
	mux := http.NewServeMux()
	mux.HandleFunc("/assign-guest", auth.AssignGuest)
	//mux.HandleFunc("/table-view", $$)
	mux.HandleFunc("ws/join-table", Api.JoinTableRequest)
	fmt.Println(cache.Get(context.Background(), "view-tables"))
	go actions.ListenToLobby(cache)

	serverNetwork.NewServer(port, mux)
}
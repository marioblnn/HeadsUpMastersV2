package main

import (
	tablepb "GoServer/proto/table/v1"
	clientNetwork "GoServer/internal/client/network"
	serverNetwork "GoServer/internal/server/network"
	"GoServer/auth"
	//"GoServer/internal/server/handlers"
	"GoServer/internal/client/actions"
	"GoServer/internal/server/handlers"
	"net/http"
)

func main() {
	conn := clientNetwork.InitGRPC()

	tableClient := tablepb.NewTableServiceClient(conn)

	Api := &handlers.HTTPHandler{
		Handler: &actions.GameEngine{
			TableClient: tableClient,
		},
	}

	port := ":8080"
	mux := http.NewServeMux()
	mux.HandleFunc("/assign-guest", auth.AssignGuest)
	//mux.HandleFunc("/table-view", $$)
	mux.HandleFunc("ws/join-table", Api.JoinTableRequest)

	serverNetwork.NewServer(port, mux)
}
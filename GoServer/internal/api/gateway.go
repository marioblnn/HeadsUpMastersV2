package api

import (
	"fmt"
	"log"
	"net/http"

	"GoServer/internal/backend" 
	"GoServer/internal/ws"
)


type APIGateway struct {
	Engine *backend.GameEngine
	Hub    *ws.WSHub
}


func (g *APIGateway) HandleHealth(w http.ResponseWriter, r *http.Request) {
	w.WriteHeader(http.StatusOK)
	w.Write([]byte("OK"))
}


func (g *APIGateway) Start(port string) {
	router := http.NewServeMux()
	router.HandleFunc("/health", enableCORS(g.HandleHealth))
	


	fmt.Printf("Starting the Gateway server on port %s...\n", port)
	err := http.ListenAndServe(port, router)
	if err != nil {
		log.Fatal("Server crashed: ", err)
	}
}
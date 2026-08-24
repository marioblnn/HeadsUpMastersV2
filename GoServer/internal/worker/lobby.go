package worker

import (
	"GoServer/internal/ws"
	"context"
	"fmt"
	"log"

	"github.com/coder/websocket"
	"github.com/redis/go-redis/v9"
)

func LobbyUpdates(r *redis.Client, hub *ws.WSHub) {
	ctx := context.Background() 
	
	pubsub := r.Subscribe(ctx, "view-tables")
	defer pubsub.Close()
	ch := pubsub.Channel()

	fmt.Println("Listening for lobby updates...")
	
	for msg := range ch {
		payloadBytes := []byte(msg.Payload)
		
		hub.Mu.RLock()
		for client := range hub.Clients {
			err := client.Write(ctx, websocket.MessageText, payloadBytes)
			if err != nil {
				log.Printf("Error writing to WS client: %v", err)
			}
		}
		hub.Mu.RUnlock()
	}
}
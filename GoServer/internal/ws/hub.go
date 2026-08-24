package ws

import (
	"sync"
	"github.com/coder/websocket"
)

type WSHub struct {
	Clients map[*websocket.Conn]bool
	Mu  sync.RWMutex
}

func NewWSHub() *WSHub {
	return &WSHub{
		Clients: map[*websocket.Conn]bool{},
	}
}
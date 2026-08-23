package network

import (
	"fmt"
	"net/http"
	"github.com/coder/websocket"
)

func EstablishWSConn(w http.ResponseWriter, r *http.Request) (*websocket.Conn, error) {
	conn, err := websocket.Accept(w, r, &websocket.AcceptOptions{
		InsecureSkipVerify: true,
	})
	if err != nil {
		return nil, fmt.Errorf("Could not establish websocket conn: %v", err)
	}
	return conn, nil
}
package main

import (
	"GoServer/internal/client/network"
	"fmt"
)


func main() {
	conn := network.StartClient()
	fmt.Println(conn)
}
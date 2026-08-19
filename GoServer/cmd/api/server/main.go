package main


import "GoServer/internal/server/network"

func main() {
	port := ":8080"
	network.NewServer(port)
}
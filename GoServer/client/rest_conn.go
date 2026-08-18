package client

import (
	"GoServer/config"
	"fmt"
	"net"
)

func PingFrontendClient() {
	address := config.LoadConfig().FrontendURL
	conn, err := net.Dial("tcp", address)
	if err != nil {
		fmt.Println("Could not connect: \n", err)
	}
	defer conn.Close()
	fmt.Printf("Frontend client is up! \n")
}

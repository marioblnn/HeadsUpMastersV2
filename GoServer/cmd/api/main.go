package main

import (
	//"GoServer/user"
	//"GoServer/auth"
	//"GoServer/client/grpc"
	"GoServer/client"
	"fmt"
	//"net"
	//"os/user"
	//"context"
	//"google.golang.org/grpc"
	//"google.golang.org/grpc/credentials/insecure"
	//tablepb "GoServer/proto/table/v1"
)

func main() {
	fmt.Println("Starting the Go Client....")
	fmt.Printf("Connecting to the Frontend Client...\n",)
	client.PingFrontendClient()
}

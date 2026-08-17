package main

import (
	"GoServer/user"
	//"GoServer/auth"
	"GoServer/client/grpc"
	"fmt"
	//"os/user"
	//"context"
	//"google.golang.org/grpc"
	//"google.golang.org/grpc/credentials/insecure"
	//tablepb "GoServer/proto/table/v1"
)

func main() {
	fmt.Println("Starting the Go Client....")
	testGuest := user.NewGuest()
	grpc.JoinTable("table-001", testGuest.GetDisplayName(), 1, 200)
}

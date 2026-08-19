package network

import (
	"fmt"
	"time"
	"GoServer/config"
	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	"google.golang.org/grpc/keepalive"
)


func GRPCClientConnect(address string) (*grpc.ClientConn, error) {
	kacp := keepalive.ClientParameters{
		Time: 10 * time.Second,
		Timeout: time.Second * 5,
		PermitWithoutStream: true,
	}

	opts := []grpc.DialOption{
		grpc.WithTransportCredentials(insecure.NewCredentials()),
		grpc.WithKeepaliveParams(kacp),
		grpc.WithDefaultCallOptions(
			grpc.MaxCallRecvMsgSize(1024 * 1024 * 16), 
		),
	}

	conn, err := grpc.NewClient(address, opts...)
	if err != nil {
		return nil, fmt.Errorf("failed to create grpc client: %w", err)
	}
	return  conn, nil
}


func EstablishConn() (*grpc.ClientConn, error){
	fmt.Println("Connecting to the java backend...")
	conn , err := GRPCClientConnect(config.LoadConfig().JavaServerURL)
	if err != nil {
		return nil, fmt.Errorf("Did not connect to the java backend: %w", err)
		
	}
	fmt.Println("Connected to the java backend on a keep alive conn")
	return conn, nil
}
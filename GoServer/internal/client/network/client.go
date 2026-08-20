package network

import (
	"GoServer/config"
	"context"
	"fmt"
	"time"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials/insecure"
	healthpb "google.golang.org/grpc/health/grpc_health_v1"
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


func CheckHealth(conn *grpc.ClientConn) error {
	client := healthpb.NewHealthClient(conn)
	ctx, cancel := context.WithTimeout(context.Background(), time.Second * 3)
	defer cancel()

	resp, err := client.Check(ctx, &healthpb.HealthCheckRequest{Service: ""})
	if err != nil {
		return fmt.Errorf("Backend unreachable: %v", err)
	}

	if resp.GetStatus() != healthpb.HealthCheckResponse_SERVING {
		return fmt.Errorf("Backend is not serving: %v", resp.GetStatus())
	}
	fmt.Println("Backend server is reachable")
	return nil
}


func EstablishConn() (*grpc.ClientConn, error){
	fmt.Println("Connecting to the java backend...")
	conn , err := GRPCClientConnect(config.LoadConfig().JavaServerURL)
	if err != nil {
		return nil, fmt.Errorf("Did not connect to the game engine backend: %w", err)
		
	}
	fmt.Println("Trying to connect to the backend...")
	
	return conn, nil
}

func StartClient() *grpc.ClientConn {
	fmt.Println("Starting the Client...")
	fmt.Println("Trying to connect to the server...")
	for {
		conn, err := tryToConnect()
		if err != nil{
			fmt.Println(err)
			fmt.Println("Trying again...")
			time.Sleep(time.Second * 2)
			continue
		}
		return conn
	}
	
}


func tryToConnect() (*grpc.ClientConn, error) {
	conn , err := EstablishConn()
	if err != nil {
		return nil, fmt.Errorf("Connection to game engine failed: %v", err)
	}
	err = CheckHealth(conn)
	if err != nil {
		return nil, fmt.Errorf("Error occured: %v", err)
	}
	return conn, nil
}
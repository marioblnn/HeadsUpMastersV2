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


func InitGRPC() *grpc.ClientConn {
    fmt.Println("Configuring gRPC connection to Java backend...")

    kacp := keepalive.ClientParameters{
        Time:                10 * time.Second,
        Timeout:             time.Second * 5,
        PermitWithoutStream: true,
    }

    opts := []grpc.DialOption{
        grpc.WithTransportCredentials(insecure.NewCredentials()),
        grpc.WithKeepaliveParams(kacp),
        grpc.WithDefaultCallOptions(
            grpc.MaxCallRecvMsgSize(1024 * 1024 * 16),
        ),
    }

    conn, err := grpc.NewClient(config.LoadConfig().JavaServerURL, opts...)
    if err != nil {
        panic(fmt.Sprintf("Failed to create gRPC client: %v", err))
    }
    healthClient := healthpb.NewHealthClient(conn)
    
    for {
        fmt.Println("Checking Java backend health...")
        
        ctx, cancel := context.WithTimeout(context.Background(), time.Second*3)
        resp, err := healthClient.Check(ctx, &healthpb.HealthCheckRequest{})
        cancel()

        if err == nil && resp.GetStatus() == healthpb.HealthCheckResponse_SERVING {
            fmt.Println("✅ Java backend is online and serving!")
            break
        }

        fmt.Println("Java backend unreachable. Retrying in 2 seconds...")
        time.Sleep(time.Second * 2)
    }

    return conn
}
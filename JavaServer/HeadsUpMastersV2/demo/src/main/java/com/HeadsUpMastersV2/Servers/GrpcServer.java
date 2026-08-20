package com.HeadsUpMastersV2.servers;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.protobuf.services.HealthStatusManager;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import com.HeadsUpMastersV2.grpc.TableController;
import io.grpc.health.v1.HealthCheckResponse.ServingStatus;


public class GrpcServer {
    private final int port;
    private final Server server;
    private final HealthStatusManager healthStatusManager;

    public GrpcServer(int port) {
        this.port = port;
        this.healthStatusManager = new HealthStatusManager();
        this.server = NettyServerBuilder.forPort(port)
                .addService(new TableController())
                .addService(this.healthStatusManager.getHealthService())
                .permitKeepAliveTime(5, TimeUnit.SECONDS)
                .permitKeepAliveWithoutCalls(true)
                .build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        healthStatusManager.setStatus("", ServingStatus.SERVING);
        System.out.println("Java gRPC Server running on port " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server...");
            if (server != null) {
                server.shutdown();
            }
        }));
        server.awaitTermination();
    }
    public void stop(){
        if (server != null) {
            server.shutdown();
        }
    }
}
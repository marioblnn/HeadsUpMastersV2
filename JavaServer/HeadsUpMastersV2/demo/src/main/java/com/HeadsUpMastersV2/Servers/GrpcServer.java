package com.HeadsUpMastersV2.Servers;

import com.HeadsUpMastersV2.Controllers.TableController;
import io.grpc.Server;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GrpcServer {
    private final int port;
    private final Server server;

    public GrpcServer(int port) {
        this.port = port;
        // Configure keepalive policies to prevent ENHANCE_YOUR_CALM / too_many_pings errors
        this.server = NettyServerBuilder.forPort(port)
                .addService(new TableController())
                // Allow clients to send pings as frequently as every 5 seconds
                .permitKeepAliveTime(5, TimeUnit.SECONDS)
                // Allow client pings even when no active RPC stream is open
                .permitKeepAliveWithoutCalls(true)
                .build();
    }

    public void start() throws IOException, InterruptedException {
        server.start();
        System.out.println("Java gRPC Server running on port " + port);

        // Clean shutdown when the app is terminated (CTRL+C / SIGTERM)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down gRPC server...");
            if (server != null) {
                server.shutdown();
            }
        }));

        // Block main thread so the server doesn't exit immediately
        server.awaitTermination();
    }
}
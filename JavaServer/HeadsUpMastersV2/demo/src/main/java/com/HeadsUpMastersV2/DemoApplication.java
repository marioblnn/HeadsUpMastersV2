package com.HeadsUpMastersV2;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.HeadsUpMastersV2.servers.GrpcServer;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
        int port = 9090;
        try {
            GrpcServer server = new GrpcServer(port);
            server.start();
        } catch (Exception e) {
            System.err.println("Failed to start gRPC server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

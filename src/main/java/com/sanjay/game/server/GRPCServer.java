package com.sanjay.game.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GRPCServer {

    public static void main(String[] args) throws InterruptedException, IOException {
        Server server = ServerBuilder.forPort(6565)
                .addService(new GameService())
                .build();

        server.start();
        server.awaitTermination();
    }
}

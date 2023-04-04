package com.sanjay.game.server;

import com.sanjay.game.Die;
import com.sanjay.game.GameServiceGrpc;
import com.sanjay.game.GameState;
import com.sanjay.game.Player;
import io.grpc.stub.StreamObserver;

public class GameService extends GameServiceGrpc.GameServiceImplBase {

    @Override
    public StreamObserver<Die> roll(StreamObserver<GameState> responseObserver) {

        Player client = Player.newBuilder().setName("client").setPosition(0).build();
        Player server = Player.newBuilder().setName("server").setPosition(0).build();

        return new DieStreamingRequest(responseObserver, client, server);
    }
}

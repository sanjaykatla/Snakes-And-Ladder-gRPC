package com.sanjay.game.server;

import com.sanjay.game.Die;
import com.sanjay.game.GameState;
import com.sanjay.game.Player;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.ThreadLocalRandom;

public class DieStreamingRequest implements StreamObserver<Die> {

    private StreamObserver<GameState> gameStateStreamObserver;

    private Player client;

    private Player server;

    public DieStreamingRequest(StreamObserver<GameState> gameStateStreamObserver, Player client, Player server) {
        this.gameStateStreamObserver = gameStateStreamObserver;
        this.client = client;
        this.server = server;
    }

    @Override
    public void onNext(Die die) {

        this.client = this.getNewPlayerPosition(client, die);
        if (this.client.getPosition() != 100) {
            int random = ThreadLocalRandom.current().nextInt(1, 7);
            Die serverDie = Die.newBuilder()
                    .setValue(random)
                    .build();
            this.server = this.getNewPlayerPosition(server, serverDie);
        }
        this.gameStateStreamObserver.onNext(this.getGameState());

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        this.gameStateStreamObserver.onCompleted();
    }


    private GameState getGameState() {
        return GameState.newBuilder()
                .addPlayer(this.client)
                .addPlayer(this.server)
                .build();
    }


    private Player getNewPlayerPosition(Player player, Die die) {
        int position = player.getPosition() + die.getValue();
        if (position <= 100) {
            player = player.toBuilder()
                    .setPosition(position)
                    .build();
        }
        return player;
    }
}

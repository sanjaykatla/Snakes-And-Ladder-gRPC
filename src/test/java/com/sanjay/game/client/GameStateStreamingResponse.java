package com.sanjay.game.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.sanjay.game.Die;
import com.sanjay.game.GameState;
import com.sanjay.game.Player;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class GameStateStreamingResponse implements StreamObserver<GameState> {

    private CountDownLatch latch;

    private StreamObserver<Die> dieStreamObserver;

    public GameStateStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    public void setDieStreamObserver(StreamObserver<Die> dieStreamObserver){
        this.dieStreamObserver = dieStreamObserver;
    }

    @Override
    public void onNext(GameState gameState) {

        List<Player> playerList = gameState.getPlayerList();
        playerList.forEach(p -> System.out.println(p.getName() +" -> " + p.getPosition()));
        boolean isGameOver = playerList.stream()
                .anyMatch(p -> p.getPosition() == 100);
        if(isGameOver){
            System.out.println("Game Over");
            this.dieStreamObserver.onCompleted();
        } else {
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
            this.roll();
        }
        System.out.println("------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {
        latch.countDown();
    }

    public void roll(){
        int dieValue = ThreadLocalRandom.current().nextInt(1, 7);
        Die die = Die.newBuilder().setValue(dieValue).build();
        dieStreamObserver.onNext(die);
    }
}

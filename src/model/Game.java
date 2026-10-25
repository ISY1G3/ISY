package model;

import model.player.Player;

public interface Game {
    void start();
    void finish();
    GameStatus getGameStatus();
    Player getCurrentPlayer();
    GameState getState();
    void play(Move move);
    boolean isFinished();
}

package model;

import model.player.Player;

public interface Game {
    Player getCurrentPlayer();
    GameState getState();
    void play(Move move);
    boolean isFinished();
}

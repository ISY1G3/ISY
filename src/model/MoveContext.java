package model;

public interface MoveContext {
    void setInputEnabled(boolean enabled);

    GameState getGameState();

    void submitMove(Move move);
}

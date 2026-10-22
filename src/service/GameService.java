package service;

import model.Game;
import model.GameState;
import model.Move;
import model.MoveContext;

public class GameService implements MoveContext {
    private Game currentGame;

    public void startTurn() {
        throw new UnsupportedOperationException("Starting a turn is not implemented yet.");
    }

    @Override
    public void submitMove(Move move) {
        throw new UnsupportedOperationException("Move submission is not implemented yet.");
    }

    @Override
    public void setInputEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Input control is not implemented yet.");
    }

    @Override
    public GameState getGameState() {
        throw new UnsupportedOperationException("Game state access is not implemented yet.");
    }
}

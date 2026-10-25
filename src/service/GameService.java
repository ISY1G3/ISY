package service;

import model.Game;
import model.GameState;
import model.Move;
import model.MoveContext;

public class GameService implements MoveContext {
    private Game currentGame;

    public void startGame() {
        // TODO: Create the selected game, assign currentGame, then start the first turn.
        throw new UnsupportedOperationException("Game creation is not implemented yet.");
    }

    public void startTurn() {
        requireCurrentGame();
        if (!currentGame.isFinished()) {
            currentGame.getCurrentPlayer().askForMove(this);
        }
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
        requireCurrentGame();
        return currentGame.getState();
    }

    private void requireCurrentGame() {
        if (currentGame == null) {
            throw new IllegalStateException("No game has been started.");
        }
    }
}

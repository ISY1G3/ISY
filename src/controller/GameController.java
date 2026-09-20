package controller;
class GameController {
    private GameState gameState;

    public GameController() {
        gameState = new GameState();
    }

    public void switchPlayer() {
        gameState.switchPlayer();
    }

    public int getCurrentPlayer() {
        return gameState.getCurrentPlayer();
    }
}
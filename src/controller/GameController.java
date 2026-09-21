package controller;

import model.GameState;
import model.enums.PlayerType;


import model.Player.Player;
import model.Player.LocalPlayer;
import model.Player.RemotePlayer;
import model.Player.AIPlayer;


public class GameController {

    private static GameController instance; // Singleton instance

    private GameState gameState;

    private GameController() {
        if(instance != null) {
            throw new IllegalStateException("GameController instance already exists!");
        }
        instance = this;
    }

    public static GameController getInstance() {
        if(instance == null) {
            instance = new GameController();
        }
        return instance;
    }

    private Player initPlayer(PlayerType playerType, char marker) {

        // Handle player type and instantiate
        switch (playerType) {
            case PLAYER_LOCAL_HUMAN:
                return new LocalPlayer(marker);
            case PLAYER_REMOTE_HUMAN:
                return new RemotePlayer(marker);
            case PLAYER_AI:
                return new AIPlayer(marker);
            default:
                throw new IllegalArgumentException("Unknown player type: " + playerType);
        }
    }

    public void initNewGame(PlayerType player1Type, PlayerType player2Type) {
        // Initialize the game state with the specified player types
        Player player1 = initPlayer(player1Type, 'X'); // Example marker for player 1
        Player player2 = initPlayer(player2Type, 'O'); // Example marker for player
        gameState = new GameState(player1, player2);
    }

    public void switchPlayer() {
        gameState.switchPlayer();
        //
        Player cplayer = this.gameState.getCurrentPlayer();
        if(cplayer instanceof LocalPlayer) {
            // We should enable the controls here, which should then call 
        }
        else{

        }
    }

    public int getCurrentPlayer() {
        return gameState.getCurrentPlayer();
    }
}
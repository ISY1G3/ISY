package service;

import model.Game;
import model.GameMode;
import model.GameStatus;

// deze GameSession bewaart data over 1 wedstrijd

public class GameSession {

    private final String gameName;              // naam van spel
    private final GameMode gameMode;            // voor nu spelen we PvP of PvAi

    private final String player1Name;           // naam speler 1
    private final String player2Name;           // naam speler 2

    private final Game game;                    // het game object met het bord en de regels
    private final boolean player1Ai;
    private final boolean player2Ai;

    //wanneer gameservice een nieuwe GameSession maakt wordt hier de goede gegevens uit meegegeven
    public GameSession(String gameName, GameMode gameMode, String player1Name, String player2Name, Game game, boolean player1Ai, boolean player2Ai){
        this.gameName = gameName;           //sla de meegegeven spelnaam op in het veld van deze sessie
        this.gameMode = gameMode;           //etc
        this.player1Name = player1Name;
        this.player2Name = player2Name;
        this.game = game;
        this.player1Ai = player1Ai;
        this.player2Ai = player2Ai;
    }

    //we willen deze gegevens ook kunnen opvragen daarvoor hebben we deze getters
    public String getGameName() {
        return gameName;
    }
    public GameMode getGameMode() {
        return gameMode;
    }

    public String getPlayer1Name() {
        return player1Name;
    }
    public String getPlayer2Name() {
        return player2Name;
    }

    public boolean isPlayer1Ai() {
        return player1Ai;
    }
    public boolean isPlayer2Ai() {
        return player2Ai;
    }

    public Game getGame() {
        return game;
    }

    // deze voegen we toe als get omdat we deze gamestatus al bewaart wordt door TicTacToeGame
    public GameStatus getStatus(){
        return game.getGameStatus();
    }
    // deze controlleert of de status van de game wel op actief staat.
    // zo kan je bijv voorkomen dat er een tweede game gestart wordt terwijl er al 1 bezig is
    public boolean isActive(){
        return getStatus() == GameStatus.ACTIVE;
    }
}

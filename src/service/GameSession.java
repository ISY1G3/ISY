package service;

import model.Game;
import model.GameMode;
import model.GameStatus;

import model.player.Player;
import model.player.AIPlayer;

import java.util.Objects;

// deze GameSession bewaart data over 1 wedstrijd
public class GameSession {

    private final String gameName;              // naam van spel
    private final GameMode gameMode;            // geeft aan of de wedstrijd lokaal PvP, lokaal PvAI of online wordt gespeeld

    private final Player player1;           //bewaart een speler object
    private final Player player2;

    private final Game game;                    // het game object met het bord en de regels

    //wanneer gameservice een nieuwe GameSession maakt wordt hier de goede gegevens uit meegegeven
    public GameSession(String gameName, GameMode gameMode, Player player1, Player player2, Game game){
        this.gameName = Objects.requireNonNull(gameName);
        this.gameMode = Objects.requireNonNull(gameMode);

        this.player1 = Objects.requireNonNull(player1);
        this.player2 = Objects.requireNonNull(player2);

        this.game = Objects.requireNonNull(game);

        if (player1 == player2) {
            throw new IllegalArgumentException("Een speler kan niet beide posities hebben");
        }
    }

    //we willen deze gegevens ook kunnen opvragen daarvoor hebben we deze getters
    public String getGameName() {
        return gameName;
    }
    public GameMode getGameMode() {
        return gameMode;
    }
    public Game getGame() {return game;}

    public String getPlayer1Name() {return player1.getName();}
    public String getPlayer2Name() {return player2.getName();}

    public Player getPlayer1() {return player1;}
    public Player getPlayer2() {return player2;}

    public boolean isPlayer1Ai() {return player1 instanceof AIPlayer;}
    public boolean isPlayer2Ai() {return player2 instanceof AIPlayer;}

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

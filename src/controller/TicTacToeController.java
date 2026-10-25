package controller;

import model.Board;
import model.player.Player;
import model.GameMode;
import model.TicTacToeGame;
import service.*;
import view.TicTacToeGameView;

public class TicTacToeController {

    private int scoreP1 = 0;
    private int scoreP2 = 0;
    private final GameService gameService = new GameService();
    private TicTacToeGameView ticTacToeGameView;
    private TicTacToeGame ticTacToeGame;
    private Runnable goToMenu;

    public void initView(TicTacToeGameView view) {
        this.ticTacToeGameView = view;
    }

    public void startGame(GameMode gameMode, char startingMarker) {
        if (ticTacToeGameView == null) {
            throw new IllegalStateException("View moet eerst gekoppeld worden");
        }
        // de GUI gebruikt X voor speler 1 en 0 voor speler 2
        int startingPlayer;
        if (startingMarker == Board.PLAYER_X_MARKER){startingPlayer = 1;}
        else if (startingMarker == Board.PLAYER_0_MARKER) {startingPlayer = 2;}
        else throw new IllegalArgumentException("geen geldige beginspeler");

        //Wanneer je teruggaat naar het menu kan het zijn dat de vorige wedstijd te vroeg gestopt is
        closeOldSession();
        gameService.startTicTacToe(gameMode, "Speler 1",
                gameMode == GameMode.PLAYER_VS_AI? "Computer" : "Speler 2",
                startingPlayer);
        refreshBoardAndTurn();          //toont ookk een openingszet van de ai.
    }

    public TicTacToeGame getGame() {
        GameSession session = gameService.getCurrentSession();
        if (session == null || !(session.getGame() instanceof TicTacToeGame game)) {
            throw new IllegalStateException("Er is nog geen TTT-game gestart");
        }
        return game;
    }

    public void handleInputFromUser(int boxNumber) {
        try {
            if (!gameService.makeMove(boxNumber)) {
                ticTacToeGameView.showMessage("dit vakje is al bezet of is niet geldig");
                return;
            }
        }
        catch (IllegalStateException e) {
            ticTacToeGameView.showMessage(e.getMessage());
            return;
        }

        // hier wordt het bord uit de bestaande game afgelezen
        refreshBoardAndTurn();
        if (!gameService.getCurrentSession().isActive()) {
            showGameResult();
        }
    }

    private void refreshBoardAndTurn() {
        TicTacToeGame game = getGame();
        char[][] board = game.getBoard();
        for(int row = 0; row < Board.SIZE; row++){
            for(int col = 0; col < Board.SIZE; col++){
                char marker = board[row][col];
                if (marker != '\0') {
                    ticTacToeGameView.setMarker(row * Board.SIZE + col, marker);
                }
            }
        }
        if (game.isActive()) {
            Player currentPlayer = game.getCurrentPlayer();
            ticTacToeGameView.showMessage(
                    "Player " + game.getMarker(currentPlayer) + " turn");
        }
    }

    //zorgt voor het goede resultaat op het scherm bij winst verlies of draw
    private void showGameResult() {
        TicTacToeGame game = getGame();
        if (game.playerXWon()) {
            scoreP1++;
            ticTacToeGameView.showGameResult("Player X Won!");
        }
        else if (game.player0Won()) {
            scoreP2++;
            ticTacToeGameView.showGameResult("Player 0 Won!");
        }
        else {
            ticTacToeGameView.showGameResult("Game Drawn!");
        }
    }

    public int getScoreP1(int x) { //functie voor het bewaren van scorep1 zodat ik die in Tictactoeview kan aanroepen
        if (x != 0) // kijken of we een score krijgen van storep1 uit Tictactoegameapp
            scoreP1 = x;
        return scoreP1;
    }

    public int getScoreP2(int x) { //functie voor het bewaren van scorep2 zodat ik die in Tictactoeview kan aanroepen
        if (x != 0)// kijken of we een score krijgen van storep1 uit Tictactoegameapp
            scoreP2 = x;
        return scoreP2;
    }

    public void initGoToMenu(Runnable goToMenu) { // goToMenu is eerst niks maar als
    //  TicTacToeController.initGoToMenu(() -> showStartScreen(stage)); wordt aangeroepen dan wordt door this.goToMenu = goToMenu
    // goToMenu gelijk aan showstartscreen
    this.goToMenu = goToMenu;
    }

    private void closeOldSession() {
        if (gameService.getCurrentSession() != null) {
            gameService.finishCurrentSession();
            gameService.clearCurrentSession();
        }
    }

    public void resetGame() {
        goToMenu.run();
    }
}


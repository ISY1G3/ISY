package controller;

import model.TicTacToeGame;
import view.TicTacToeGameView;
import ai.AiPlayer;
import model.Board;
import model.GameMode;
import model.player.Player;

public class TicTacToeController {

    private int scoreP1 = 0;
    private int scoreP2 = 0;
    //Model
    private TicTacToeGame ticTacToeGame;

    //View
    private TicTacToeGameView ticTacToeGameView;

    private GameMode gameMode;

    private AiPlayer aiPlayer;

    public void initView(TicTacToeGameView ticTacToeGameView) {
        this.ticTacToeGameView = ticTacToeGameView;
    }

    public void startGame(GameMode gameMode) {
        if (ticTacToeGameView == null) {
            throw new IllegalStateException("View moet eerst gekoppeld worden");
        }

        // Nieuwe wedstrijd aanmaken
        ticTacToeGame = new TicTacToeGame(gameMode);

        // Gekozen spelmodus bewaren
        this.gameMode = gameMode;

        // Eventueel AI aan speler 0 koppelen
        if (gameMode == GameMode.PLAYER_VS_AI) {
            aiPlayer = new AiPlayer(Board.PLAYER_0_MARKER);
        } else {
            aiPlayer = null;
        }

        // Status actief maken en eerste speler bepalen
        ticTacToeGame.start();

        // Eerste beurt starten
        ticTacToeGameView.showMessage(
                "Player "
                        + ticTacToeGame.getMarker(ticTacToeGame.getCurrentPlayer())
                        + " turn"
        );
    }

    public TicTacToeGame getGame() {
        if (ticTacToeGame == null) {
            throw new IllegalStateException("Er is nog geen game gestart");
        }

        return ticTacToeGame;
    }

    public void handleInputFromUser(int boxNumber) {

        Player currentPlayer = ticTacToeGame.getCurrentPlayer();

        // zet van gewone speler
        ticTacToeGame.placeMarker(boxNumber);

        ticTacToeGameView.setMarker(
                boxNumber,
                ticTacToeGame.getMarker(currentPlayer)
        );

        // Heeft X gewonnen of is er gelijkspel?
        if (showGameResultIfNeeded()) {
            return;
        }

        // wissel naar volgende speler
        ticTacToeGame.changeTurn();

        ticTacToeGameView.showMessage(
                "Player "
                        + ticTacToeGame.getMarker(ticTacToeGame.getCurrentPlayer())
                        + " turn"
        );

        // Als we tegen AI spelen en speler 0 (de AI) is aan de beurt dan vragen we de AI een move te doen
        if (gameMode == GameMode.PLAYER_VS_AI
                && ticTacToeGame.getMarker(ticTacToeGame.getCurrentPlayer())
                == Board.PLAYER_0_MARKER) {

            doAiMove();
        }
    }

    private void doAiMove() {

        int aiMove = aiPlayer.chooseMove(ticTacToeGame);
        //checkt of de return waarde van de AI -1 is oftewel fout is
        if (aiMove == -1) {
            return;
        }

        // AI zet plaatsen op bord
        ticTacToeGame.placeMarker(aiMove);

        // AI zet op scherm laten zien
        ticTacToeGameView.setMarker(
                aiMove,
                ticTacToeGame.getMarker(ticTacToeGame.getCurrentPlayer())
        );

        // Heeft AI gewonnen / gelijkspel?
        if (showGameResultIfNeeded()) {
            return;
        }

        // Beurt terug naar X oftewel de gewone speler
        ticTacToeGame.changeTurn();

        ticTacToeGameView.showMessage(
                "Player "
                        + ticTacToeGame.getMarker(ticTacToeGame.getCurrentPlayer())
                        + " turn"
        );
    }

    //zorgt voor het goede resultaat op het scherm bij winst verlies of draw
    private boolean showGameResultIfNeeded() {
        

        if (!ticTacToeGame.isGameOver()) {
            return false;
        }

        ticTacToeGame.finish();

        if (ticTacToeGame.playerXWon()) {
            scoreP1++;
            ticTacToeGameView.showGameResult("Player X Won!");
            
        }
        else if (ticTacToeGame.player0Won()) {
            scoreP2++;
            ticTacToeGameView.showGameResult("Player 0 Won!");
            
        }
        else {
            ticTacToeGameView.showGameResult("Game Drawn!");
        }

        return true;
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


private Runnable goToMenu;

public void initGoToMenu(Runnable goToMenu) { // goToMenu is eerst niks maar als
//  TicTacToeController.initGoToMenu(() -> showStartScreen(stage)); wordt aangeroepen dan wordt door this.goToMenu = goToMenu 
// goToMenu gelijk aan showstartscreen
    this.goToMenu = goToMenu;
   

}

public void resetGame() {
    goToMenu.run();
}

}


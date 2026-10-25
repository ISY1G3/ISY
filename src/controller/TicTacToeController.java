package controller;

import model.TicTacToeGame;
import view.TicTacToeGameView;
import ai.AiPlayer;
import javafx.stage.Stage;
import model.Board;
import model.GameMode;



public class TicTacToeController {

    // Door Copilot aangepast: maakt de view-API ook bruikbaar voor de FXML-view.
    public interface GameView {
        void showMessage(String message);

        void setMarker(int boxNumber, char marker);

        void showGameResult(String gameResult);
    }

    private int scoreP1 = 0;
    private int scoreP2 = 0;
    //Model
    private TicTacToeGame ticTacToeGame;

    //View
    private GameView ticTacToeGameView;

    private GameMode gameMode;

    private AiPlayer aiPlayer;

    private char aiMarker = Board.PLAYER_0_MARKER;
    private String playerXName = "Player X";
    private String player0Name = "Player 0";

    public void initView(GameView ticTacToeGameView) {
        this.ticTacToeGameView = ticTacToeGameView;
    }

    public void startGame(GameMode gameMode) {
        startGame(gameMode, Board.PLAYER_X_MARKER, Board.PLAYER_0_MARKER,
                "Player X", "Player 0");
    }

    public void startGame(GameMode gameMode, char startingMarker, char aiMarker,
                          String playerXName, String player0Name) {
        if (ticTacToeGameView == null) {
            throw new IllegalStateException("View moet eerst gekoppeld worden");
        }

        // Nieuwe wedstrijd aanmaken
        ticTacToeGame = new TicTacToeGame();

        // Gekozen spelmodus bewaren
        this.gameMode = gameMode;
        this.aiMarker = aiMarker;
        this.playerXName = playerXName;
        this.player0Name = player0Name;

        // Eventueel AI aan de gekozen speler koppelen
        if (gameMode == GameMode.PLAYER_VS_AI) {
            aiPlayer = new AiPlayer(aiMarker);
        } else {
            aiPlayer = null;
        }

        // Status actief maken en eerste speler bepalen
        ticTacToeGame.start();
        if (startingMarker == Board.PLAYER_0_MARKER) {
            ticTacToeGame.changeTurn();
        }

        // Eerste beurt starten
        showCurrentTurn();
        if (isAiTurn()) {
            doAiMove();
        }
    }

    public TicTacToeGame getGame() {
        if (ticTacToeGame == null) {
            throw new IllegalStateException("Er is nog geen game gestart");
        }

        return ticTacToeGame;
    }

    public void handleInputFromUser(int boxNumber) {
        if (ticTacToeGame == null || !ticTacToeGame.isActive() || isAiTurn()
                || !ticTacToeGame.getValidMoves().contains(boxNumber)) {
            return;
        }

        // zet van gewone speler
        ticTacToeGame.placeMarker(boxNumber);

        ticTacToeGameView.setMarker(
                boxNumber,
                ticTacToeGame.getCurrentPlayer().getMarker()
        );

        // Heeft X gewonnen of is er gelijkspel?
        if (showGameResultIfNeeded()) {
            return;
        }

        // wissel naar volgende speler
        ticTacToeGame.changeTurn();

        showCurrentTurn();

        if (isAiTurn()) {
            doAiMove();
        }
    }

    private boolean isAiTurn() {
        return gameMode == GameMode.PLAYER_VS_AI
                && ticTacToeGame.getCurrentPlayer().getMarker() == aiMarker;
    }

    private void showCurrentTurn() {
        char marker = ticTacToeGame.getCurrentPlayer().getMarker();
        String playerName = marker == Board.PLAYER_X_MARKER ? playerXName : player0Name;
        ticTacToeGameView.showMessage( "turn: "+ playerName + " (" + marker + ")");
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
                ticTacToeGame.getCurrentPlayer().getMarker()
        );

        // Heeft AI gewonnen / gelijkspel?
        if (showGameResultIfNeeded()) {
            return;
        }

        // Beurt terug naar X oftewel de gewone speler
        ticTacToeGame.changeTurn();

        showCurrentTurn();
    }

    //zorgt voor het goede resultaat op het scherm bij winst verlies of draw
    private boolean showGameResultIfNeeded() {
        

        if (!ticTacToeGame.isGameOver()) {
            return false;
        }

        ticTacToeGame.finish();

        if (ticTacToeGame.playerXWon()) {
            scoreP1++;
            ticTacToeGameView.showGameResult(playerXName + " (X) won!");
            
        }
        else if (ticTacToeGame.player0Won()) {
            scoreP2++;
            ticTacToeGameView.showGameResult(player0Name + " (0) won!");
            
        }
        else {
            ticTacToeGameView.showGameResult("Draw!");
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

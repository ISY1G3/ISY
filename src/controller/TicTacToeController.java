package controller;

import model.TicTacToeGame;
import view.TicTacToeGameView;
import ai.AiPlayer;
import model.Board;
import model.GameMode;

public class TicTacToeController {
    private int scoreP1 = 0;
    private int scoreP2 = 0;
    //Model
    private TicTacToeGame ticTacToeGame;

    //View
    private TicTacToeGameView ticTacToeGameView;

    private GameMode gameMode;

    private AiPlayer aiPlayer;

    public void initModel(TicTacToeGame ticTacToeGame) {
        this.ticTacToeGame = ticTacToeGame;
    }

    public void initView(TicTacToeGameView ticTacToeGameView) {
        this.ticTacToeGameView = ticTacToeGameView;
    }

    public void initGameMode(GameMode gameMode) {
        this.gameMode = gameMode;

        // Speler 0 is de AI zoals we hadden afgesproken
        if (gameMode == GameMode.PLAYER_VS_AI) {
            aiPlayer = new AiPlayer(Board.PLAYER_0_MARKER);
        }
    }

    public void handleInputFromUser(int boxNumber) {

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

        ticTacToeGameView.showMessage(
                "Player "
                        + ticTacToeGame.getCurrentPlayer().getMarker()
                        + " turn"
        );

        // Als we tegen AI spelen en speler 0 (de AI) is aan de beurt dan vragen we de AI een move te doen
        if (gameMode == GameMode.PLAYER_VS_AI
                && ticTacToeGame.getCurrentPlayer().getMarker()
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
                ticTacToeGame.getCurrentPlayer().getMarker()
        );

        // Heeft AI gewonnen / gelijkspel?
        if (showGameResultIfNeeded()) {
            return;
        }

        // Beurt terug naar X oftewel de gewone speler
        ticTacToeGame.changeTurn();

        ticTacToeGameView.showMessage(
                "Player "
                        + ticTacToeGame.getCurrentPlayer().getMarker()
                        + " turn"
        );
    }

    //zorgt voor het goede resultaat op het scherm bij winst verlies of draw
    private boolean showGameResultIfNeeded() {
        

        if (!ticTacToeGame.isGameOver()) {
            return false;
        }

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
    public int getScoreP1() { //functie voor het bewaren van scorep1 zodat ik die in Tictactoeview kan aanroepen
        return scoreP1;
    }

    public int getScoreP2() { //functie voor het bewaren van scorep2 zodat ik die in Tictactoeview kan aanroepen
        return scoreP2;
    }


    public void resetGame() {
        ticTacToeGame = new TicTacToeGame();
        ticTacToeGameView.resetBoard();
    
    }
}


package controller;

import model.TicTacToeGame;
import view.TicTacToeGameView;

public class TicTacToeController {
    //Model
    private TicTacToeGame ticTacToeGame;

    //View
    private TicTacToeGameView ticTacToeGameView;

    public void initModel(TicTacToeGame ticTacToeGame) {
        this.ticTacToeGame = ticTacToeGame;
    }

    public void initView(TicTacToeGameView ticTacToeGameView) {
        this.ticTacToeGameView = ticTacToeGameView;
    }

    public void handleInputFromUser(int boxNumber) {
        ticTacToeGame.placeMarker(boxNumber); //Zet X of 0 in het model

        ticTacToeGameView.setMarker(boxNumber, ticTacToeGame.getCurrentPlayer().getMarker()); //Toont de teken op geklikte vakje

        if (ticTacToeGame.isGameOver()) { //Controlleerd of het spel beindigt is
            if (ticTacToeGame.playerXWon()) {
                ticTacToeGameView.showGameResult("Player X Won!");
            }
            else if (ticTacToeGame.player0Won()) {
                ticTacToeGameView.showGameResult("Player 0 Won!");
            }
            else {
                ticTacToeGameView.showGameResult("Game Drawn!"); //showGameResult toont en popup met result
            }
        }
        else {
            ticTacToeGame.changeTurn(); //Als spel niet beindigt is, wordt de volgende speler aangeroepen.
            // showMessage past de text aan om te laten zien wie aan de beurt is.
            ticTacToeGameView.showMessage("Player " + ticTacToeGame.getCurrentPlayer().getMarker() + " turn"); 
        }
    }
}


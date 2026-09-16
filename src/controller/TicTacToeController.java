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
        ticTacToeGame.placeMarker(boxNumber);

        ticTacToeGameView.setMarker(boxNumber, ticTacToeGame.getCurrentPlayer().getMarker());

        if (ticTacToeGame.isGameOver()) {
            if (ticTacToeGame.playerXWon()) {
                ticTacToeGameView.showGameResult("Player X Won!");
            }
            else if (ticTacToeGame.player0Won()) {
                ticTacToeGameView.showGameResult("Player 0 Won!");
            }
            else {
                ticTacToeGameView.showGameResult("Game Drawn!");
            }
        }
        else {
            ticTacToeGame.changeTurn();
            ticTacToeGameView.showMessage("Player " + ticTacToeGame.getCurrentPlayer().getMarker() + " turn");
        }
    }
}


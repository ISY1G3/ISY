package model.player;

import model.TicTacToeGame;

public class AIPlayer extends Player implements PlayerInterface {
    public AIPlayer(String name) {
        super(name);
    }
    public void askForMove(TicTacToeGame game) {
        // todo: plumb the existing AI functionality into here.
        super.askForMove(game);
    }
}

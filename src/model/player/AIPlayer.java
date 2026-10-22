package model.player;

import model.TicTacToeGame;

public class AIPlayer extends Player implements PlayerInterface {
    public AIPlayer(String name) {
        super(name);
    }
    @Override
    public void askForMove(TicTacToeGame game) {
        // todo: plumb the existing AI functionality into here.
        throw new UnsupportedOperationException("AI move requests are not implemented yet.");
    }
}

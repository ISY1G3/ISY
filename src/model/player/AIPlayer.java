package model.player;

import model.TicTacToeGame;

public class AIPlayer extends Player implements PlayerInterface {
    public AIPlayer(String name, char symbol) {
        super(name, symbol);
    }
    public void chooseMove(TicTacToeGame game) {
        // todo: plumb the existing AI functionality into here.
        super.chooseMove(game);
    }
}

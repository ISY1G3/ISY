package model.player;

import model.TicTacToeGame;
// This interface should be implemented by each Player subclass.
public interface PlayerInterface {
    public void chooseMove(TicTacToeGame game);
}

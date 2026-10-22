package model.player;

import model.TicTacToeGame;
import model.MoveContext;

// This interface should be implemented by each Player subclass.
public interface PlayerInterface {
    public void askForMove(MoveContext context);
}

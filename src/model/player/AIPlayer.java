package model.player;

import model.MoveContext;

public class AIPlayer extends Player {
    public AIPlayer(String name) {
        super(name);
    }
    @Override
    public void askForMove(MoveContext context) {
        // todo: plumb the existing AI functionality into here.
        throw new UnsupportedOperationException("AI move requests are not implemented yet.");
    }
}

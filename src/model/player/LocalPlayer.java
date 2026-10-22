package model.player;

import model.TicTacToeGame;

public class LocalPlayer extends Player implements PlayerInterface {
    public LocalPlayer(String name) {
        super(name);
    }

    @Override
    public void askForMove(TicTacToeGame game) {
        // TODO: Enable local input when this player is asked for a move.
        // Implementation should be as follows:
        // GameService.getGameSession.UnlockBoardForLocalPlayer(this)
        throw new UnsupportedOperationException("Local move requests are not implemented yet.");
    }
}

package model.player;

import model.TicTacToeGame;

public class LocalPlayer extends Player implements PlayerInterface {
    public LocalPlayer(String name, char symbol) {
        super(name, symbol);
    }

    @Override
    public void chooseMove(TicTacToeGame game) {
        // TODO: This needs a proper implementation later. Right now we call the stub on the superclass which throws an error.
        // Implementation should be as follows:
        // GameService.getGameSession.UnlockBoardForLocalPlayer(this)
        super.chooseMove(game);
    }
}

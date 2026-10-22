package model.player;

import model.TicTacToeGame;

public class RemotePlayer extends Player implements PlayerInterface {
    public RemotePlayer(String name) {
        super(name);
    }
    public void chooseMove(TicTacToeGame game) {
        // TODO: Stub. plumb this to functionality to check for a networked move from the server.
        super.chooseMove(game);
    }
}

package model.player;

import model.TicTacToeGame;

abstract public class Player {
    private String name;
    public Player(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public void chooseMove(TicTacToeGame game) {
        // This needs to be implemented in subclasses, throw error.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

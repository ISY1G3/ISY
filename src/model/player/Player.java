package model.player;

import model.TicTacToeGame;

abstract public class Player {
    private String name;
    private char symbol;
    public Player(String name, char symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    public String getName() {
        return name;
    }
    public char getSymbol() {
        return symbol;
    }
    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }
    public void setName(String name){
        this.name = name;
    }
    public void chooseMove(TicTacToeGame game) {
        // This needs to be implemented in subclasses, throw error.
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

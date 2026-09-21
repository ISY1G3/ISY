package model;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int SIZE = 3; //Bord is 3x3
    public static  final char PLAYER_X_MARKER = 'X'; //Teken van speler "X"
    public static  final char PLAYER_0_MARKER = '0'; //Teken van speler "0"

    private char board[][];

    public Board() { 
        board = new char[SIZE][SIZE]; //board is een 2d array waarin de vakjes worden opgeslagen
    }

    public void print() { //Print bord in terminal voor testing, to disable ga naar TicTacToeGame.java en verwijder "Board.print()".
        for(int i = 0; i<SIZE; i++) {
            for(int j = 0; j<SIZE; j++) {
                System.out.printf(" %s ", "" + board[i][j]);

                if (j+1<SIZE) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i+1<SIZE) {
                System.out.println("----------");
            }
        }
    }

    public boolean isBoardFull() { //Kijkt of alle 9 vakjes bezet zijn.
        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getBoard() { //get voor encapsulation (zie README.md)
        char[][] boardCopy = new char[SIZE][SIZE];

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public boolean isValidBoxNumber(int boxNumber) { //Controleert of een gekozen nummer tussen 0 en 8 ligt
        return boxNumber >= 0 && boxNumber < (SIZE*SIZE);
    }

    public boolean isBoxNumberEmpty(int boxNumber) { //Controleert of een vakje vrij is
        int i = boxNumber/SIZE;
        int j = boxNumber%SIZE;
        if (board[i][j] == PLAYER_X_MARKER || board[i][j] == PLAYER_0_MARKER) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean placeMarker(int boxNumber, char marker) { //Zet "X" of "0" in een vrij vakje.
        if (!isBoxNumberEmpty(boxNumber)) {
            return false;
        }
        int i = boxNumber/SIZE;
        int j = boxNumber%SIZE;
        board[i][j] = marker;
        return true;
    }
    public List<Integer> getValidMoves() { //Geeft een lijst van alle vrije vakjes
        List<Integer> validMoves = new ArrayList<>();
        for (int i = 0; i < SIZE * SIZE; i++) {
            if (isBoxNumberEmpty(i)) {
                validMoves.add(i);
            }
        }
        return validMoves;
    }
}

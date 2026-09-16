package model;
public class Board {

    public static final int SIZE = 3;
    public static  final char PLAYER_X_MARKER = 'X';
    public static  final char PLAYER_0_MARKER = '0';

    private char board[][];

    public Board() {
        board = new char[SIZE][SIZE];
    }

    public void print() {
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

    public boolean isBoardFull() {
        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getBoard() {
        char[][] boardCopy = new char[SIZE][SIZE];

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public boolean isValidBoxNumber(int boxNumber) {
        return boxNumber >= 0 && boxNumber < (SIZE*SIZE);
    }

    public boolean isBoxNumberEmpty(int boxNumber) {
        int i = boxNumber/SIZE;
        int j = boxNumber%SIZE;
        if (board[i][j] == PLAYER_X_MARKER || board[i][j] == PLAYER_0_MARKER) {
            return false;
        }
        else {
            return true;
        }
    }

    public boolean placeMarker(int boxNumber, char marker) {
        if (!isBoxNumberEmpty(boxNumber)) {
            return false;
        }
        int i = boxNumber/SIZE;
        int j = boxNumber%SIZE;
        board[i][j] = marker;
        return true;
    }
}

package ui.tui.view;

import model.Board;

//print een situatie van het board op een bepaald moment. bewaart zelf geen bord of spelregels
public class TicTacToeBoardView {
    public void draw(char[][] board) {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                char marker = board[row][col];
                String cell = marker == Board.EMPTY_MARKER
                        ? String.valueOf(row * Board.SIZE + col + 1)
                        : String.valueOf(marker);
                System.out.print(" " + cell + " ");
                if (col < Board.SIZE - 1) System.out.print("|");
            }
            System.out.println();
            if (row < Board.SIZE - 1) System.out.println("---+---+---");
        }
    }
}


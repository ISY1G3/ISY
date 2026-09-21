package model;

import java.util.ArrayList;
import java.util.List;
import model.Player.LocalPlayer;

public class TicTacToeGame {
    //Board
    private Board board; //Bewaart het 3x3 bord

    //Players
    private LocalPlayer playerX; //Speler X
    private LocalPlayer player0; //Speler 0

    //Current Player
    private LocalPlayer currentPlayer; //Bewaart wie aan de beurt is.

    public TicTacToeGame() { //Maakt nieuwe bord, de twee spelers en laat X beginnen
        board = new Board(); 

        //Create Players
        playerX = new LocalPlayer(Board.PLAYER_X_MARKER);
        player0 = new LocalPlayer(Board.PLAYER_0_MARKER);

        //Player X eerst
        currentPlayer = playerX;
    }

    public boolean checkWinner(LocalPlayer player) { //Controleerd horizontaal, verticaal, diagonaal of iemand gewonnen heeft.
        char playerMarker = player.getMarker();
        char board[][] = this.board.getBoard();

        //check horizontaal
        for (int i=0; i<Board.SIZE; i++) {
            boolean horizontalWin = true;
            for(int j=0; j<Board.SIZE; j++) {
                if (board[i][j]!=playerMarker) {
                    horizontalWin = false;
                    break;
                }
            }
            if (horizontalWin) {
                return true;
            }
        }

        //check verticaal
        for (int j=0; j<Board.SIZE; j++) {
            boolean verticalWin = true;
            for(int i=0; i<Board.SIZE; i++) {
                if (board[i][j]!=playerMarker) {
                    verticalWin = false;
                    break;
                }
            }
            if (verticalWin) {
                return true;
            }
        }

        //diagonaal check links
        boolean leftDiagonalWin = true;
        for (int i=0; i<Board.SIZE; i++) {
            if (board[i][i]!=playerMarker) {
                leftDiagonalWin = false;
                break;
            }
        }
        if (leftDiagonalWin) {
            return true;
        }

        //diagonaal check rechts
        boolean rightDiagonalWin = true;
        for (int i=0; i<Board.SIZE; i++) {
            int j = Board.SIZE - i - 1;
            if (board[i][j]!=playerMarker) {
                rightDiagonalWin = false;
                break;
            }
        }
        if (rightDiagonalWin) {
                return true;
        }
        return false;
    }

    public boolean playerXWon() {
        return checkWinner(playerX); //checkWinner wordt gebruikt voor de juiste speler
    }

    public boolean player0Won() {
        return checkWinner(player0);
    }

    public boolean isGameOver() { //true als iemand wint of het bord vol is.
        return checkWinner(playerX) || checkWinner(player0) || board.isBoardFull();
    }

    public void changeTurn() { //Wisselt de beurt
        currentPlayer = currentPlayer == playerX? player0 : playerX;
    }

    public void placeMarker(int boxNumber) { //Zet het teken van de huidige speler op het gekozen vakje.
        board.placeMarker(boxNumber, currentPlayer.getMarker());
        board.print(); //Print bord in terminal voor controle, kan worden verwijderd.
    }

    public LocalPlayer getCurrentPlayer() { //Geeft speler terug die aan de beurt is, zodat controller weet welke teken het moet zetten.
        return currentPlayer;
    }

    // voor de ai om alle moves te krijgen die nog mogelijk zijn
    public List<Integer> getValidMoves() {
        List<Integer> validMoves = new ArrayList<>();
        for (int boxNumber = 0; boxNumber < Board.SIZE * Board.SIZE; boxNumber++) {
            if (board.isBoxNumberEmpty(boxNumber)) {
                validMoves.add(boxNumber);
            }
        }
        return validMoves;
    }
}
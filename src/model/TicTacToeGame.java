package model;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame {
    //Board
    private Board board; //Bewaart het 3x3 bord

    //Players
    private Player playerX; //Speler X
    private Player player0; //Speler 0

    //Current Player
    private Player currentPlayer; //Bewaart wie aan de beurt is.

    public TicTacToeGame() { //Maakt nieuwe bord, de twee spelers en laat X beginnen
        board = new Board(); 

        //Create Players
        playerX = new Player(Board.PLAYER_X_MARKER);
        player0 = new Player(Board.PLAYER_0_MARKER);

        //Player X eerst
        currentPlayer = playerX;
    }

    // Ik wilde niet opnieuw alle win condities maken
    // dus nu laat ik de AI via dit stukje de game vragen of iets een winnende positie is
    // hierdoor is dit stuk code nu herbruikbaar. en niet alleen maar te gebruiken op het originele board.
    public boolean checkWinner(Player player) {
        return checkWinner(
                board.getBoard(),
                player.getMarker()
        );
    }

    public boolean checkWinner(char[][] board, char playerMarker) { //Controleerd horizontaal, verticaal, diagonaal of iemand gewonnen heeft.

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

    public Player getCurrentPlayer() { //Geeft speler terug die aan de beurt is, zodat controller weet welke teken het moet zetten.
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
    // dit wordt gebruikt door de AI om het board te vragen van model.Board
    public char[][] getBoard() {
        return board.getBoard();
    }
}
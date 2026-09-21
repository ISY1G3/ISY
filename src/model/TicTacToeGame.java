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

    //Game Status
    private GameStatus gameStatus;

    public TicTacToeGame() { //Maakt nieuwe bord, de twee spelers en laat X beginnen
        board = new Board(); 

        //Create Players
        playerX = new Player(Board.PLAYER_X_MARKER);
        player0 = new Player(Board.PLAYER_0_MARKER);

        //Player X eerst
        currentPlayer = playerX;


        gameStatus = GameStatus.NOT_STARTED;
    }

    public void start() {
        if (gameStatus != GameStatus.NOT_STARTED) {
            throw new IllegalStateException("Game is al gestart");
        }

        // X is volgens de huidige spelregel de eerste speler.
        currentPlayer = playerX;
        gameStatus = GameStatus.ACTIVE;
    }

    public void finish() {
        gameStatus = GameStatus.FINISHED;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public boolean isActive() {
        return gameStatus == GameStatus.ACTIVE;
    }

    public List<Player> getPlayers() {
        return List.of(playerX, player0);
    }


    public boolean checkWinner(Player player) { //Controleerd horizontaal, verticaal, diagonaal of iemand gewonnen heeft.
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
}
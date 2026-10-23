package model;

import java.util.ArrayList;
import java.util.List;

public class TicTacToeGame implements Game{
    //Board
    private Board board; //Bewaart het 3x3 bord

    //Players
    private Player playerX; //Speler X
    private Player player0; //Speler 0

    //Current Player
    private Player currentPlayer; //Bewaart wie aan de beurt is.

    //starting player
    private Player startingPlayer; //bewaart welke speler moet beginnen

    //Game Status
    private GameStatus gameStatus;

    public TicTacToeGame() { //Maakt nieuwe bord, de twee spelers en laat X beginnen
        board = new Board(); 

        //Create Players
        playerX = new Player(Board.PLAYER_X_MARKER);
        player0 = new Player(Board.PLAYER_0_MARKER);

        //Player X eerst
        startingPlayer = playerX;
        currentPlayer = playerX;

        gameStatus = GameStatus.NOT_STARTED;
    }

    @Override
    public void start() {
        if (gameStatus != GameStatus.NOT_STARTED) {
            throw new IllegalStateException("Game is al gestart");
        }

        // Begin met de gekozen speler ipv altijd X
        currentPlayer = startingPlayer;

        gameStatus = GameStatus.ACTIVE;
    }

    @Override
    public void finish() {
        gameStatus = GameStatus.FINISHED;
    }

    @Override
    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public boolean isActive() {
        return gameStatus == GameStatus.ACTIVE;
    }

    public List<Player> getPlayers() {
        return List.of(playerX, player0);
    }

    public boolean checkWinner(Player player) {
        return checkWinner(board.getBoard(), player.getMarker());
    }

    /** Checks whether the given marker wins on the supplied board state. */
    public boolean checkWinner(char[][] board, char playerMarker) {

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

    // alleen het model verandert het bord en de UI bepaalt hoe het wordt getoond
    // een ongeldige of al bezette positie verandert niks
    public boolean placeMarker(int boxNumber) { //Zet het teken van de huidige speler op het gekozen vakje.
        if (!isActive()){
            throw new IllegalStateException("er is geen game actief");
        }
        return board.placeMarker(boxNumber, currentPlayer.getMarker());
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

    // kijkt welk symbool is meegegeven en maakt 1 van de 2 spelers de beginnende speler gebaseert op wat is meegegeven
    // (standaard playerX)
    public void setStartingPlayer(char marker){
        //beginnende speler mag alleen maar veranderen VOOR de start.
        if (gameStatus != GameStatus.NOT_STARTED){
            throw new IllegalStateException("game is al gestart");
        }

        //bepaal welke speler bij welke marker(X 0) hoort
        if (marker == Board.PLAYER_X_MARKER) {
            startingPlayer = playerX;
        }
        else if (marker == Board.PLAYER_0_MARKER){
            startingPlayer = player0;
        }
        else{
            throw new IllegalArgumentException("Geen geldige speler marker");
        }
    }
}

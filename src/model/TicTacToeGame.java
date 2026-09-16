package model;

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

        //Player X goes first
        currentPlayer = playerX;
    }

    public boolean checkWinner(Player player) { //Controleerd horizontaal, verticaal, diagonaal of iemand gewonnen heeft.
        char playerMarker = player.getMarker();
        char board[][] = this.board.getBoard();

        //Hortizontal Check
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

        //Vertical Check
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

        //Left Diagonal Check
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

        //Right Diagonal Check
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
}

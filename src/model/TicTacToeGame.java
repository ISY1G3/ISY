package model;

public class TicTacToeGame {
    //Board
    private Board board;

    //Players
    private Player playerX;
    private Player player0;

    //Current Player
    private Player currentPlayer;

    public TicTacToeGame() {
        board = new Board();

        //Create Players
        playerX = new Player(Board.PLAYER_X_MARKER);
        player0 = new Player(Board.PLAYER_0_MARKER);

        //Player X goes first
        currentPlayer = playerX;
    }

    public boolean checkWinner(Player player) {
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
        return checkWinner(playerX);
    }

    public boolean player0Won() {
        return checkWinner(player0);
    }

    public boolean isGameOver() {
        return checkWinner(playerX) || checkWinner(player0) || board.isBoardFull();
    }

    public void changeTurn() {
        currentPlayer = currentPlayer == playerX? player0 : playerX;
    }

    public void placeMarker(int boxNumber) {
        board.placeMarker(boxNumber, currentPlayer.getMarker());
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}

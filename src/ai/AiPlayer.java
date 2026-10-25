package ai;
// klopt voor eerste demo. kan random moves maken op basis van moves die hij krijgt van de game
import model.TicTacToeGame;

import java.util.List;
import java.util.Random;

public class  AiPlayer {

    private char aiMarker;
    private char opponentMarker;
    private Random random;

    //bepalen welke karakter de AI is en welke de opponent is
    public AiPlayer(char aiMarker) {
        this.aiMarker = aiMarker;
        this.random = new Random();

        if (aiMarker == '0') {
            opponentMarker = 'X';
        } else {
            opponentMarker = '0';
        }
    }

    public int chooseMove(TicTacToeGame game) {
        // Is de ai wel aan de beurt?
        if (game.getMarker(game.getCurrentPlayer()) != aiMarker) { return -1; }
        //vraag om het bord van model.TicTacToeGame
        char[][] board = game.getBoard();

        //vragen om alle mogelijke moves
        List<Integer> validMoves = game.getValidMoves();

        // Zijn geen zetten meer als de lijst van moves leeg is
        if (validMoves.isEmpty()) { return -1;}

        // 1) kijk eerst of de AI kan winnen
        int winningMove = findWinningMove(game, board, validMoves, '0');

        // return winning move als die er is.
        if (winningMove != -1) { return winningMove; }

        // 2) kan de opponent winnen?
        int blockingMove = findWinningMove(game, board,validMoves,opponentMarker);

        // Zo ja blokeer de move
        if (blockingMove != -1){ return blockingMove; }

        // Probeer anders het midden te paken als die is gegeven als mogelijke move
        if (validMoves.contains(4)) { return 4; }

        // kiest random een zet uit de lijst met moves
        int randomIndex = random.nextInt(validMoves.size());

        // stuurt 1 van de mogelijke moves terug
        return validMoves.get(randomIndex);
    }

    private int findWinningMove(TicTacToeGame game, char[][] board, List<Integer> validMoves, char marker){
        for(int move : validMoves) {
            // voor elke test maken we een nieuwe copy
            char [][] testBoard = copyBoard(board);

            int row = move / 3;
            int column = move % 3;

            //doe zet op tijdelijke board
            testBoard[row][column] = marker;
            //return de eerste move die zorgt voor een win
            if (game.checkWinner(testBoard, marker)) {
                return move;
            }
        }
        //kan niet winnen dus -1 terug
        return -1;
    }

    // op deze copy van het board gaan we testen wat een goede move is
    private char[][] copyBoard(char[][] board) {
        char[][] copy = new char[board.length][board[0].length];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                copy[i][j] = board[i][j];
            }
        }
        return copy;
    }
}

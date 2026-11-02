package ai;

import java.util.ArrayList;
import java.util.List;

import model.Board;
import model.GameState;
import model.Move;
import model.TicTacToeGame;
import model.TicTacToeMove;
import model.TicTacToeTurnState;
import model.player.MoveStrategy;

/**
 * Tic-Tac-Toe AI-strategie met minimax werking
 *
 * Simuleert mogelijke vervolgzetten op bordkopieën
 * De echte game wordt nooit gewijzigd
 */
public final class TicTacToeMinimaxStrategy implements MoveStrategy {

    @Override
    public Move chooseMove(GameState gameState) {

        if (!(gameState instanceof TicTacToeTurnState state)) {
            throw new IllegalArgumentException("Een TicTacToeTurnState is vereist");
        }

        // Bepaal vanuit de snapshot welke marker de AI heeft
        char aiMarker = state.currentMarker();

        char opponentMarker = aiMarker == Board.PLAYER_X_MARKER ? Board.PLAYER_0_MARKER : Board.PLAYER_X_MARKER;

        // Werk uitsluitend met een kopie van het bord
        char[][] board = state.boardCopy();

        List<Integer> validMoves = state.validMoves();

        if (validMoves.isEmpty()) {
            throw new IllegalStateException("Geen geldige AI-zetten beschikbaar");
        }

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // Probeer iedere mogelijke zet.
        for (int move : validMoves) {

            char[][] testBoard = copyBoard(board);

            testBoard[move / Board.SIZE] [move % Board.SIZE] = aiMarker;

            // Na onze zet is de tegenstander aan de beurt.
            int score = minimax(testBoard, false, aiMarker, opponentMarker);

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return new TicTacToeMove(bestMove);
    }

    /**
     * Minimax:
     * AI maximaliseert de score
     * Tegenstander minimaliseert de score
     */
    private int minimax(char[][] board, boolean maximizingPlayer, char aiMarker, char opponentMarker) {

        int score = evaluateBoard(board, aiMarker, opponentMarker);

        if (score != 0) {return score;}

        List<Integer> validMoves = getValidMoves(board);

        if (validMoves.isEmpty()) {return 0;}

        if (maximizingPlayer) {

            int bestScore = Integer.MIN_VALUE;

            for (int move : validMoves) {

                char[][] testBoard = copyBoard(board);

                testBoard[move / Board.SIZE] [move % Board.SIZE] = aiMarker;

                int currentScore = minimax(testBoard, false, aiMarker, opponentMarker);

                bestScore = Math.max(bestScore, currentScore);
            }
            return bestScore;

        }
        else {
            int bestScore = Integer.MAX_VALUE;

            for (int move : validMoves) {

                char[][] testBoard = copyBoard(board);

                testBoard[move / Board.SIZE] [move % Board.SIZE] = opponentMarker;

                int currentScore = minimax(testBoard, true, aiMarker, opponentMarker);

                bestScore = Math.min(bestScore, currentScore);
            }
            return bestScore;
        }
    }

    /**
     * Beoordeelt een gesimuleerde bordtoestand.
     */
    private int evaluateBoard(char[][] board, char aiMarker, char opponentMarker) {
        if (TicTacToeGame.checkWinner(board, aiMarker)) {return 10;}
        if (TicTacToeGame.checkWinner(board, opponentMarker)) {return -10;}
        return 0;
    }

    /**
     * Bepaalt de beschikbare zetten op een gesimuleerd bord
     */
    private List<Integer> getValidMoves(char[][] board) {
        List<Integer> validMoves = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {

                if (board[row][column] == Board.EMPTY_MARKER) {
                    validMoves.add(row * Board.SIZE + column);
                }
            }
        }
        return validMoves;
    }

    /**
     * Maakt een nieuwe bordkopie voor Minimax-simulaties
     */
    private char[][] copyBoard(char[][] board) {

        char[][] copy = new char[board.length][];

        for (int row = 0; row < board.length; row++) {
            copy[row] = board[row].clone();
        }
        return copy;
    }
}
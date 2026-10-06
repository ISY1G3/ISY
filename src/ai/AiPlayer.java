package ai;
// klopt voor derde demo. kan alle mogelijke moves checken voor per beurt en kan kijken tot het eind.
import model.TicTacToeGame;

import java.util.ArrayList;
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
        if (game.getCurrentPlayer().getMarker() != aiMarker) { return -1; }

        //vraag om het bord van model.TicTacToeGame
        char[][] board = game.getBoard();

        //vragen om alle mogelijke moves
        List<Integer> validMoves = game.getValidMoves();

        // Zijn geen zetten meer als de lijst van moves leeg is
        if (validMoves.isEmpty()) { return -1;}

            //DEBUG//
            System.out.println();
            System.out.println("AI beurt:");
            System.out.println("AI is speler: " + aiMarker);
            System.out.println("zetten mogelijk: " + validMoves);

        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // hier gaan we alle mogelijke zetten doen
        for (int move : validMoves) {
            char [][] testBoard = copyBoard(board);

            int row = move / 3;
            int column = move % 3;

            // ai gaat zetten proberen
            testBoard[row][column] = aiMarker;

                //DEBUG//
                System.out.println();
                System.out.println("AI probeert zet: " + move);

            // Nu doet de opponent een move
            int score = minimax(game, testBoard,false);

                //DEBUG//
                System.out.println("Zet: " + move + " krijgt score: " + score);

            // En als laatst checken of de move die we als laatst hebben geprobeerd beter is dan degene die we hadden tot nu toe
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;

                    //DEBUG
                    System.out.println();
                    System.out.println("Nieuwe beste zet gevonden: " + bestMove + " score: " + bestScore);
            }
        }
            //DEBUG
            System.out.println();
            System.out.println("AI kiest uiteindelijk zet: " + bestMove + " met score " + bestScore);
            System.out.println();

        return bestMove;
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

    //Beoordeling die de ai geeft aan bepaalde posities
    private int evaluateBoard(TicTacToeGame game, char[][] board) {
        //kijkt voor de ai en de opponent of 1 van de twee een winnende move heeft alleen deze geven punten
        if (game.checkWinner(board, aiMarker)) {return 10;}

        if (game.checkWinner(board, opponentMarker)) {return -10;}
        //base case
        return 0;
    }

    // dit gaat voor de ai de een nieuwe lijst terug geven met de nieuwe mogelijke moves nadat een gesimuleerde zet is gedaan
    private List<Integer> getValidMoves(char[][] board) {
        List<Integer> validMoves = new ArrayList<>();

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board[row].length; column++) {

                if (board[row][column] != 'X' && board[row][column] != '0') {

                    int move = row * 3 + column;
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }

    private int minimax(TicTacToeGame game, char[][] board, boolean maximizingPlayer) {

        // Kijk eerst of dit bord al afgelopen is dus een win of loss
        int score = evaluateBoard(game, board);

        if (score != 0) {
            return score;
        }

        // 2. Vraag geldige zetten op van dit "gesimuleerde" bord kopie
        List<Integer> validMoves = getValidMoves(board);

        // Geen winnaar en geen zetten = gelijkspel
        if (validMoves.isEmpty()) {
            return 0;
        }

        // 3. AI is aan de beurt
        if (maximizingPlayer) {

            int bestScore = Integer.MIN_VALUE;

            for (int move : validMoves) {

                // Maak een nieuwe kopie voor deze simulatie (hier wordt dus een nieuwe move op gemaakt)
                char[][] testBoard = copyBoard(board);

                int row = move / 3;
                int column = move % 3;

                // ai doet zijn daadwerkelijke move
                testBoard[row][column] = aiMarker;

                // Nu is tegenstander
                int currentScore = minimax(game, testBoard, false);

                bestScore = Math.max(bestScore, currentScore);
            }

            return bestScore;
        }

        // 4. Tegenstander is aan de beurt
        else {

            int bestScore = Integer.MAX_VALUE;

            for (int move : validMoves) {

                char[][] testBoard = copyBoard(board);

                int row = move / 3;
                int column = move % 3;

                // Simuleer zet van tegenstander
                testBoard[row][column] = opponentMarker;

                // Daarna is AI weer aan de beurt
                int currentScore = minimax(game, testBoard, true);

                bestScore = Math.min(bestScore, currentScore);
            }

            return bestScore;
        }
    }

}

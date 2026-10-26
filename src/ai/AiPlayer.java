// klopt voor derde demo
// zou evt nog kunnen maken dat er maar 1 copy wordt gemaakt en dat er maar wordt gesimuleerd op 1 bord maar dat is onnodige moeite voor nu
//
// hoe dit werkt is de AI krijgt 1 veilige copy van het echte bord
// Minmax maakt voor elke mogelijk zet een nieuwe copy van de huidige gesimuleerde bordsituatie
// Bijvoorbeeld:
//
// Echt bord
//   v met getBoard() maken we:
// basis-kopie
//   - - -
//   - X -
//   - - -
//
// AI probeert dan verschillende zetten zoals:
//   0 - -        - 0 -        - - 0
//   - X -        - X -        - X -
//   - - -        - - -        - - -
//
// Voor ELKE van deze situaties probeert de tegenstander daarna
// weer alle mogelijke antwoorden:
//   0 X -        0 - X        0 - -
//   - X -        - X -        X X -
//   - - -        - - -        - - -
//
// En zo gaat Minimax door totdat iemand wint of het bord vol is.
package ai;

import model.TicTacToeGame;
import model.Board;
import java.util.ArrayList;
import java.util.List;

public class  AiPlayer {

    private char aiMarker;
    private char opponentMarker;

    //bepalen welke karakter de AI is en welke de opponent is
    public AiPlayer(char aiMarker) {
        if(aiMarker != Board.PLAYER_X_MARKER && aiMarker != Board.PLAYER_0_MARKER){
            throw new IllegalArgumentException("geen geldige AI marker");
        }
        this.aiMarker = aiMarker;
        this.opponentMarker = aiMarker == Board.PLAYER_X_MARKER ? Board.PLAYER_0_MARKER : Board.PLAYER_X_MARKER;
    }
    // probeert elke geldige zet en geeft die een Minimax-score daaruit wordt de beste zet gekozen
    public int chooseMove(TicTacToeGame game) {
        // is de ai wel aan de beurt?
        if (!game.isActive() || game.getCurrentMarker() != aiMarker) { return -1; }

        //vraag om het bord van model.TicTacToeGame
        char[][] board = game.getBoard();

        //vragen om alle mogelijke moves
        List<Integer> validMoves = game.getValidMoves();

        // er zijn geen zetten meer als de lijst van moves leeg is
        if (validMoves.isEmpty()) { return -1;}

        // we zoeken de hoogste score vanuit het perspectief van de AI
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        // hier gaan we alle mogelijke zetten doen
        for (int move : validMoves) {
            char [][] testBoard = copyBoard(board);

            int row = move / Board.SIZE;
            int column = move % Board.SIZE;

            // ai gaat zetten proberen
            testBoard[row][column] = aiMarker;

            // dan doet de opponent een move
            int score = minimax(game, testBoard,false);

            // en als laatst checken of de move die we als laatst hebben geprobeerd beter is dan degene die we hadden tot nu toe
            // stel er zijn dus 3 met de hoogste maar dezelfde score wordt voor nu de eerste gekozen voornamelijk te zien
            // als de AI begint want hij zal altijd het vakje linksboven kiezen want er is niks beters nadat het eerste vakje 0 heeft gescoord
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        return bestMove;
    }

    // Onderzoekt de volgende beurten recursief, de AI maximaliseert de score, en de tegenstander minimaliseert */
    private int minimax(TicTacToeGame game, char[][] board, boolean maximizingPlayer) {

        // Kijk eerst of dit bord al afgelopen is dus een win of loss
        int score = evaluateBoard(game, board);

        if (score != 0) {return score;}

        // Vraag geldige zetten op van de meegegeven "gesimuleerde" bord kopie
        List<Integer> validMoves = getValidMoves(board);

        // Geen winnaar en geen zetten = gelijkspel
        if (validMoves.isEmpty()) {return 0;}

        // AI is aan de beurt
        if (maximizingPlayer) {
            int bestScore = Integer.MIN_VALUE;

            for (int move : validMoves) {
                // Maak een nieuwe kopie voor deze simulatie (hier wordt dus een nieuwe move op gemaakt)
                char[][] testBoard = copyBoard(board);
                // bereken waar op het testboard de mogelijke move is in coords: row en column
                int row = move / Board.SIZE;
                int column = move % Board.SIZE;

                // ai doet zijn daadwerkelijke move
                testBoard[row][column] = aiMarker;

                // Nu is tegenstander
                int currentScore = minimax(game, testBoard, false);

                bestScore = Math.max(bestScore, currentScore);
            }
            return bestScore;
        }

        // Tegenstander is aan de beurt
        else {
            int bestScore = Integer.MAX_VALUE;

            for (int move : validMoves) {

                char[][] testBoard = copyBoard(board);
                // bereken waar op het testboard de mogelijke move is in coords: row en column
                int row = move / Board.SIZE;
                int column = move % Board.SIZE;

                // Simuleer zet van tegenstander
                testBoard[row][column] = opponentMarker;

                // Daarna is AI weer aan de beurt
                int currentScore = minimax(game, testBoard, true);

                bestScore = Math.min(bestScore, currentScore);
            }
            return bestScore;
        }
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

                if (board[row][column] == Board.EMPTY_MARKER ) {

                    int move = row * Board.SIZE + column;
                    validMoves.add(move);
                }
            }
        }
        return validMoves;
    }
}
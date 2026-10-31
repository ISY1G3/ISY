package model;

import java.util.List;
import java.util.Objects;

/**
 * Bewaart een momentopname van de huidige TTT-gametoestand
 * die de AI kan gebruiken om zijn volgende zet te berekenen
 *
 * Voorheen kreeg de AI via GameState een verwijzing naar de echte
 * TicTacToeGame. Hierdoor had de AI toegang tot het originele spel,
 * terwijl hij alleen informatie nodig heeft om een zet te berekenen
 *
 * het enige wat nu bewaard wordt voor de AI is:
 * - Een kopie van het speelbord.
 * - De marker van de speler die aan de beurt is (X of 0).
 * - Een lijst met de momenteel geldige zetten.
 *
 * We gebruiken defensieve kopieën om te voorkomen dat wijzigingen
 * buiten deze klasse invloed hebben op de opgeslagen speltoestand.
 *
 * Ook wanneer de AI het bord opvraagt, krijgt hij een nieuwe kopie.
 * Zo kan Minimax mogelijke zetten simuleren zonder de opgeslagen
 * speltoestand of het echte speelbord te veranderen.
 *
 * Hierdoor blijft de verantwoordelijkheid gescheiden:
 * - TicTacToeTurnState levert de gegevens voor de AI
 * - TicTacToeMinimaxStrategy berekent de beste zet
 * - GameService verwerkt uiteindelijk de gekozen zet
 */
public final class TicTacToeTurnState implements GameState {

    private final char[][] board;
    private final char currentMarker;
    private final List<Integer> validMoves;

    public TicTacToeTurnState(char[][] board, char currentMarker, List<Integer> validMoves) {
        // bewaar een eigen kopie, niet de ontvangen array
        this.board = copyBoard(board);

        if (currentMarker != Board.PLAYER_X_MARKER && currentMarker != Board.PLAYER_0_MARKER) {
            throw new IllegalArgumentException("Ongeldige huidige marker");
        }

        this.currentMarker = currentMarker;

        // List.copyOf maakt een lijst die niet verandert kan worden
        this.validMoves = List.copyOf(Objects.requireNonNull(validMoves));
    }

    // Geeft opnieuw een kopie terug
    public char[][] boardCopy() {
        return copyBoard(board);
    }

    public char currentMarker() {
        return currentMarker;
    }

    public List<Integer> validMoves() {
        return validMoves;
    }

    private static char[][] copyBoard(char[][] source) {

        if (source == null || source.length != Board.SIZE) {
            throw new IllegalArgumentException("Bord moet 3 rijen bevatten");
        }

        char[][] result = new char[Board.SIZE][Board.SIZE];

        for (int row = 0; row < Board.SIZE; row++) {

            if (source[row] == null || source[row].length != Board.SIZE) {
                throw new IllegalArgumentException("Iedere bordrij moet 3 vakjes bevatten");
            }

            System.arraycopy(source[row], 0, result[row], 0, Board.SIZE);
        }

        return result;
    }
}
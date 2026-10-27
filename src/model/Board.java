package model;
/**
 * Slaat het 3x3 bord op
 * deze klasse controleert vaknummers en of een vak bezet is
 */
public class Board {

    public static final int SIZE = 3; //Bord is 3x3
    public static  final char PLAYER_X_MARKER = 'X'; //Teken van speler "X"
    public static  final char PLAYER_0_MARKER = '0'; //Teken van speler "0"

    public static final char EMPTY_MARKER = '\0'; // Geeft aan dat een vakje leeg is

    private final char board[][]; // mag niet vervangen worden of veranderd daarom nu final

    public Board() { 
        board = new char[SIZE][SIZE]; //board is een 2d array waarin de vakjes worden opgeslagen
    }

    // print mag weg en kan evt nog worden gezet in TUI

    public boolean isBoardFull() { //Kijkt of alle 9 vakjes bezet zijn.
        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                if (board[i][j] == EMPTY_MARKER) {
                    return false;
                }
            }
        }
        return true;
    }

    public char[][] getBoard() { //get voor encapsulation (zie README.md)
        char[][] boardCopy = new char[SIZE][SIZE];

        for (int i=0; i<SIZE; i++) {
            for (int j=0; j<SIZE; j++) {
                boardCopy[i][j] = board[i][j];
            }
        }
        return boardCopy;
    }

    public boolean isValidBoxNumber(int boxNumber) { //Controleert of een gekozen nummer tussen 0 en 8 ligt
        return boxNumber >= 0 && boxNumber < (SIZE*SIZE);
    }

    public boolean isBoxNumberEmpty(int boxNumber) { //Controleert of een vakje vrij is
        if (!isValidBoxNumber(boxNumber)) {
            return false;
        }
        int i = boxNumber/SIZE;
        int j = boxNumber%SIZE;

        return board[i][j] == EMPTY_MARKER; // deze check versimpelt en geeft de board plek terug als deze leeg is/gelijk is aan EMPTY_MARKER
    }

    public boolean placeMarker(int boxNumber, char marker) { //Zet "X" of "0" in een vrij vakje
        if (marker != PLAYER_X_MARKER && marker != PLAYER_0_MARKER) { // dubbele check weggehaald en veranderd naar
            return false;                                             // een check die wel goed checkt of de box niet al een X of 0 heeft
        }
        // het gekozen vakje moet namelijk geldig en leeg zijn
        if (!isBoxNumberEmpty(boxNumber)) {
            return false;
        }
        int i = boxNumber/SIZE;
        int j = boxNumber%SIZE;
        board[i][j] = marker;   //hier wordt het daadwerkelijke bord pas aangepast
        return true;
    }
}

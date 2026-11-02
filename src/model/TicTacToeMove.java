package model;

/**Vertegenwoordigt 1 TTT zet
 * De index loopt van 0 t/m 8
 * De GameService controleert of de zet geldig is */
// waarom doen we dit?
// de strategie moet een zet terug kunnen geven zonder afhankelijk te zijn van de interne implementatie van GameService
public record TicTacToeMove(int index) implements Move {
}

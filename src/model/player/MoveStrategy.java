package model.player;

import model.GameState;
import model.Move;

@FunctionalInterface
/** functionele interface voor een AI-strategie. De gekozen Tic-Tac-Toe-strategie koppelt hier de bestaande Minimax aan
 */
public interface MoveStrategy {
    Move chooseMove(GameState gameState);
}

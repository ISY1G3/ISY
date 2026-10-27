package model;

/**
 * dit is de verbinding tussen een speler en de verwerking van zetten
 */
public interface MoveContext {

    /** Geeft de spelgegevens terug die een speler of AI nodig heeft om een zet te kiezen.*/
    GameState getGameState();

    /** Dient een voorgestelde zet in; de service blijft verantwoordelijk voor validatie. */
    void submitMove(Move move);
}

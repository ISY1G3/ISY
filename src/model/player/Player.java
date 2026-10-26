package model.player;

import model.MoveContext;
/** speler is abstract en niet afhankelijk van een spel. naam en zetsoort zijn algemeen
 * X en 0 worden pas inTicTacToeGame toegewezen
 */
public abstract class Player {

    private final String name;

    protected Player(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Naam mag niet leeg zijn.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void askForMove(MoveContext context);
}
package model.player;

import model.GameState;
import model.Move;
import model.MoveContext;
import java.util.Objects;
/** AI-speler vraagt via MoveStrategy een zet en dient die in via MoveContext let op deze player kent zelf geen X of 0*/
public final class AIPlayer extends Player {

    private final MoveStrategy strategy;

    public AIPlayer(String name, MoveStrategy strategy) {
        super(name);
        this.strategy = Objects.requireNonNull(strategy, "AI-strategie is verplicht.");
    }

    @Override
    public void askForMove(MoveContext context) {
        Objects.requireNonNull(context, "MoveContext mag niet null zijn.");
        GameState state = Objects.requireNonNull(context.getGameState(), "GameState ontbreekt.");
        Move move = Objects.requireNonNull(strategy.chooseMove(state), "AI gaf geen zet terug.");
        context.submitMove(move);
    }
}
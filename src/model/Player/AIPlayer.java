package model.Player;

import java.util.Optional;

public class AIPlayer extends Player {
    
    public AIPlayer(char marker, Optional<String> name) {
        super(marker, name);
    }
    
    public AIPlayer(char marker) {
        super(marker, Optional.empty());
    }

    @Override
    public int takeTurn() {       // Implement the logic for making a move for the AI player
        // This could involve getting input from the user, validating it, and updating the game state
        return 0;
    }
}
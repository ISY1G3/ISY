package model.Player;

import java.util.Optional;

public class LocalPlayer extends Player {
    public LocalPlayer(char marker) {
        super(marker, Optional.empty());
    }
    public LocalPlayer(char marker, Optional<String> name) {
        super(marker, name);
    }

    @Override
    public void takeTurn() {
        // Implement the logic for making a move for the local player
        // This could involve getting input from the user, validating it, and updating the game state
    }
}
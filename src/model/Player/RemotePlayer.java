package model.Player;

import java.util.Optional;

public class RemotePlayer extends Player {
    public RemotePlayer(char marker) {
        super(marker);
    }
    public RemotePlayer(char marker, Optional<String> name) {
        super(marker, name);
    }

    @Override
    public int takeTurn() {
        // Implement the logic for making a move for the remote player
        // This could involve getting input from the user, validating it, and updating the game state
        return 0;
    }
}
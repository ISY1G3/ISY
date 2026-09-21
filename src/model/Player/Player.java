/*
 * This is the Generic Player class that will be extended for:
 * - Local Player
 * - Remote Player
 * - AI Player
 * 
 * - These files were added as stubs for another issue, but do not constitute a complete implementation. This would need to be expanded upon in the future.
 * 
 * Because the class is a generic, abstract class, it should never be instantiated directly.
*/

package model.Player;

import java.util.Optional;

public abstract class Player {
    private String name;
    private char marker;



    public Player(char marker) {
        this.marker = marker;
    }

    public Player(char marker, Optional<String> name) {
        this.marker = marker;
        this.name = name.orElse("Player " + marker);
    }

    public String getName() {
        return name;
    }

    public char getMarker() {
        return marker;
    }

    public abstract int takeTurn(int move); // Player is an abstract class, meaning it will never be instantiated directly. Instead, it will be extended by other classes that implement the makeMove() method.
}
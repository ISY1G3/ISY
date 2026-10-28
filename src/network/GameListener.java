package network;

/*  Luistert naar events tijdens een match.
    Voor GameController */

public interface GameListener {
    default void onMatch(String opponent, String gameType, String startingPlayer) {}
    default void onYourTurn() {}
    default void onMove(String player, int move) {}
    default void onResult(ResultType type, String comment) {}
    default void onMessage(String sender, String text) {}
}
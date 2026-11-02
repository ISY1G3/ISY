package network;

/*  Luistert naar events rond de connectie
    Is bedoeld voor loginscreen of fout bij conncetie */

public interface ConnectionListener {
    default void onLoginOk() {}
    default void onCommandOk(String command) {}
    default void onCommandError(String command, String reason) { onError(reason); }
    default void onError(String reason) {}
    default void onDisconnect() {}
}

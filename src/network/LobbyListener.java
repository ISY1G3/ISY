package network;

import java.util.List;

/** Luistert naar events rond de lobby.
    Aangeroepen door GameServer en is voor dingen zoals choosegamescreen*/

public interface LobbyListener {
    default void onGameList(List<String> games) {}
    default void onPlayerList(List<String> players) {}
    default void onChallenge(String challenger, String game, int nr) {}
    default void onChallengeCancelled(int nr) {}
}
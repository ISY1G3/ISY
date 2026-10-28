package ui.tui.view;

import ui.tui.LobbyData;

public class LobbyView extends TuiView {
    private final LobbyData data;
    public LobbyView(LobbyData data) { this.data = data; }
    @Override public void draw() {
        System.out.println("\n========== ONLINE LOBBY ==========");
        System.out.println("Ingelogd: " + data.getNickname());
        System.out.println("Games: " + (data.getGames().isEmpty() ? "Nog niet geladen" : data.getGames()));
        System.out.println("Spelers: " + (data.getPlayers().isEmpty() ? "Geen / nog niet geladen" : data.getPlayers()));
        System.out.println("Uitdagingen:");
        if (data.getChallenges().isEmpty()) System.out.println("  Geen openstaande uitdagingen");
        for (LobbyData.Challenge c : data.getChallenges()) {
            System.out.println("  #" + c.number() + " van " + c.challenger() + " (" + c.game() + ")");
        }
        System.out.println("Tic-Tac-Toe: " + (data.isSubscribedTicTacToe() ? "Aangemeld" : "Niet aangemeld"));
        System.out.println("Besturing op deze client: " + (data.isLocalAi() ? "AI (Minimax)" : "Mens"));
        System.out.println("[1] Vernieuw spelers en games");
        System.out.println("[2] Daag speler uit");
        System.out.println("[3] Accepteer uitdaging");
        System.out.println("[4] Aanmelden voor Tic-Tac-Toe (kan direct match starten)");
        System.out.println("[5] Wissel besturing: Mens / AI");
        System.out.println("[6] Informatie over toernooien");
        System.out.println("[0] Uitloggen");
    }
}

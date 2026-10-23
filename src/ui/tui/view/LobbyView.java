package ui.tui.view;

import ui.tui.LobbyData;
// ONLINE
// voorlopige online lobby, alle data hiervan zijn voorbeelden
public class LobbyView extends TuiView {
    private final LobbyData data;
    public LobbyView(LobbyData data) { this.data = data; }

    @Override
    public void draw() {
        System.out.println("\n================================");
        System.out.println("         ONLINE LOBBY");
        System.out.println("       [DEMO / OFFLINE]");
        System.out.println("================================");
        System.out.println("Nickname: " + data.getNickname());
        System.out.println("Voorbeeldspelers: Piet, Klaas");
        System.out.println("Voorbeelduitdaging: geen");
        System.out.println("[1] Bekijk uitdagingen (demo)");
        System.out.println("[2] Daag een speler uit (demo)");
        System.out.println("[3] Tic-Tac-Toe uitnodigingen: "
                + (data.isSubscribedTicTacToe() ? "Aan" : "Uit") + " (demo)");
        System.out.println("[4] Toernooien (demo)");
        System.out.println("[5] Bekijk GameScreen (demo)");
        System.out.println("[0] Back");
    }
}

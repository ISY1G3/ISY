package ui.tui.view;

import ui.tui.LocalSetupData;

/** Toont configuratie; wijzigt zelf geen spelergegevens. */
public class LocalSetupView extends TuiView {
    private final LocalSetupData data;
    public LocalSetupView(LocalSetupData data) { this.data = data; }

    @Override
    public void draw() {
        System.out.println("\n================================");
        System.out.println("       LOCAL GAME SETUP");
        System.out.println("================================");
        System.out.println("Game: " + data.getGameName());
        System.out.println("[1] Speler 1 type: " + (data.isPlayer1Ai() ? "AI" : "Lokaal"));
        System.out.println("[2] Speler 1 naam: " + data.getPlayer1Name());
        System.out.println("[3] Speler 2 type: " + (data.isPlayer2Ai() ? "AI" : "Lokaal"));
        System.out.println("[4] Speler 2 naam: " + data.getPlayer2Name());
        System.out.println("[5] Beginspeler: " + data.startingPlayerLabel());
        System.out.println("[6] Start de game");
        System.out.println("[0] Back");
    }

    public void askName(int player) {
        System.out.println("Nieuwe naam voor speler " + player + " (of 'back' om te annuleren):");
    }
}


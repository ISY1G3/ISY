package ui.tui.view;

import model.Board;
import model.GameMode;
import model.TicTacToeGame;
import model.player.AIPlayer;
import online.OnlineGameCoordinator;
import service.GameSession;

// Toont actuele bord en game info
// de regels en zetten worden verwerkt in de GameService
// voor zowel online en offline
public class GameView extends TuiView {
    private final GameSession session;
    private final OnlineGameCoordinator online;
    private final TicTacToeBoardView boardView = new TicTacToeBoardView();
    public GameView(GameSession session) { this(session, null); }
    public GameView(GameSession session, OnlineGameCoordinator online) {
        this.session = session; this.online = online;
    }
    // Bestaande online demo-constructor wordt niet meer gebruikt.
    @Override public void draw() {
        System.out.println("\n========== " + session.getGameName() + " ==========");
        System.out.println(session.getPlayer1Name() + " (X) vs " + session.getPlayer2Name() + " (0)");
        if (!(session.getGame() instanceof TicTacToeGame game)) {
            System.out.println("Nog geen bordview voor dit spel"); return;
        }
        boardView.draw(game.getBoard());
        if (!session.isActive()) { System.out.println("Status: " + session.getStatus()); return; }
        System.out.println("Aan de beurt: " + game.getCurrentPlayer().getName()
                + " (" + game.getCurrentMarker() + ")");
        if (session.getGameMode() == GameMode.ONLINE) {
            boolean localAi = session.getPlayer1() instanceof AIPlayer
                    || session.getPlayer2() instanceof AIPlayer;
            System.out.println("Lokale besturing: " + (localAi ? "AI (automatisch)" : "Mens"));
            if (online == null) System.out.println("Onlinecoordinator ontbreekt");
            else if (online.hasGivenUp()) System.out.println("Wachten op uitslag na opgave...");
            else if (online.isAwaitingConfirmation()) System.out.println("Wachten op serverbevestiging...");
            else if (online.isYourTurn()) System.out.println(localAi
                    ? "AI kiest automatisch een zet..." : "Jouw beurt! Kies een vakje [1-9]");
            else System.out.println("Wachten op server of tegenstander...");
            System.out.println("[0] Wedstrijd opgeven");
        } else {
            System.out.println("Kies een vakje [1-9]");
            System.out.println("[0] Terug naar lokale setup");
        }
    }
}

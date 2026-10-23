package ui.tui.view;

import model.TicTacToeGame;
import service.GameSession;

/** Berekent de tekst rechtstreeks uit de beëindigde game, zonder resultaatkopie. */
public class ResultView extends TuiView {
    private final GameSession session;
    private final TicTacToeBoardView boardView = new TicTacToeBoardView();

    public ResultView(GameSession session) { this.session = session; }

    @Override
    public void draw() {
        System.out.println("\n================================");
        System.out.println("          GAME RESULT");
        System.out.println("================================");

        if (session.getGame() instanceof TicTacToeGame game) {
            boardView.draw(game.getBoard());
            if (game.playerXWon()) {
                System.out.println("\nWinnaar: " + session.getPlayer1Name() + " (X)");
            } else if (game.player0Won()) {
                System.out.println("\nWinnaar: " + session.getPlayer2Name() + " (0)");
            } else if (game.isGameOver()) {
                System.out.println("\nGelijkspel!");
            } else {
                System.out.println("\nWedstrijd beëindigd zonder winnaar.");
            }
        } else {
            System.out.println("Uitslag nog niet beschikbaar voor dit spel.");
        }
        System.out.println("[1] Nieuwe wedstrijd (zelfde instellingen)");
        System.out.println("[0] Hoofdmenu");
    }
}

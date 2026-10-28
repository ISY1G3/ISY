package ui.tui.view;

import network.ResultType;
import service.GameSession;
import model.TicTacToeGame;

/** Serverresultaat is bepalend, ook bij opgave of verbroken wedstrijd. */
public final class OnlineResultView extends TuiView {
    private final GameSession session;
    private final ResultType result;
    private final String comment;
    public OnlineResultView(GameSession session, ResultType result, String comment) {
        this.session = session; this.result = result; this.comment = comment;
    }
    @Override public void draw() {
        System.out.println("\n========== ONLINE RESULTAAT ==========");
        if (session.getGame() instanceof TicTacToeGame game) new TicTacToeBoardView().draw(game.getBoard());
        System.out.println(switch (result) {
            case WIN -> "Je hebt gewonnen!";
            case LOSS -> "Je hebt verloren.";
            case DRAW -> "Gelijkspel.";
        });
        if (comment != null && !comment.isBlank()) System.out.println("Server: " + comment);
        System.out.println("[0] Terug naar lobby");
    }
}

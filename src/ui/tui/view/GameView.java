package ui.tui.view;

import model.Board;
import model.TicTacToeGame;
import service.GameSession;

// Toont actuele bord en game info
// de regels en zetten worden verwerkt in de GameService
public class GameView extends TuiView {
    private final GameSession session;
    // deze 3 worden alleen gebruikt voor de ONLINE demo en niet voor de uiteindelijke game
    private final String demoGameName;
    private final String demoPlayer1;
    private final String demoPlayer2;
    // hergebruikt de al bestaande board view
    private final TicTacToeBoardView boardView = new TicTacToeBoardView();

    //de constructor voor een game
    public GameView(GameSession session) {
        this.session = java.util.Objects.requireNonNull(session); // dit voorkomt dat je per ongeluk een lokale GameView zonder een sessie maakt

        this.demoGameName = null;        // gegevens die we halen uit de game sessie
        this.demoPlayer1 = null;
        this.demoPlayer2 = null;
    }

    // tijdelijk, de online demo heeft nog geen echte server/gamesessie.
    public GameView(String gameName, String player1, String player2) {
        this.session = null;    //demo heeft nog geeen echte session

        this.demoGameName = gameName;
        this.demoPlayer1 = player1;
        this.demoPlayer2 = player2;
    }

    @Override
    public void draw() {
        //ONLINE demo heeft nog geen sessie
        if (session == null) {
            drawHeader(demoGameName,demoPlayer1,demoPlayer2);

            System.out.println("ONLINE DEMO");

            //tijdelijk leeg board alleen voor de weergave, hier wordt verder niks mee gedaan
            boardView.draw(new char [Board.SIZE][Board.SIZE]);

            System.out.println("Online is nog niet gekoppeld");
            System.out.println("[0] Back");

            return;
        }

        // algemene info over deze Session
        drawHeader(session.getGameName(),session.getPlayer1Name(),session.getPlayer2Name());

        //check eerst welk spel er in de session zit
        if (!(session.getGame() instanceof TicTacToeGame game)) {
            System.out.println("Nog geen bordView voor dit spel");
            System.out.println("[0] Back");
            return;
        }

        //Haal het bord op en print het
        boardView.draw(game.getBoard());

        //toon de huidige speler
        if(session.isActive()){
            char marker = game.getMarker(game.getCurrentPlayer());

            String playerName = marker == Board.PLAYER_X_MARKER ? session.getPlayer1Name() : session.getPlayer2Name();

            System.out.println("\nAan de beurt: " + playerName + " (" + marker + ")");
            System.out.println("Kies een vakje [1-9]");
            System.out.println("[0] Stoppen en terug naar setup");
        }
        else {
            System.out.println("\nStatus: " + session.getStatus());
        }
    }

    // hiermee hoeven we niet voor de TUI en online demo 2 aparte print statements voor de titel e.d.
    private void drawHeader (String gameName, String player1, String player2) {
        // print game info
        System.out.println("\n================================");
        System.out.println("       " + gameName.toUpperCase() + " GAME");
        System.out.println("================================");
        System.out.println(player1 + " (X) vs " + player2 + " (0)");
    }
}

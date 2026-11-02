package service;

import ai.TicTacToeMinimaxStrategy;
import model.*;
import model.player.AIPlayer;
import model.player.LocalPlayer;
import model.player.Player;
import model.player.RemotePlayer;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Objects;

/** spelservice voor lokale en online wedstrijden
 *  kan spelers en sessies maken, valideertzetten en laat het model aanpassen */
public class GameService {
    private GameSession currentSession;

    // start een local pvp of pvAI game de korte variant leidt standaard AI keuzen af uit de modus
    public synchronized GameSession startTicTacToe(GameMode mode, String name1, String name2, int startingPlayer) {
        return startTicTacToe(mode, name1, name2, startingPlayer, false, mode == GameMode.PLAYER_VS_AI);
    }

    public synchronized GameSession startTicTacToe(GameMode mode, String name1, String name2,
                                                   int startingPlayer, boolean player1Ai, boolean player2Ai) {
        ensureNoActiveSession();
        if (mode == null || mode == GameMode.ONLINE || name1 == null || name1.isBlank()
                || name2 == null || name2.isBlank())
            throw new IllegalArgumentException("Ongeldige lokale instellingen");
        if (player1Ai && player2Ai) throw new IllegalArgumentException("Twee AI spelers worden niet ondersteund");
        if ((mode == GameMode.PLAYER_VS_AI) != (player1Ai || player2Ai))
            throw new IllegalArgumentException("Spelmodus en spelertypen komen niet overeen");

        char startingMarker = switch (startingPlayer) {
            case 0 -> ThreadLocalRandom.current().nextBoolean() ? Board.PLAYER_X_MARKER : Board.PLAYER_0_MARKER;
            case 1 -> Board.PLAYER_X_MARKER;
            case 2 -> Board.PLAYER_0_MARKER;
            default -> throw new IllegalArgumentException("Ongeldige beginspeler");
        };
        Player playerX = createPlayer(name1, player1Ai);
        Player player0 = createPlayer(name2, player2Ai);
        TicTacToeGame game = new TicTacToeGame(playerX, player0);
        game.setStartingPlayer(startingMarker);
        GameSession session = startSession("Tic-Tac-Toe", game, mode, playerX, player0);
        playAiTurnIfNeeded(session);
        return session;
    }

    /**
     * maakt een lokale speler of AI-speler aan
     *
     * De AIPlayer krijgt een Tic-Tac-Toe-strategie
     * De strategie berekent de zet
     * de GameService verwerkt de zet
     */
    private Player createPlayer(String name, boolean isAi) {

        if (!isAi) {return new LocalPlayer(name);}

        return new AIPlayer(name, new TicTacToeMinimaxStrategy());
    }

    /**
     * maakt een defensieve speltoestand voor de AI
     * de strategie krijgt geen verwijzing naar de echte game
     * en krijg alleen een kopie van wat de AI echt nodig heeft om een zet te berekenen
     */
    private TicTacToeTurnState snapshotOf(TicTacToeGame game) {
        return new TicTacToeTurnState(game.getBoard(), game.getCurrentMarker(), game.getValidMoves());
    }

    // Oude aanroep blijft werken: standaard bestuur je deze client zelf.
    public synchronized GameSession startOnlineTicTacToe(String local, String opponent, String starting) {
        return startOnlineTicTacToe(local, opponent, starting, false);
    }

    /** Alleen de lokale kant kiest AI. De tegenstander wordt altijd via de server bestuurd. */
    public synchronized GameSession startOnlineTicTacToe(
            String local, String opponent, String starting, boolean localUsesAi) {
        ensureNoActiveSession();
        if (local == null || local.isBlank() || opponent == null || opponent.isBlank() || local.equals(opponent)
                || (!Objects.equals(starting, local) && !Objects.equals(starting, opponent)))
            throw new IllegalArgumentException("Ongeldige MATCH-gegevens");
        Player localPlayer = createPlayer(local, localUsesAi);
        Player remotePlayer = new RemotePlayer(opponent);
        // Protocolafspraak controleren: PLAYERTOMOVE start met X in dit ontwerp.
        Player x = starting.equals(local) ? localPlayer : remotePlayer;
        Player zero = starting.equals(local) ? remotePlayer : localPlayer;
        TicTacToeGame game = new TicTacToeGame(x, zero);
        return startSession("Tic-Tac-Toe", game, GameMode.ONLINE, x, zero);
    }

    private GameSession startSession(String name, Game game, GameMode mode, Player playerX, Player player0) {
        game.start();
        currentSession = new GameSession(name, mode, playerX, player0, game);
        return currentSession;
    }

    public synchronized boolean makeMove(int number) {
        GameSession session = requireActiveSession();
        if (session.getGameMode() == GameMode.ONLINE) throw new IllegalStateException("Online zetten gaan via de server");
        if (!(session.getGame() instanceof TicTacToeGame game)) throw new IllegalStateException("Geen Tic-Tac-Toe game");
        if (game.getCurrentPlayer() instanceof AIPlayer) throw new IllegalStateException("AI is aan de beurt");
        if (!applyMove(game, number)) return false;
        playAiTurnIfNeeded(session);
        return true;
    }

    public synchronized void applyConfirmedOnlineMove(String playerName, int number) {
        GameSession session = requireActiveSession();
        if (session.getGameMode() != GameMode.ONLINE || !(session.getGame() instanceof TicTacToeGame game))
            throw new IllegalStateException("Geen online wedstrijd actief");
        if (playerName == null || !game.getCurrentPlayer().getName().equals(playerName))
            throw new IllegalStateException("MOVE komt niet van speler aan de beurt");
        if (!applyMove(game, number)) throw new IllegalStateException("Ongeldige serverzet: " + number);
    }

    /**
     * dit laat de lokale AI tijdens een online wedstrijd een zet berekenen
     *
     * Anders dan bij offline spelen wordt de zet hier niet gelijk op het bord geplaatst
     *
     * De AI berekent via askForMove() een zet en levert deze via MoveContext.submitMove() aan
     *
     * Daarna geeft deze methode het gekozen vaknummer terug
     * OnlineGameCoordinator kan het vaknummer naar de server sturen
     *
     * Pas alleen wanneer de server een MOVE-event terugstuurt, wordt de zet op het echte bord toegepast
     */
    public synchronized int chooseOnlineAiMove() {
        GameSession session = requireActiveSession();
        if (session.getGameMode() != GameMode.ONLINE
                || !(session.getGame() instanceof TicTacToeGame game)
                || !(game.getCurrentPlayer() instanceof AIPlayer ai)) {
            throw new IllegalStateException("Geen lokale online-AI aan de beurt");
        }

        final Move[] suggestion = new Move[1];
        ai.askForMove(new MoveContext() {
            @Override public GameState getGameState() { return snapshotOf(game); }
            @Override public void submitMove(Move move) {
                if (suggestion[0] != null) throw new IllegalStateException("Meerdere AI-zetten ontvangen");
                suggestion[0] = Objects.requireNonNull(move);
            }
        });

        if (!(suggestion[0] instanceof TicTacToeMove chosen)
                || !game.getValidMoves().contains(chosen.index())) {
            throw new IllegalStateException("AI koos een ongeldige online-zet");
        }
        // Let op: GEEN applyMove. Alleen MOVE van de server wijzigt het bord.
        return chosen.index();
    }

    private boolean applyMove(TicTacToeGame game, int number) {
        if (!game.placeMarker(number)) return false;
        if (game.isGameOver()) game.finish();
        else game.changeTurn();
        return true;
    }

    private void playAiTurnIfNeeded(GameSession session) {
        if (!session.isActive() || !(session.getGame() instanceof TicTacToeGame game)
                || !(game.getCurrentPlayer() instanceof AIPlayer ai)) return;
        MoveContext context = new MoveContext() {
            @Override public GameState getGameState() { return snapshotOf(game); }
            @Override public void submitMove(Move move) {
                if (!session.isActive() || game.getCurrentPlayer() != ai)
                    throw new IllegalStateException("AI is niet aan de beurt");
                if (!(move instanceof TicTacToeMove m) || !applyMove(game, m.index()))
                    throw new IllegalStateException("AI gaf ongeldige zet");
            }
        };
        ai.askForMove(context);
    }

    private void ensureNoActiveSession() {
        if (currentSession != null && currentSession.isActive()) throw new IllegalStateException("Er is al een wedstrijd actief");
    }
    private GameSession requireActiveSession() {
        if (currentSession == null || !currentSession.isActive()) throw new IllegalStateException("Geen actieve wedstrijd");
        return currentSession;
    }
    public synchronized GameSession getCurrentSession() { return currentSession; }
    public synchronized void finishCurrentSession() {
        if (currentSession == null) throw new IllegalStateException("Geen wedstrijd");
        if (currentSession.isActive()) currentSession.getGame().finish();
    }
    public synchronized void clearCurrentSession() {
        if (currentSession != null && currentSession.isActive()) throw new IllegalStateException("Wedstrijd nog actief");
        currentSession = null;
    }
}

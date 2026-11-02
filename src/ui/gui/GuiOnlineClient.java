package ui.gui;

import javafx.application.Platform;
import model.Board;
import model.GameMode;
import model.TicTacToeGame;
import network.*;
import online.OnlineGameCoordinator;
import service.GameService;
import service.GameSession;

import java.util.Objects;

public final class GuiOnlineClient {
    private final GameService service;
    private Client client;
    private OnlineGameCoordinator match;
    private OnlineGameView view;
    private Runnable onMatchStarted;
    private String nickname;
    private boolean localAi;
    private boolean tournamentMode;

    public GuiOnlineClient(GameService service) {
        this.service = Objects.requireNonNull(service);
    }

    public Client getClient() { return client; }
    public boolean isLocalAi() { return localAi; }
    public void setLocalAi(boolean localAi) { this.localAi = localAi; }
    public boolean isTournamentMode() { return tournamentMode; }
    public void setTournamentMode(boolean tournamentMode) { this.tournamentMode = tournamentMode; }
    public void setOnMatchStarted(Runnable action) {this.onMatchStarted = action;}

    public void attachView(OnlineGameView view) {
        this.view = view;
        refreshView();
    }

    public void detachView() {
        this.view = null;
    }

    /** Call na login , voordat je kunt subscriben. */
    public void attach(Client attempt, String nickname) {
        if (client == attempt) return;

        this.client = Objects.requireNonNull(attempt);
        this.nickname = Objects.requireNonNull(nickname);
        this.match = new OnlineGameCoordinator(attempt, service, nickname);

        attempt.addGameListener(new GameListener() {
            @Override
            public void onMatch(String opponent, String gameType, String firstPlayer) {
                ui(attempt, () -> {
                    if (match == null) return;
                    match.onMatch(opponent, gameType, firstPlayer, localAi);
                    if (onMatchStarted != null) onMatchStarted.run();
                });
            }

            @Override
            public void onYourTurn() {
                ui(attempt, () -> {
                    if (match != null) {
                        match.onYourTurn();
                        refreshView();
                    }
                });
            }

            @Override
            public void onMove(String player, int move) {
                ui(attempt, () -> {
                    if (match != null) {
                        match.onMove(player, move);
                        refreshView();
                    }
                });
            }

            @Override
            public void onResult(ResultType type, String comment) {
                ui(attempt, () -> {
                    if (match == null) return;
                    match.onResult(type, comment);

                    if (view != null) {
                        view.showResult(type, comment);
                    } else {
                        // Speler disconnected/forfeit dus we gaan stoppen met spelen.
                        clearFinishedMatch();
                    }
                });
            }
        });

        attempt.addConnectionListener(new ConnectionListener() {
            @Override
            public void onError(String reason) {
                ui(attempt, () -> {
                    if (match != null && match.isAwaitingConfirmation()) {
                        match.onCommandRejected();
                        refreshView();
                    }
                });
            }

            @Override
            public void onDisconnect() {
                ui(attempt, () -> {
                    clearOnlineSession();
                    client = null;
                    match = null;
                    view = null;
                });
            }
        });
    }

    public boolean requestLocalMove(int pos) {
        return match != null && match.requestLocalMove(pos);
    }

    public void forfeit() {
        if (match != null) match.forfeit();
    }

    public boolean isYourTurn() {
        return match != null && match.isYourTurn();
    }

    public boolean isAwaitingConfirmation() {
        return match != null && match.isAwaitingConfirmation();
    }

    public boolean hasGivenUp() {
        return match != null && match.hasGivenUp();
    }

    /** Lees het bord van bestaande gamemodel. */
    public String symbolAt(int pos) {
        if (pos < 0 || pos >= Board.SIZE * Board.SIZE) return "";

        GameSession session = service.getCurrentSession();
        if (session == null || !(session.getGame() instanceof TicTacToeGame game)) {
            return "";
        }

        char marker = game.getBoard()[pos / Board.SIZE][pos % Board.SIZE];
        return marker == Board.EMPTY_MARKER ? "" : String.valueOf(marker);
    }

    /** Alleen clear als de resultaat de match stopt.*/
    public void clearFinishedMatch() {
        GameSession session = service.getCurrentSession();
        if (session != null && session.getGameMode() == GameMode.ONLINE) {
            if (session.isActive()) return;
            service.clearCurrentSession();
        }

        match = client != null && nickname != null
                ? new OnlineGameCoordinator(client, service, nickname)
                : null;
    }

    public void disconnect() {
        Client oldClient = client;
        client = null;

        if (oldClient != null && oldClient.isConnected()) {
            oldClient.disconnect();
        }

        clearOnlineSession();
        match = null;
        view = null;
    }

    private void clearOnlineSession() {
        GameSession session = service.getCurrentSession();
        if (session != null && session.getGameMode() == GameMode.ONLINE) {
            if (session.isActive()) service.finishCurrentSession();
            service.clearCurrentSession();
        }
    }

    private void refreshView() {
        if (view != null) view.refresh();
    }

    private void ui(Client source, Runnable action) {
        Platform.runLater(() -> {
            if (client == source) action.run();
        });
    }

}

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
import java.util.function.Consumer;

public final class GuiOnlineClient {
    private final GameService service;
    private Client client;
    private OnlineGameCoordinator match;
    private OnlineGameView view;
    private Runnable onMatchStarted;
    private String nickname;
    private boolean localAi;
    private Consumer<String> onServerError;
    private Consumer<String> onDisconnected;
    private Runnable onSubscriptionConfirmed;
    private String pendingSubscription;
    private boolean subscriptionPending;
    private boolean subscribedTicTacToe;

    public GuiOnlineClient(GameService service) {
        this.service = Objects.requireNonNull(service);
    }

    public Client getClient() { return client; }
    public boolean isLocalAi() { return localAi; }
    public void setLocalAi(boolean localAi) { this.localAi = localAi; }
    public void setOnMatchStarted(Runnable action) {this.onMatchStarted = action;}
    public void setOnServerError(Consumer<String> action) { this.onServerError = action; }
    public void setOnDisconnected(Consumer<String> action) { this.onDisconnected = action; }
    public void setOnSubscriptionConfirmed(Runnable action) { this.onSubscriptionConfirmed = action; }

    public boolean subscribeTicTacToe() {
        if (client == null || !client.isConnected() || subscriptionPending || subscribedTicTacToe) return false;
        pendingSubscription = "subscribe tic-tac-toe";
        subscriptionPending = true;
        try {
            client.subscribe("tic-tac-toe");
            return true;
        } catch (RuntimeException e) {
            pendingSubscription = null;
            subscriptionPending = false;
            throw e;
        }
    }

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
            public void onCommandOk(String command) {
                ui(attempt, () -> {
                    if (!command.equals(pendingSubscription)) return;
                    pendingSubscription = null;
                    subscriptionPending = false;
                    subscribedTicTacToe = true;
                    if (onSubscriptionConfirmed != null) onSubscriptionConfirmed.run();
                });
            }

            @Override
            public void onCommandError(String command, String reason) {
                handleServerError(attempt, command, reason);
            }

            @Override
            public void onError(String reason) {
                handleServerError(attempt, null, reason);
            }

            @Override
            public void onDisconnect() {
                ui(attempt, () -> {
                    clearOnlineSession();
                    client = null;
                    match = null;
                    view = null;
                    pendingSubscription = null;
                    subscriptionPending = false;
                    subscribedTicTacToe = false;
                    if (onDisconnected != null) onDisconnected.accept("The connection to the server was lost.");
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
        pendingSubscription = null;
        subscriptionPending = false;
        subscribedTicTacToe = false;

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

    private void handleServerError(Client source, String command, String reason) {
        ui(source, () -> {
            if (command != null && command.equals(pendingSubscription)) {
                pendingSubscription = null;
                subscriptionPending = false;
            }
            if (onServerError != null) onServerError.accept(reason);
            if (match != null && match.isAwaitingConfirmation()
                    && (command == null || command.startsWith("choose "))) {
                match.onCommandRejected();
                refreshView();
            }
            if (match != null && "forfeit".equals(command)) {
                match.onForfeitRejected();
                refreshView();
            }
        });
    }

    private void ui(Client source, Runnable action) {
        Platform.runLater(() -> {
            if (client == source) action.run();
        });
    }

}

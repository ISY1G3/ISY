package ui.tui;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import network.*;
import online.OnlineGameCoordinator;
import service.GameService;
import service.GameSession;
import model.GameMode;

/** Eén plek voor clientlevenscyclus, lobbydata en netwerkcallbacks. */
public final class TuiOnlineClient {
    private final TuiNavigator navigator;
    private final GameService service;
    private final Consumer<Runnable> uiDispatcher;
    private Client client;
    private OnlineGameCoordinator match;
    private LobbyData lobby;
    private boolean loginPending;
    private String pendingSubscription;

    public TuiOnlineClient(TuiNavigator navigator, GameService service, Consumer<Runnable> dispatcher) {
        this.navigator = Objects.requireNonNull(navigator);
        this.service = Objects.requireNonNull(service);
        this.uiDispatcher = Objects.requireNonNull(dispatcher);
    }
    public LobbyData getLobby() { return lobby; }
    public OnlineGameCoordinator getMatch() { return match; }
    public boolean isConnected() { return client != null && client.isConnected() && !loginPending && lobby != null; }

    public void connect(String hostname, int port, String nickname) {
        if (hostname == null || hostname.isBlank() || nickname == null || nickname.isBlank()) {
            System.out.println("Server en nickname zijn verplicht");
            return;
        }
        disconnect();
        Client attempt = new Client();
        client = attempt;
        loginPending = true;
        match = new OnlineGameCoordinator(attempt, service, nickname);

        attempt.addConnectionListener(new ConnectionListener() {
            @Override public void onLoginOk() { dispatch(attempt, () -> {
                if (!loginPending) return;
                loginPending = false;
                lobby = new LobbyData(nickname);
                navigator.showLobby();
                refreshLobby();
            }); }
            @Override public void onCommandOk(String command) { dispatch(attempt, () -> {
                if (!command.equals(pendingSubscription)) return;
                pendingSubscription = null;
                if (lobby != null) {
                    lobby.setSubscriptionPending(false);
                    lobby.setSubscribed(true);
                    redrawLobbyIfVisible();
                }
            }); }
            @Override public void onCommandError(String command, String reason) {
                handleServerError(attempt, command, reason);
            }
            @Override public void onError(String reason) {
                handleServerError(attempt, null, reason);
            }
            @Override public void onDisconnect() { dispatch(attempt, () -> {
                System.out.println("Verbinding met server verbroken");
                GameSession s = service.getCurrentSession();
                if (s != null && s.getGameMode() == GameMode.ONLINE) {
                    service.finishCurrentSession();
                    service.clearCurrentSession();
                }
                client = null;
                match = null;
                lobby = null;
                loginPending = false;
                pendingSubscription = null;
                navigator.showLogin();
            }); }
        });
        attempt.addLobbyListener(new LobbyListener() {
            @Override public void onGameList(List<String> games) { dispatch(attempt, () -> {
                if (lobby == null) return;
                lobby.setGames(games);
                // Niet automatisch aanmelden: eerst zelf Mens of AI kunnen kiezen.
                redrawLobbyIfVisible();
            }); }
            @Override public void onPlayerList(List<String> players) { dispatch(attempt, () -> {
                if (lobby != null) { lobby.setPlayers(players); redrawLobbyIfVisible(); }
            }); }
            @Override public void onChallenge(String challenger, String game, int nr) { dispatch(attempt, () -> {
                if (lobby != null) { lobby.addChallenge(challenger, game, nr); redrawLobbyIfVisible(); }
            }); }
            @Override public void onChallengeCancelled(int nr) { dispatch(attempt, () -> {
                if (lobby != null) { lobby.removeChallenge(nr); redrawLobbyIfVisible(); }
            }); }
        });
        attempt.addGameListener(new GameListener() {
            @Override public void onMatch(String opponent, String gameType, String firstPlayer) {
                dispatch(attempt, () -> {
                    match.onMatch(opponent, gameType, firstPlayer, lobby != null && lobby.isLocalAi());
                    navigator.showOnlineGame();
                });
            }
            @Override public void onYourTurn() { dispatch(attempt, () -> {
                if (match != null) { match.onYourTurn(); navigator.redrawIfOnlineGame(); }
            }); }
            @Override public void onMove(String player, int move) { dispatch(attempt, () -> {
                if (match != null) { match.onMove(player, move); navigator.redrawIfOnlineGame(); }
            }); }
            @Override public void onResult(ResultType type, String comment) { dispatch(attempt, () -> {
                if (match != null) {
                    match.onResult(type, comment);
                    navigator.showOnlineResult(type, comment);
                }
            }); }
        });
        try {
            attempt.connect(hostname, port);
            attempt.login(nickname);
            System.out.println("Verbonden, wachten op loginbevestiging...");
        } catch (IOException | RuntimeException e) {
            System.out.println("Verbinding mislukt: " + e.getMessage());
            disconnect();
        }
    }
    /**
     * Zet een ontvangen serverevent in de TUI-Queue
     *
     * Netwerkberichten worden op een aparte thread ontvangen
     * We willen voorkomen dat die thread rechtstreeks de schermnavigatie of de GameSession verandert
     * Daarom wordt de actie later op de TUI-eventthread uitgevoerd
     *
     * De controle client == source voorkomt dat
     * oude serverberichten van een afgesloten verbinding
     * alsnog invloed hebben op een nieuwe verbinding
     */
    private void dispatch(Client source, Runnable action) {
        uiDispatcher.accept(() -> { if (client == source) action.run(); });
    }

    private void handleServerError(Client source, String command, String reason) {
        dispatch(source, () -> {
            System.out.println("Waarschuwing van server: " + reason);
            if (loginPending) {
                disconnect();
                navigator.showLogin();
            } else if (command != null && command.equals(pendingSubscription)) {
                pendingSubscription = null;
                if (lobby != null) {
                    lobby.setSubscriptionPending(false);
                    redrawLobbyIfVisible();
                }
            } else if (match != null && (command == null || command.startsWith("move "))) {
                match.onCommandRejected();
            }
        });
    }
    private void redrawLobbyIfVisible() {
        if (navigator.isLobbyVisible()) navigator.drawCurrentView();
    }
    public void refreshLobby() {
        if (!isConnected()) return;
        client.getGameList();
        client.getPlayerList();
        System.out.println("Spelerslijst en games opgevraagd");
    }
    public void subscribeTicTacToe() {
        if (!isConnected() || lobby.isSubscribedTicTacToe() || pendingSubscription != null) return;
        String game = lobby.getTicTacToeServerName();
        if (game == null) { System.out.println("Tic-Tac-Toe is niet gevonden in de gamelijst"); return; }
        pendingSubscription = "subscribe " + game;
        lobby.setSubscriptionPending(true);
        try {
            client.subscribe(game);
        } catch (RuntimeException e) {
            pendingSubscription = null;
            lobby.setSubscriptionPending(false);
            System.out.println("Aanmelden versturen mislukt: " + e.getMessage());
        }
        redrawLobbyIfVisible();
    }
    public void challenge(String opponent) {
        if (!isConnected() || !lobby.isSubscribedTicTacToe()) { System.out.println("Niet aangemeld"); return; }
        client.challenge(opponent, lobby.getTicTacToeServerName());
        System.out.println("Uitdaging verstuurd naar " + opponent);
    }
    public void acceptChallenge(int nr) {
        if (!isConnected() || !lobby.hasChallenge(nr)) return;
        client.accept(nr);
        lobby.removeChallenge(nr);
        redrawLobbyIfVisible();
    }
    public boolean requestLocalMove(int pos) { return match != null && match.requestLocalMove(pos); }
    public void forfeit() { if (match != null) match.forfeit(); }
    public void clearFinishedMatch() {
        GameSession s = service.getCurrentSession();
        if (s != null && s.getGameMode() == GameMode.ONLINE) service.clearCurrentSession();
        match = isConnected() ? new OnlineGameCoordinator(client, service, lobby.getNickname()) : null;
    }
    public void disconnect() {
        Client old = client;
        client = null; // Achtergebleven callbacks van deze client worden genegeerd.
        loginPending = false;
        pendingSubscription = null;
        if (old != null) {
            try { if (old.isConnected()) old.disconnect(); } catch (RuntimeException ignored) {}
        }
        GameSession s = service.getCurrentSession();
        if (s != null && s.getGameMode() == GameMode.ONLINE) {
            service.finishCurrentSession();
            service.clearCurrentSession();
        }
        lobby = null;
        match = null;
    }
}

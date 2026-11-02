package ui.tui;

import model.GameMode;
import java.util.Objects;
import java.util.function.Consumer;
import network.ResultType;
import service.GameService;
import service.GameSession;
import ui.tui.controller.*;
import ui.tui.view.*;

/** Koppelt precies 1 actieve view aan 1 controller. */
public class TuiNavigator {
    private TuiView currentView;
    private TuiController currentController;
    private boolean running = true;
    private final LoginData loginData = new LoginData();
    private final GameService gameService;
    private final TuiOnlineClient online;

    public TuiNavigator(GameService gameService, Consumer<Runnable> dispatcher) {
        this.gameService = Objects.requireNonNull(gameService);
        this.online = new TuiOnlineClient(this, gameService, dispatcher);
    }
    public TuiOnlineClient getOnline() { return online; }
    private void show(TuiView view, TuiController controller) {
        currentView = view;
        currentController = controller;
        drawCurrentView();
    }
    public void showMainMenu() { show(new MainMenuView(), new MainMenuController(this)); }
    public void showGameSelect() { show(new GameSelectView(), new GameSelectController(this)); }
    public void showLocalSetup(String name) { showLocalSetup(new LocalSetupData(name)); }
    public void showLocalSetup(LocalSetupData data) {
        LocalSetupView view = new LocalSetupView(data);
        show(view, new LocalSetupController(this, data, view, gameService));
    }
    public void showLogin() {
        LoginView view = new LoginView(loginData);
        show(view, new LoginController(this, loginData, view));
    }
    public void showLobby() {
        LobbyData data = online.getLobby();
        if (data == null) { showLogin(); return; }
        showLobby(data);
    }
    public void showLobby(LobbyData data) {
        LobbyView view = new LobbyView(data);
        show(view, new LobbyController(this, data, view));
    }
    public void showLocalGame(LocalSetupData data) {
        GameSession s = gameService.getCurrentSession();
        if (s == null) throw new IllegalStateException("Geen actieve GameSession");
        show(new GameView(s), new LocalGameController(this, data, gameService));
    }
    public void showLocalResult(LocalSetupData data) {
        GameSession s = gameService.getCurrentSession();
        if (s == null) throw new IllegalStateException("Geen uitslag");
        show(new ResultView(s), new ResultController(this, data, gameService));
    }
    public void showOnlineGame() {
        GameSession s = gameService.getCurrentSession();
        if (s == null) throw new IllegalStateException("Geen online match");
        show(new GameView(s, online.getMatch()), new OnlineGameController(this));
    }
    public void showOnlineResult(ResultType type, String comment) {
        GameSession s = gameService.getCurrentSession();
        if (s == null) throw new IllegalStateException("Geen online matchresultaat");
        show(new OnlineResultView(s, type, comment), new OnlineResultController(this));
    }

    public boolean hasActiveSession() {
        GameSession session = gameService.getCurrentSession();
        return session != null && session.isActive();
    }

    public void returnToMainMenu(boolean confirmedAbandon) {

        GameSession session = gameService.getCurrentSession();

        if (session != null && session.isActive()) {

            if (!confirmedAbandon) {
                System.out.println(
                        "Bevestig eerst het verlaten van de wedstrijd."
                );
                return;
            }

            if (session.getGameMode() == GameMode.ONLINE) {
                try {
                    online.forfeit();
                } catch (RuntimeException e) {
                    System.out.println(
                            "Opgave kon niet worden verstuurd: "
                                    + e.getMessage()
                    );
                }
            } else {
                gameService.finishCurrentSession();
            }
        }

        online.disconnect();

        session = gameService.getCurrentSession();

        if (session != null) {
            if (session.isActive()) {
                gameService.finishCurrentSession();
            }
            gameService.clearCurrentSession();
        }

        showMainMenu();
    }

    public boolean isLobbyVisible() { return currentView instanceof LobbyView; }
    public void redrawIfOnlineGame() { if (currentController instanceof OnlineGameController) drawCurrentView(); }
    public void drawCurrentView() { if (currentView != null) currentView.draw(); }
    public TuiView getCurrentView() { return currentView; }
    public TuiController getCurrentController() { return currentController; }
    public boolean isRunning() { return running; }
    public void stop() { running = false; }
}

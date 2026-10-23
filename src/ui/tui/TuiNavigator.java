package ui.tui;

import ui.tui.controller.*;
import ui.tui.view.*;
import service.*;

// Koppelt steeds 1 actieve view aan 1 controller en print de view
public class TuiNavigator {
    private TuiView currentView;
    private TuiController currentController;
    private boolean running = true;
    private final LoginData loginData = new LoginData();

    private final GameService gameService;                  // de gedeelde gameservice die in TuiApplication is aangemaakt

    public TuiNavigator(GameService gameService){
        // bewaar de gegeven GameService
        this.gameService = gameService;     //bewaart een verwijzing naar hetzelfde object dat in TuiApplication is gemaakt
    }

    // Bewaar view en controller samen hierdoor gaat de input nooit naar het verkeerde scherm
    private void show(TuiView view, TuiController controller) {
        currentView = view;
        currentController = controller;
        drawCurrentView();
    }

    public void showMainMenu() {
        show(new MainMenuView(), new MainMenuController(this));
    }

    public void showGameSelect() {
        show(new GameSelectView(), new GameSelectController(this));
    }

    // maak een nieuwe lokale setup voor het spel dat gekozen is
    public void showLocalSetup(String gameName) {
        showLocalSetup(new LocalSetupData(gameName));
    }

    // Bestaande keuzes bewaren bij terugnavigeren vanaf game of resultaat
    public void showLocalSetup(LocalSetupData data) {
        LocalSetupView view = new LocalSetupView(data);
        show(view, new LocalSetupController(this, data, view, gameService));
    }

    public void showLogin() {
        LoginView view = new LoginView(loginData);
        show(view, new LoginController(this, loginData, view));
    }

    // DEMO: zonder verbinding met de server.
    public void showLobby(String nickname) {
        showLobby(new LobbyData(nickname));
    }

    public void showLobby(LobbyData data) {
        LobbyView view = new LobbyView(data);
        show(view, new LobbyController(this, data, view));
    }

    public void showSettings() {
        show(new SettingsView(), new SettingsController(this));
    }

    public void showLocalGame(LocalSetupData data) {
        GameSession session = gameService.getCurrentSession();          // vraagt huidige sessie op

        if (session == null) {      //zonder sessie tonen we geen bord
            throw new IllegalStateException("er is geen actieve GameSession");
        }
        // dan maken we een view met de game
        GameView view = new GameView(session);
        // koppelen van de view aan de bestaande controller
        show(view, new LocalGameController(this, data, gameService));
    }

    //dit is nog echt met demo data gedaan uit LobbyData
    public void showOnlineGameDemo(LobbyData data) {
        show(new GameView("Tic-Tac-Toe", data.getNickname(), "Tegenstander (demo)"),
                new GameDemoController(this, data));
    }

    public void showLocalResult(LocalSetupData data) {
        GameSession session = gameService.getCurrentSession();
        if (session == null) throw new IllegalStateException("er is geen uitslag");
        show(new ResultView(session), new ResultController(this, data, gameService));
    }

    public void showTournament(LobbyData data) {
        show(new TournamentView(), new TournamentController(this, data));
    }

    public void drawCurrentView() {
        if (currentView != null) currentView.draw();
    }

    public TuiView getCurrentView() { return currentView; }
    public TuiController getCurrentController() { return currentController; }
    public boolean isRunning() { return running; }
    public void stop() { running = false; }
}

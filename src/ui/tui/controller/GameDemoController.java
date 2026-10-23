package ui.tui.controller;

import ui.tui.LobbyData;
import ui.tui.TuiNavigator;

//ONLINE
// Tijdelijke controller, later vervangen door acties naar de GameService
public class GameDemoController implements TuiController {
    private final TuiNavigator navigator;
    private final LobbyData lobbyData;

    public GameDemoController(TuiNavigator navigator, LobbyData lobbyData) {
        this.navigator = navigator;
        this.lobbyData = lobbyData;
    }

    @Override
    public void handleInput(String input) {
        if (input.equals("0")) {
                navigator.showLobby(lobbyData);
        }
        else {
            System.out.println("voor Online DEMO: Zetten zijn nog niet mogelijk.");
        }
    }
}
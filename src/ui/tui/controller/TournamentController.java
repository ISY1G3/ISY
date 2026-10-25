package ui.tui.controller;

import ui.tui.TuiNavigator;
import ui.tui.LobbyData;

public class TournamentController implements TuiController {
    private final TuiNavigator navigator;
    private final LobbyData data;
    public TournamentController(TuiNavigator navigator, LobbyData data) {
        this.navigator = navigator;
        this.data = data;
    }
    @Override
    public void handleInput(String input) {
        if (input.equals("0")) navigator.showLobby(data);
        else System.out.println("Kies 0 om terug te gaan.");
    }
}

package ui.tui.controller;

import ui.tui.TuiNavigator;

public final class OnlineResultController implements TuiController {
    private final TuiNavigator navigator;
    public OnlineResultController(TuiNavigator navigator) { this.navigator = navigator; }
    @Override public void handleInput(String input) {
        if (!input.equals("0")) { System.out.println("Kies 0 om terug te gaan"); return; }
        navigator.getOnline().clearFinishedMatch();
        if (navigator.getOnline().isConnected()) {
            navigator.showLobby();
            navigator.getOnline().refreshLobby();
        } else navigator.showLogin();
    }
}

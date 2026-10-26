package ui.tui.controller;

import ui.tui.LobbyData;
import ui.tui.TuiNavigator;
import ui.tui.view.LobbyView;

// Handelt demonstratiekeuzes af  verstuurt nog geen servercommando's
public class LobbyController implements TuiController {
    private final TuiNavigator navigator;
    private final LobbyData data;
    private final LobbyView view;

    public LobbyController(TuiNavigator navigator, LobbyData data, LobbyView view) {
        this.navigator = navigator;
        this.data = data;
        this.view = view;
    }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1": System.out.println("DEMO: Er zijn geen echte uitdagingen geladen."); break;
            case "2": System.out.println("DEMO: Uitdagen werkt pas na serverkoppeling."); break;
            case "3": data.toggleSubscribedTicTacToe(); navigator.drawCurrentView(); break;
            case "4": navigator.showTournament(data); break;
            case "5": navigator.showOnlineGameDemo(data); break;
            case "0": navigator.showLogin(); break;
            default: System.out.println("Ongeldige keuze.");
        }
    }
}

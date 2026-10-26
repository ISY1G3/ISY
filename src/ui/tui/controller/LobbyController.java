package ui.tui.controller;

import ui.tui.LobbyData;
import ui.tui.TuiNavigator;
import ui.tui.view.LobbyView;

/**
 * Verwerkt invoer van de gebruiker vanuit de online lobby
 *
 * Via TuiOnlineClient kan de gebruiker spelerslijsten
 * vernieuwen, uitdagingen versturen en accepteren,
 * en zich aanmelden voor TTT
 */
public class LobbyController implements TuiController {
    private final TuiNavigator navigator;
    private final LobbyData data;
    private int awaiting;       //2 = naam van de tegenstander, 3 = challenge nummer
    public LobbyController(TuiNavigator navigator, LobbyData data, LobbyView view) {
        this.navigator = navigator;
        this.data = data;
    }

    @Override
    public void handleInput(String input) {
        if (awaiting != 0) {
            int type = awaiting;
            if (input.equalsIgnoreCase("back")) { awaiting = 0; navigator.drawCurrentView(); return; }
            if (type == 2) {
                if (input.isBlank() || input.equals(data.getNickname())) {
                    System.out.println("Voer een andere spelersnaam in (of back)"); return;
                }
                if (!data.isSubscribedTicTacToe()) { System.out.println("Meld je eerst aan voor Tic-Tac-Toe"); return; }
                navigator.getOnline().challenge(input);
                awaiting = 0;
            } else {
                try {
                    int number = Integer.parseInt(input);
                    if (!data.hasChallenge(number)) { System.out.println("Onbekend challenge-nummer"); return; }
                    navigator.getOnline().acceptChallenge(number);
                    awaiting = 0;
                } catch (NumberFormatException e) { System.out.println("Voer een geldig nummer in"); }
            }
            return;
        }
        switch (input) {
            case "1" -> navigator.getOnline().refreshLobby();
            case "2" -> { awaiting = 2; System.out.println("Welke speler wil je uitdagen? (of back)"); }
            case "3" -> { awaiting = 3; System.out.println("Welk challenge-nummer accepteer je? (of back)"); }
            case "4" -> navigator.getOnline().subscribeTicTacToe();
            case "5" -> { data.toggleLocalAi(); navigator.drawCurrentView(); }
            case "6" -> navigator.showTournament(data);
            case "0" -> { navigator.getOnline().disconnect(); navigator.showLogin(); }
            default -> System.out.println("Ongeldige keuze");
        }
    }
}

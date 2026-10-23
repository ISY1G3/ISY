package ui.tui.controller;

import ui.tui.LoginData;
import ui.tui.TuiNavigator;
import ui.tui.view.LoginView;

/** Verwerkt login-formuliervelden; echte login komt later via Client/GameService. */
public class LoginController implements TuiController {
    private final TuiNavigator navigator;
    private final LoginData data;
    private final LoginView view;
    private int waitingFor = 0; // 1 = server, 2 = bijnaam

    public LoginController(TuiNavigator navigator, LoginData data, LoginView view) {
        this.navigator = navigator;
        this.data = data;
        this.view = view;
    }

    @Override
    public void handleInput(String input) {
        if (waitingFor != 0) {
            if (input.equalsIgnoreCase("back")) {
                waitingFor = 0;
                navigator.drawCurrentView();
            } else if (input.isEmpty() || input.length() > 100) {
                System.out.println("Waarde moet 1 t/m 100 tekens bevatten.");
                view.askValue(waitingFor == 1 ? "server" : "nickname");
            } else {
                if (waitingFor == 1) data.setHostname(input);
                else data.setNickname(input);
                waitingFor = 0;
                navigator.drawCurrentView();
            }
            return;
        }

        switch (input) {
            case "1": waitingFor = 1; view.askValue("server"); break;
            case "2": waitingFor = 2; view.askValue("nickname"); break;
            case "3": navigator.showLobby(data.getNickname()); break;
            case "0": navigator.showMainMenu(); break;
            default: System.out.println("Ongeldige keuze.");
        }
    }
}


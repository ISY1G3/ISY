package ui.tui.controller;

import ui.tui.LoginData;
import ui.tui.TuiNavigator;
import ui.tui.view.LoginView;

public class LoginController implements TuiController {
    private final TuiNavigator navigator;
    private final LoginData data;
    private final LoginView view;
    private int waitingFor;

    public LoginController(TuiNavigator navigator, LoginData data, LoginView view) {
        this.navigator = navigator;
        this.data = data;
        this.view = view;
    }

    @Override public void handleInput(String input) {
        if (waitingFor != 0) {
            int field = waitingFor;
            if (input.equalsIgnoreCase("back")) {
                waitingFor = 0;
                navigator.drawCurrentView();
                return;
            }
            if (input.isBlank() || input.length() > 100) {
                System.out.println("Waarde moet 1 t/m 100 tekens bevatten");
                return;
            }
            if (field == 3) {
                try {
                    int port = Integer.parseInt(input);
                    if (port < 1 || port > 65535) throw new NumberFormatException();
                    data.setPort(port);
                } catch (NumberFormatException e) {
                    System.out.println("Poort moet tussen 1 en 65535 liggen");
                    return;
                }
            } else if (field == 1) data.setHostname(input);
            else data.setNickname(input);
            waitingFor = 0;
            navigator.drawCurrentView();
            return;
        }
        switch (input) {
            case "1", "2", "3" -> {
                waitingFor = Integer.parseInt(input);
                view.askValue(waitingFor == 1 ? "server" : waitingFor == 2 ? "nickname" : "poort");
            }
            case "4" -> navigator.getOnline().connect(data.getHostname(), data.getPort(), data.getNickname());
            case "0" -> navigator.showMainMenu();
            default -> System.out.println("Ongeldige keuze");
        }
    }
}


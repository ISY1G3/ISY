package ui.tui.controller;

import ui.tui.TuiNavigator;

// navigeert naar de setup van gekozen spel
public class GameSelectController implements TuiController {
    private final TuiNavigator navigator;
    public GameSelectController(TuiNavigator navigator) { this.navigator = navigator; }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1": navigator.showLocalSetup("Tic-Tac-Toe"); break;
            case "0": navigator.showMainMenu(); break;
            default: System.out.println("Ongeldige keuze.");
        }
    }
}

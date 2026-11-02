package ui.tui.controller;

import ui.tui.TuiNavigator;

/** Zet keuzes uit het hoofdmenu om naar navigatieacties. */
public class MainMenuController implements TuiController {
    private final TuiNavigator navigator;
    public MainMenuController(TuiNavigator navigator) { this.navigator = navigator; }

    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1": navigator.showGameSelect(); break;
            case "2": navigator.showLogin(); break;
            case "0": navigator.stop(); break;
            default: System.out.println("Ongeldige keuze. Kies 0, 1 of 2.");
        }
    }
}

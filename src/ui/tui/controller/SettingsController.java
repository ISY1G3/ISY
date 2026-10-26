package ui.tui.controller;

import ui.tui.TuiNavigator;

/** Alleen terugnavigatie: instellingen opslaan volgt later. */
public class SettingsController implements TuiController {
    private final TuiNavigator navigator;
    public SettingsController(TuiNavigator navigator) { this.navigator = navigator; }
    @Override
    public void handleInput(String input) {
        if (input.equals("0")) navigator.showMainMenu();
        else System.out.println("Kies 0 om terug te gaan.");
    }
}

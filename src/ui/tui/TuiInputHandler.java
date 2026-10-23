package ui.tui;

import ui.tui.controller.TuiController;

public class TuiInputHandler {

    private final TuiNavigator navigator;

    //instantie van de navigator om te wisselen
    public TuiInputHandler(TuiNavigator navigator) {
        this.navigator = navigator;
    }

    public void handle(String input) {

        // welke controller is er nu actief
        TuiController controller = navigator.getCurrentController();

        // Geef de invoer van player aan die^ controller
        if (controller != null) {
            controller.handleInput(input);
        }
    }
}
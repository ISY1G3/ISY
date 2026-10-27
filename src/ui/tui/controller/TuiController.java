package ui.tui.controller;

public interface TuiController {
    // Alle controllers moet invoer kunnen verwerken (handle())
    void handleInput(String input);

    default String inputHint() {return null;}
}

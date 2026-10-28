package ui.tui.controller;

import ui.tui.TuiNavigator;

public class OnlineGameController implements TuiController {
    private final TuiNavigator navigator;
    public OnlineGameController(TuiNavigator navigator) { this.navigator = navigator; }
    @Override public void handleInput(String input) {
        if (input.equals("0")) {
            System.out.println("Je geeft de wedstrijd op. Wachten op serveruitslag...");
            navigator.getOnline().forfeit();
            return;
        }
        try {
            int position = Integer.parseInt(input);
            if (position < 1 || position > 9) { System.out.println("Kies 1 t/m 9, of 0 om op te geven"); return; }
            if (navigator.getOnline().requestLocalMove(position - 1)) {
                System.out.println("Zet verzonden. Wachten op serverbevestiging...");
            } else System.out.println("Niet jouw beurt, zet in behandeling, of vakje bezet");
        } catch (NumberFormatException e) { System.out.println("Kies een nummer van 1 t/m 9 (of 0)"); }
    }
}

package ui.tui.controller;

import ui.tui.TuiNavigator;
import online.OnlineGameCoordinator;

public class OnlineGameController implements TuiController {
    private final TuiNavigator navigator;
    public OnlineGameController(TuiNavigator navigator) { this.navigator = navigator; }
    @Override
    public void handleInput(String input) {
        OnlineGameCoordinator match = navigator.getOnline().getMatch();

        // check of er een online game bestaat
        if (match == null) {
            System.out.println("Geen actieve online wedstrijd. Typ 'menu' om terug te gaan.");
            return;
        }

        // voorkom nieuwe invoer wanneer deze al is opgegeven
        if (match.hasGivenUp()) {
            System.out.println("Opgave is al verstuurd. Wacht op de uitslag of typ 'menu'.");
            return;
        }

        // Speler wil de wedstrijd opgeven.
        if (input.equals("0")) {
            try {
                navigator.getOnline().forfeit();

                System.out.println("Opgave verstuurd. Wachten op serveruitslag...");
            }
            catch (RuntimeException e) {
                System.out.println("Opgave versturen mislukt: " + e.getMessage());
            }
            return;
        }

        int position;

        // controleer of de invoer een getal is
        try {position = Integer.parseInt(input);}

        catch (NumberFormatException e) {
            System.out.println("Kies 1 t/m 9, 0 om op te geven of 'help' voor uitleg.");
            return;
        }

        // controleer het bereik van het vaknummer
        if (position < 1 || position > 9) {
            System.out.println("Kies een vakje van 1 t/m 9, of 0 om op te geven.");
            return;
        }

        // probeer de zet naar de server te sturen
        try {
            if (navigator.getOnline().requestLocalMove(position - 1)) {
                System.out.println("Zet verstuurd. Wachten op serverbevestiging...");
            }
            else {
                System.out.println("Zet niet mogelijk: verkeerde beurt, vakje bezet of verzoek in behandeling.");
            }

        }
        catch (RuntimeException e) {
            System.out.println("Zet versturen mislukt: " + e.getMessage());
        }
    }
}

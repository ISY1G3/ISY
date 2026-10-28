package ui.tui;

import ui.tui.controller.TuiController;

public class TuiInputHandler {

    private final TuiNavigator navigator;

    private boolean confirmingExit;
    private TuiController confirmationScreen;

    //instantie van de navigator om te wisselen
    public TuiInputHandler(TuiNavigator navigator) {
        this.navigator = navigator;
    }

    public void handle(String input) {

        TuiController controller =
                navigator.getCurrentController();

        if (controller == null) return;

        // Een serverevent kan ondertussen het scherm veranderen.
        if (confirmingExit && confirmationScreen != controller) {
            confirmingExit = false;
        }

        // Bevestiging wanneer iemand een wedstrijd wil verlaten.
        if (confirmingExit) {

            if (input.equalsIgnoreCase("ja")) {
                confirmingExit = false;
                navigator.returnToMainMenu(true);

            } else if (input.equalsIgnoreCase("nee")
                    || input.equalsIgnoreCase("back")
                    || input.equals("0")) {

                confirmingExit = false;
                System.out.println("Verlaten geannuleerd.");
                navigator.drawCurrentView();

            } else {
                System.out.println(
                        "Typ 'ja' om te verlaten of 'nee' om door te spelen."
                );
            }

            return;
        }

        // Overal hulp kunnen opvragen.
        if (input.equalsIgnoreCase("help")) {
            navigator.drawCurrentView();

            String hint = controller.inputHint();

            if (hint != null) {
                System.out.println(hint);
            }

            return;
        }

        // Overal naar het hoofdmenu kunnen.
        if (input.equalsIgnoreCase("menu")) {

            if (navigator.hasActiveSession()) {

                confirmingExit = true;
                confirmationScreen = controller;

                System.out.println(
                        "Je hebt nog een actieve wedstrijd. " +
                                "Teruggaan betekent de wedstrijd verlaten. " +
                                "Typ 'ja' om te bevestigen of 'nee' om te blijven."
                );

            } else {
                navigator.returnToMainMenu(false);
            }

            return;
        }

        controller.handleInput(input);
    }
}
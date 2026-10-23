package ui.tui.controller;

import service.GameService;
import ui.tui.LocalSetupData;
import ui.tui.TuiNavigator;

public class ResultController implements TuiController {
    private final TuiNavigator navigator;
    private final LocalSetupData setup;
    private final GameService gameService;
    public ResultController(TuiNavigator navigator, LocalSetupData setup, GameService gameService) {
        this.navigator = navigator;
        this.setup = setup;
        this.gameService = gameService;
    }
    @Override
    public void handleInput(String input) {
        switch (input) {
            case "1":
                gameService.clearCurrentSession();      // verwijder game die net afgelopen is
                navigator.showLocalSetup(setup);        // terug naar lokale setup
                break;
            case "0":
                gameService.clearCurrentSession();      // verwijder game die net afgelopen is
                navigator.showMainMenu();               // terug naar main scherm
                break;
            default: System.out.println("Ongeldige keuze.");
        }
    }
}


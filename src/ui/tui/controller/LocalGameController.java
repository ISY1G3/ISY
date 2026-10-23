package ui.tui.controller;

import service.GameSession;
import service.GameService;
import ui.tui.LocalSetupData;
import ui.tui.TuiNavigator;

// dit verwerkt alleen de terminalinvoer, de GameService doet alle spelregels
public class LocalGameController implements TuiController {
    private final TuiNavigator navigator;
    private final LocalSetupData setup;
    private final GameService gameService;

    public LocalGameController(TuiNavigator navigator, LocalSetupData setup, GameService gameService){
        this.navigator = navigator;
        this.setup = setup;
        this.gameService = gameService;
    }

    @Override
    public void handleInput(String input) {
        if (input.equals("0")){
            gameService.finishCurrentSession();
            gameService.clearCurrentSession();
            navigator.showLocalSetup(setup);
            return;
        }
        int boxNumber;
        try {
            boxNumber = Integer.parseInt(input) - 1;
        } catch (NumberFormatException e) {
            System.out.println("voer een vaknummer van 1 t/m 9 in of 0 om terug te gaan");
            return;
        }
        try{
            if (!gameService.makeMove(boxNumber)) {
                System.out.println("dit vakje bestaat niet of is al bezet. dus kies een ander");
                return;
            }
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());
            return;
        }

        GameSession session = gameService.getCurrentSession();
        if (!session.isActive()) {
            navigator.showLocalResult(setup);
        }
        else {
            navigator.drawCurrentView();
        }
    }
}

package ui.tui.controller;

import ui.tui.LocalSetupData;
import ui.tui.TuiNavigator;
import ui.tui.view.LocalSetupView;
import service.GameService;
import model.GameMode;

/** Verwerkt alleen instellingen. Spelregels blijven bij de GameService. */
public class LocalSetupController implements TuiController {
    private final TuiNavigator navigator;
    private final LocalSetupData data;
    private final LocalSetupView view;
    private int nameInputFor = 0; // 0 = normale menu-invoer, 1/2 = nieuwe naam
    private final GameService gameService;      //gedeelde service die games start en beheert

    public LocalSetupController(TuiNavigator navigator, LocalSetupData data,
                                LocalSetupView view, GameService gameService) {
        this.navigator = navigator;
        this.data = data;
        this.view = view;
        this.gameService = gameService;
    }

    // helper functie om te zien welke gamemode er wordt meegestuurd
    private GameMode determineGameMode() {
        //vraag eerst de speler types op van het setupscherm
        boolean player1Ai = data.isPlayer1Ai();
        boolean player2Ai = data.isPlayer2Ai();

        //dit is nog niet geimplementeerd dus geeft nu een error
        if (player1Ai && player2Ai) {
            throw new IllegalArgumentException(
                    "Twee AI-spelers worden nog niet ondersteund."
            );
        }
        //als er minimaal 1 speler ai is kiezen we player vs ai
        if (player1Ai || player2Ai) {
            return GameMode.PLAYER_VS_AI;
        }
        //anders zijn beide spelers
        return GameMode.PLAYER_VS_PLAYER;
    }

    @Override
    public void handleInput(String input) {
        // Na keuze 2/4 is de volgende regel een naam, geen menuoptie.
        if (nameInputFor != 0) {
            if (input.equalsIgnoreCase("back")) {
                nameInputFor = 0;
                navigator.drawCurrentView();
            } else if (input.isEmpty() || input.length() > 24) {
                System.out.println("Naam moet 1 t/m 24 tekens bevatten.");
                view.askName(nameInputFor);
            } else {
                if (nameInputFor == 1) data.setPlayer1Name(input);
                else data.setPlayer2Name(input);
                nameInputFor = 0;
                navigator.drawCurrentView();
            }
            return;
        }

        switch (input) {
            case "1": data.togglePlayer1Ai(); navigator.drawCurrentView(); break;
            case "2": nameInputFor = 1; view.askName(1); break;
            case "3": data.togglePlayer2Ai(); navigator.drawCurrentView(); break;
            case "4": nameInputFor = 2; view.askName(2); break;
            case "5": data.nextStartingPlayer(); navigator.drawCurrentView(); break;
            case "6":
                try{
                    GameMode mode = determineGameMode();    // bepaalt de modus op basis van de speler input

                    //start de wedstrijd met GameService
                    gameService.startTicTacToe(mode, data.getPlayer1Name(), data.getPlayer2Name(),
                            data.getStartingPlayer(), data.isPlayer1Ai(), data.isPlayer2Ai());

                    //de service heeft de game (en wanneer gekozen ook de eerste ai zet) al gestart
                    navigator.showLocalGame(data);
                } catch (IllegalArgumentException | IllegalStateException e) {
                    //waarom kan de wedstrijd niet gestart worden
                    System.out.println("kan niet starten want: " + e.getMessage());
                }
                break;

            case "0": navigator.showGameSelect(); break;
            default: System.out.println("Ongeldige keuze.");
        }
    }
}

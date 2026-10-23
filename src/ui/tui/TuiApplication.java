package ui.tui;

import java.util.Scanner;
import service.GameService;

public class TuiApplication {

    // er is 1 Scanner voor alle invoer vanuit de terminal
    private final Scanner scanner;

    // Vanuit Main krijgt de TUI dezelfde Scanner doorgegeven.
    public TuiApplication(Scanner scanner) {
        this.scanner = java.util.Objects.requireNonNull(scanner);
    }

    // Bestaande directe TUI-start blijft ook mogelijk.
    public TuiApplication() {
        this(new Scanner(System.in));
    }

    // beheert de huidige game en maakt een nieuwe sessie aan
    private final GameService gameService = new GameService();

    // zorgt dat we kunnen navigeren tussen de schermen
    private final TuiNavigator navigator = new TuiNavigator(gameService);

    // Stuurt invoer van de player naar de goede controller
    private final TuiInputHandler inputHandler = new TuiInputHandler(navigator);

    public void start() {

        // start de applicatie op main menu1
        navigator.showMainMenu();

        // Blijf invoer lezen zolang de isRunning true is/we de game aan willen houden
        while (navigator.isRunning()) {

            System.out.print("> ");

            // check of er nog invoer is
            if (!scanner.hasNextLine()) {
                break;
            }

            // wat wil player doen
            String input = scanner.nextLine().trim();

            // geef de invoer van player door aan de actieve controller
            inputHandler.handle(input);
        }

        System.out.println("TUI gestopt");
    }


    // tijdelijke main voor testen
    public static void main(String[] args) {
        new TuiApplication().start();
    }
}
import java.util.Scanner;
import ui.tui.TuiApplication;
import view.TicTacToeGameApp;

// De game kan vanaf nu gestart worden via hier. je kan hier kiezen welke interface je wil gebruiken
// de TUI kant werkt en je kan een local game starten online nog niet maar dat probeer ik nog te fixen voor zondag
public class Main {

    public static void main(String[] args) {
        // gbruik dezelfde Scanner als de TUI: twee Scanners op System.in kunnen
        // anders elkaars invoer opvangen
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n================================");
            System.out.println("         TIC-TAC-TOE AI");
            System.out.println("================================");
            System.out.println("[1] TUI (terminal)");
            System.out.println("[2] GUI (tijdelijke JavaFX-versie)");
            System.out.println("[0] Afsluiten");
            System.out.print("> ");

            if (!scanner.hasNextLine()) {
                return;
            }
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    new TuiApplication(scanner).start();
                    return;
                case "2":
                    // Later vervangen door de nieuwe JavaFX-app
                    // voor nu leid dit naar de oude javafx game app maar deze werkt toch niet meer en wordt toch verwijderd later
                    // deze regel hieronder vervangen we als de gui werkt door iets als GuiApplication.main(args)
                    TicTacToeGameApp.main(args);
                    return;
                case "0":
                    System.out.println("DOEI");
                    return;
                default:
                    System.out.println("Kies: 0, 1 of 2.");
            }
        }
    }
}

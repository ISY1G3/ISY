import java.io.File;
import java.util.Scanner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.tui.TuiApplication;

/**
 * Startpunt van de app.
 *
 * Laat de gebruiker kiezen tussen de terminalinterface en de ondersteunde
 * FXML-interface.
 */
public class Main extends Application {

    public static void main(String[] args) {
        // gbruik dezelfde Scanner als de TUI: twee Scanners op System.in kunnen
        // anders elkaars invoer opvangen
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n================================");
            System.out.println("         TIC-TAC-TOE AI");
            System.out.println("================================");
            System.out.println("[1] TUI (terminal)");
            System.out.println("[2] GUI (FXML)");
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
                    launch(args);
                    return;
                case "0":
                    System.out.println("DOEI");
                    return;
                default:
                    System.out.println("Kies: 0, 1 of 2.");
            }
        }
    }
    // Start de FXML JavaFX-applicatie.
    @Override
    public void start(Stage stage) throws Exception {
        try {
            File file = new File("src/ui/gui/fxml/IntroMenuView.fxml");
            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent root = loader.load();

            Scene gameSelect = new Scene(root);
            stage.setScene(gameSelect);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

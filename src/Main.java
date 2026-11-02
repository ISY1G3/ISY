import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.gui.FxmlViews;
import ui.tui.TuiApplication;

/**
 * Startpunt van de app.
 *
 * Laat de gebruiker kiezen tussen de terminalinterface en de ondersteunde
 * FXML-interface.
 */
public class Main extends Application {

    public static void main(String[] args) {
        // bij cli argument "--tui" start het de TUI
        if (args.length > 0 && args[0].equals("--tui")) {
            Scanner scanner = new Scanner(System.in);
            new TuiApplication(scanner).start();
            // en als dat niet gebeurd dan start het de GUI
        } else {
            launch(args);
        }
    }
    // Start de FXML JavaFX-applicatie.
    @Override
    public void start(Stage stage) throws Exception {
        try {
            Parent root = FxmlViews.loader("IntroMenuView.fxml").load();

            Scene gameSelect = new Scene(root);
            stage.setScene(gameSelect);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package ui.gui.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            File file = new File("src/ui/gui/fxml/MainMenuView.fxml");
            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent root = loader.load();

            Scene gameSelect = new Scene(root);
            stage.setScene(gameSelect);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

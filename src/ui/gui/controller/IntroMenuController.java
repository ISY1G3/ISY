package ui.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class IntroMenuController {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private void initialize() {
        Platform.runLater(rootPane::requestFocus);
    }

    @FXML
    private void playOffline(ActionEvent event) throws IOException {
        File file = new File("src/ui/gui/fxml/MainMenuView.fxml");
        Parent root = new FXMLLoader(file.toURI().toURL()).load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void playOnline(ActionEvent event) throws IOException {
        File file = new File("src/ui/gui/fxml/ConnectionView.fxml");
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Parent root = loader.load();
        ConnectionController controller = loader.getController();

        Stage owner = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Stage connectionWindow = new Stage();
        connectionWindow.setTitle("Connection");
        connectionWindow.initOwner(owner);
        connectionWindow.initModality(Modality.WINDOW_MODAL);
        connectionWindow.setScene(new Scene(root));
        connectionWindow.setOnHidden(windowEvent -> controller.disconnect());
        connectionWindow.show();
    }
}

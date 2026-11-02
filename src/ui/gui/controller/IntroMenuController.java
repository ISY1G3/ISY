package ui.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ui.gui.FxmlViews;

import java.io.IOException;

public class IntroMenuController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private Button offlineButton;
    @FXML
    private Button onlineButton;

    @FXML
    private void initialize() {
        boolean keyboardFocus = SettingsController.getKeyboardFocus();
        offlineButton.setFocusTraversable(keyboardFocus);
        onlineButton.setFocusTraversable(keyboardFocus);
        rootPane.setFocusTraversable(!keyboardFocus);
        Platform.runLater(keyboardFocus ? offlineButton::requestFocus : rootPane::requestFocus);
    }

    @FXML
    private void playOffline(ActionEvent event) throws IOException {
        Parent root = FxmlViews.loader("MainMenuView.fxml").load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void playOnline(ActionEvent event) throws IOException {
        FXMLLoader loader = FxmlViews.loader("ConnectionView.fxml");
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

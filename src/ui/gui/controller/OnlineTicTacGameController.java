package ui.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import network.Client;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OnlineTicTacGameController implements Initializable {

    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;
    @FXML private Button forfeitButton;

    private Client client;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List.of(button1, button2, button3, button4, button5,
                button6, button7, button8, button9, forfeitButton)
                .forEach(button -> button.setFocusTraversable(false));
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void forfeit() {
        if (client != null && client.isConnected()) {
            client.forfeit();
        }

        try {
            File file = new File("src/ui/gui/fxml/OnlineMenuView.fxml");
            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent onlineMenu = loader.load();
            OnlineMenuController menuController = loader.getController();
            if (client != null) {
                menuController.setClient(client);
            }

            Stage stage = (Stage) forfeitButton.getScene().getWindow();
            stage.setTitle("OnlineMenu");
            stage.setScene(new Scene(onlineMenu));
            stage.setOnHidden(windowEvent -> menuController.disconnect());
            stage.show();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Online Tic-Tac-Toe");
            alert.setHeaderText("Could not return to OnlineMenu");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Dit is nog WIP
}

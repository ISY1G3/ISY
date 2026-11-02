package ui.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ui.gui.GuiOnlineClient;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OnlineMenuController implements Initializable {

    @FXML
    private Button tictactoebutton;
    @FXML
    private Button tournamentbutton;
    @FXML
    private Button playerTypeButton;
    @FXML
    private Button backbutton;

    private GuiOnlineClient online;
    private Stage stage;
    private boolean waitingForPlayer;
    private Alert waitingAlert;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List.of(tictactoebutton, tournamentbutton, playerTypeButton, backbutton)
                .forEach(button -> button.setFocusTraversable(false));
    }

    public void setOnline(GuiOnlineClient online) {
        this.online = online;
        updatePlayerTypeButton();
        online.setOnMatchStarted(() -> {
            waitingForPlayer = false;
            if (waitingAlert != null) {
                waitingAlert.close();
                waitingAlert = null;
            }
            openOnlineTicTacGame(online.isTournamentMode());
        });
    }

    @FXML
    private void togglePlayerType() {
        if (online == null) return;

        online.setLocalAi(!online.isLocalAi());
        updatePlayerTypeButton();
    }

    private void updatePlayerTypeButton() {
        if (playerTypeButton == null) return;
        playerTypeButton.setText(online != null && online.isLocalAi()
                ? "Player: AI"
                : "Player: Human");
    }

    @FXML
    private void playTicTacToe(ActionEvent event) {
        if (online == null || online.getClient() == null || !online.getClient().isConnected()) {
            showWarning("You are not connected to a server.");
            return;
        }
        online.setTournamentMode(false);
        if (waitingForPlayer) {
            return;
        }

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        waitingForPlayer = true;
        waitingAlert = new Alert(Alert.AlertType.INFORMATION);
        waitingAlert.setTitle("Tic-Tac-Toe");
        waitingAlert.setHeaderText("Waiting for player");
        waitingAlert.setContentText("Waiting for another player to join...");
        waitingAlert.initOwner(stage);
        waitingAlert.show();

        try {
            online.getClient().subscribe("tic-tac-toe");
        } catch (RuntimeException e) {
            waitingForPlayer = false;
            waitingAlert.close();
            waitingAlert = null;
            showWarning("Could not subscribe to Tic-Tac-Toe: " + e.getMessage());
        }
    }

    @FXML
    private void showTournamentInfo() {
        if (online != null) online.setTournamentMode(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Tournament");
        alert.setHeaderText("Servergestuurde tournaments");

        alert.setContentText(
                "Tournaments worden gestart door de serverbeheerder.\n\n" +
                        "Ingelogde online spelers worden automatisch " +
                        "door de server ingedeeld zolang deze niet in een game zitten.\n\n" +
                        "Je hoeft hier zelf geen toernooi te starten."
        );
        alert.showAndWait();
    }

    private void openOnlineTicTacGame(boolean tournamentMode) {
        Stage menuStage = getMenuStage();
        if (menuStage == null) {
            showWarning("Could not find the OnlineMenu window.");
            return;
        }

        try {
            File file = new File("src/ui/gui/fxml/OnlineTicTacGame.fxml");
            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent gameView = loader.load();
            OnlineTicTacGameController gameController = loader.getController();
            gameController.setOnline(online);
            gameController.setTournamentMode(tournamentMode);
            menuStage.setTitle("OnlineTicTacGame");
            menuStage.setScene(new Scene(gameView));
            menuStage.show();
        } catch (IOException e) {
            showWarning("Could not open the online Tic-Tac-Toe screen: " + e.getMessage());
        }
    }

    private Stage getMenuStage() {
        if (stage == null && tictactoebutton != null && tictactoebutton.getScene() != null
                && tictactoebutton.getScene().getWindow() instanceof Stage menuStage) {
            stage = menuStage;
        }
        return stage;
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Online Tic-Tac-Toe");
        alert.setHeaderText("Unable to start the game");
        alert.setContentText(message);
        Stage owner = getMenuStage();
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.showAndWait();
    }

    @FXML
    private void goBackToIntroMenu(ActionEvent event) throws IOException {
        disconnect();

        File file = new File("src/ui/gui/fxml/IntroMenuView.fxml");
        Parent introMenu = new FXMLLoader(file.toURI().toURL()).load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setOnHidden(null);
        stage.setTitle("IntroMenu");
        stage.setScene(new Scene(introMenu));
        stage.show();
    }

    public void disconnect() {
        if (online != null) online.disconnect();
        online = null;
    }
}

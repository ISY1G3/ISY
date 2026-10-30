package ui.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import network.ResultType;
import ui.gui.GuiOnlineClient;
import ui.gui.OnlineGameView;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OnlineTicTacGameController implements Initializable, OnlineGameView {

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
    @FXML private Label statusLabel;

    private Button[] cells;
    private GuiOnlineClient online;
    private boolean gameFinished;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cells = new Button[] {
                button1, button2, button3,
                button4, button5, button6,
                button7, button8, button9
        };

        for (int i = 0; i < cells.length; i++) {
            int position = i;
            cells[i].setFocusTraversable(false);
            cells[i].setOnAction(event -> onCellClicked(position));
        }
        forfeitButton.setFocusTraversable(false);
    }

    public void setOnline(GuiOnlineClient online) {
        this.online = online;
        online.attachView(this);
    }

    private void onCellClicked(int position) {
        if (online == null) return;

        if (!online.requestLocalMove(position)) {
            statusLabel.setText("Not your turn, or that cell is taken.");
            return;
        }

        // Disable input while waiting; draw the marker after the server's MOVE event.
        refresh();
    }

    @Override
    public void refresh() {
        if (online == null) return;

        boolean yourTurn = online.isYourTurn();
        for (int i = 0; i < cells.length; i++) {
            String symbol = online.symbolAt(i);
            cells[i].setText(symbol);
            cells[i].setDisable(!yourTurn || !symbol.isEmpty());
        }

        if (online.hasGivenUp()) {
            statusLabel.setText("Forfeit sent. Waiting for the result...");
        } else if (online.isAwaitingConfirmation()) {
            statusLabel.setText("Waiting for server confirmation...");
        } else {
            statusLabel.setText(yourTurn ? "Your turn" : "Waiting for opponent...");
        }
        forfeitButton.setDisable(online.hasGivenUp());
    }

    @Override
    public void showResult(ResultType type, String comment) {
        gameFinished = true;
        for (Button cell : cells) {
            cell.setDisable(true);
        }
        forfeitButton.setText("Back");
        forfeitButton.setDisable(false);

        String message = type.toString();
        if (comment != null && !comment.isBlank()) {
            message += " (" + comment + ")";
        }
        statusLabel.setText(message);
    }

    @FXML
    private void forfeit() {
        if (online == null) return;

        if (!gameFinished) {
            online.forfeit();
            refresh();
            return;
        }

        // A server result ends the GameSession before the user can return to the menu.
        online.clearFinishedMatch();
        online.detachView();

        try {
            File file = new File("src/ui/gui/fxml/OnlineMenuView.fxml");
            FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
            Parent menuRoot = loader.load();

            OnlineMenuController menuController = loader.getController();
            menuController.setOnline(online);

            Stage stage = (Stage) forfeitButton.getScene().getWindow();
            stage.setTitle("OnlineMenu");
            stage.setScene(new Scene(menuRoot));
            stage.setOnHidden(windowEvent -> online.disconnect());
            stage.show();
        } catch (IOException e) {
            online.attachView(this);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Online Tic-Tac-Toe");
            alert.setHeaderText("Could not return to OnlineMenu");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}

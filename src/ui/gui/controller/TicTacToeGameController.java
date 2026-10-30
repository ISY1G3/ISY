package ui.gui.controller;

import controller.TicTacToeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.GameMode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class TicTacToeGameController implements Initializable {

    @FXML private Button button1;
    @FXML private Button button2;
    @FXML private Button button3;
    @FXML private Button button4;
    @FXML private Button button5;
    @FXML private Button button6;
    @FXML private Button button7;
    @FXML private Button button8;
    @FXML private Button button9;

    @FXML private Text winnerText;

    @FXML private Button exitbutton;

    private List<Button> buttons;

    private TicTacToeController gameController;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3, button4, button5, button6, button7, button8, button9));

        for (int i = 0; i < buttons.size(); i++) {
            int boxNumber = i;
            Button button = buttons.get(i);
            button.setOnAction(event -> gameController.handleInputFromUser(boxNumber));
        }
        boolean keyboardFocus = SettingsController.getKeyboardFocus();
        buttons.forEach(button -> button.setFocusTraversable(keyboardFocus));
        exitbutton.setFocusTraversable(keyboardFocus);
    }

    public void startGame(String player1, String player2, GameMode gameMode, String player1Name, String player2Name, String startingPlayer) {
        char aiMarker = "AI".equals(player1) ? 'X' : '0';
        char startingMarker = "Player 2".equals(startingPlayer) ? '0' : 'X';
        String playerXName = "AI".equals(player1) ? "AI" : player1Name;
        String player0Name = "AI".equals(player2) ? "AI" : player2Name;

        gameController = new TicTacToeController();

        gameController.initView(new TicTacToeController.GameView() {
            @Override
            public void showMessage(String message) {
                winnerText.setText(message);
            }

            @Override
            public void setMarker(int boxNumber, char marker) {
                Button button = buttons.get(boxNumber);
                button.setText(String.valueOf(marker));
                button.setDisable(true);
            }

            @Override
            public void showGameResult(String gameResult) {
                winnerText.setText(gameResult);
                buttons.forEach(button -> button.setDisable(true));
                new Alert(Alert.AlertType.INFORMATION, gameResult).showAndWait();
            }
        });
        gameController.startGame(gameMode, startingMarker, aiMarker, playerXName, player0Name);
    }
    
    public void openMainMenuView(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File file = new File("src/ui/gui/fxml/MainMenuView.fxml");
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



}

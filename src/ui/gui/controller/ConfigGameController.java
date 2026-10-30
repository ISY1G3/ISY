package ui.gui.controller;

import controller.TicTacToeController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import model.GameMode;

import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;

import static model.GameMode.PLAYER_VS_AI;
import static model.GameMode.PLAYER_VS_PLAYER;




public class ConfigGameController {
    private Parent root;
    private Scene scene;

    @FXML
    private ChoiceBox<String> player1Box;
    @FXML
    private ChoiceBox<String> player2Box;
    @FXML
    private ChoiceBox<String> startingPlayer;
    @FXML
    private TextField player1Name;
    @FXML
    private TextField player2Name;
    @FXML
    private Button playButton;
    @FXML
    private Button exitButton;



    private Stage stage;

    @FXML
    public void initialize() {
        player1Box.getItems().addAll("Player", "AI");
        player2Box.getItems().addAll("AI", "Player");
        startingPlayer.getItems().addAll("Player 1", "Player 2");

        player1Box.setValue("Player");
        player2Box.setValue("AI");
        startingPlayer.setValue("Player 1");

        player1Name.setText("");
        player2Name.setText("");

        boolean keyboardFocus = SettingsController.getKeyboardFocus();
        player1Box.setFocusTraversable(keyboardFocus);
        player2Box.setFocusTraversable(keyboardFocus);
        startingPlayer.setFocusTraversable(keyboardFocus);
        player1Name.setFocusTraversable(keyboardFocus);
        player2Name.setFocusTraversable(keyboardFocus);
        exitButton.setFocusTraversable(keyboardFocus);
        playButton.setFocusTraversable(keyboardFocus);
    }

    @FXML
    private void playSelectGame(ActionEvent event) throws IOException {
        String player1 = player1Box.getValue();
        String player2 = player2Box.getValue();
        GameMode ticTacToeMode;

        if ("Player".equals(player1) && "Player".equals(player2)) {
            ticTacToeMode = PLAYER_VS_PLAYER;
            // AI vs AI moet nog toegevoegt worden
        } else if ("AI".equals(player1) && "AI".equals(player2)) {
            ticTacToeMode = PLAYER_VS_AI;
        } else {
            ticTacToeMode = PLAYER_VS_AI;
        }

        File file = new File("src/ui/gui/fxml/TicTacToeGameView.fxml");
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        TicTacToeGameController controller = loader.getController();
        controller.startGame(player1, player2, ticTacToeMode,
                player1Name.getText(), player2Name.getText(), startingPlayer.getValue());

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void openMainMenuView(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        File file = new File("src/ui/gui/fxml/MainMenuView.fxml");
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        root = loader.load();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}

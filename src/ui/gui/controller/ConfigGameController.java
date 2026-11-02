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
import javafx.scene.control.Alert;
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
        //!! De GameService accepteert geen lege namen, het feit dat deze worden gestuurd naar de gameservice is fout
        //!! deze 2 velden hieronder kunnen we doen als standaard invoer bijv Player 1 en 2.
        //!! maar wanneer een naam leeg is moet er een fout melding komen
        player1Name.setText("Player 1");
        player2Name.setText("Player 2");

        boolean keyboardFocus = SettingsController.getKeyboardFocus();
        player1Box.setFocusTraversable(keyboardFocus);
        player2Box.setFocusTraversable(keyboardFocus);
        startingPlayer.setFocusTraversable(keyboardFocus);
        player1Name.setFocusTraversable(keyboardFocus);
        player2Name.setFocusTraversable(keyboardFocus);
        exitButton.setFocusTraversable(keyboardFocus);
        playButton.setFocusTraversable(keyboardFocus);
    }

    // Het voordeel nu is dat de GUI bij een fout op het setupscherm blijft en uitlegt wat de gebruiker moet veranderen
    // Voor de AI player is er geen naam nodig
    //toegevoegd dat er een standaard naam is ingevuld

    @FXML
    private void playSelectGame(ActionEvent event) throws IOException {
        String player1 = player1Box.getValue();
        String player2 = player2Box.getValue();

        String name1 = player1Name.getText().trim();
        String name2 = player2Name.getText().trim();

        // De lokale GameService kan nog geen ai tegen ai spelen
        if ("AI".equals(player1) && "AI".equals(player2)) {
            showError("AI tegen AI wordt lokaal nog niet ondersteund");
            return;
        }
        // Alleen menselijke spelers moeten een naam invullen.
        if ("Player".equals(player1) && name1.isEmpty()) {
            showError("Vul een naam in voor speler 1");
            return;
        }
        if ("Player".equals(player2) && name2.isEmpty()) {
            showError("Vul een naam in voor speler 2");
            return;
        }

        //bepaalt game modus
        GameMode mode;

        if ("Player".equals(player1) && "Player".equals(player2)) {
            mode = PLAYER_VS_PLAYER;
        }
        else {
            mode = PLAYER_VS_AI;
        }

        // Laad pas na het valideren een gamescherm
        File file = new File(
                "src/ui/gui/fxml/TicTacToeGameView.fxml"
        );

        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        Parent gameRoot = loader.load();

        TicTacToeGameController controller = loader.getController();

        // geef de gekozen instellingen door
        controller.startGame(player1, player2, mode, name1, name2, startingPlayer.getValue());

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        stage.setScene(new Scene(gameRoot));
        stage.show();
    }

<<<<<<< HEAD
=======
    //De error die je krijgt als je iets fout invoer
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ongeldige instellingen");
        alert.setHeaderText("Wedstrijd kan niet starten");
        alert.setContentText(message);
        alert.showAndWait();
    }
>>>>>>> origin/main

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

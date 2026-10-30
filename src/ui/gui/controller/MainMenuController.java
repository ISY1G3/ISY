package ui.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.scene.control.Label;


public class MainMenuController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button tictactoebutton;
    @FXML
    private Button reversibutton;
    @FXML
    private Button tournamentbutton;
    @FXML
    private Button exitbutton;
    @FXML
    private Label welcome;
    @FXML 
    private Button settings;

    ArrayList<Button> buttons;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(
                tictactoebutton, reversibutton, tournamentbutton, settings, exitbutton));

        buttons.forEach(button -> {
            if (button != null) {
                button.setFocusTraversable(SettingsController.getKeyboardFocus());
            }
        });
    }

    public void openReversiView() {
        welcome.setText("Reversi is not available. Select a different mode.");
    }

    public void openSettingsView(ActionEvent event) throws IOException {
        File file = new File("src/ui/gui/fxml/SettingsView.fxml");
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openConfigGameView(ActionEvent event) throws IOException {
        File file = new File("src/ui/gui/fxml/ConfigGameView.fxml");
        FXMLLoader loader = new FXMLLoader(file.toURI().toURL());
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void closeMenu() {
        Platform.exit();
    }





}

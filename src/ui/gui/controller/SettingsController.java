package ui.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ui.gui.FxmlViews;

import java.io.IOException;

public class SettingsController {
    private static boolean kfocus;
    private Parent root;
    private Scene scene;

    @FXML
    private Button keyboardfocusbutton;
    @FXML
    private Button settings;
    @FXML
    private Button exitbutton;
    private Stage stage;

    @FXML
    private void initialize() {
        updateFocusControls();
    }

    public void openMainMenuView(ActionEvent event) throws IOException {
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = FxmlViews.loader("MainMenuView.fxml");
        root = loader.load();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void turnKeyboardFocus() {
        kfocus = !kfocus;
        updateFocusControls();
    }

    private void updateFocusControls() {
        
        keyboardfocusbutton.setFocusTraversable(kfocus);
        settings.setFocusTraversable(kfocus);
        exitbutton.setFocusTraversable(kfocus);
       
    }

    @FXML
    private void closeMenu() {
        Platform.exit();
    }

    public static boolean getKeyboardFocus() {
        return kfocus;
    }

}

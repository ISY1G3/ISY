/**
 * DeveloperPanel.java
 * 
 * This class represents a developer panel in the application.
 * 
 * It provides a user interface for developers to interact with the application and test new features
 * Without directly exposing them in the main application.
 * 
 * 
 * Notes:
 * 
 *  AI was used to generate the dummy buttons to test the developer panel functionality. These will be removed later.
 * 
 * @author Andreas Schaafsma
 * @version 1.0
 */

package view.dev;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class DeveloperPanel extends VBox {

    private final Label statusLabel;

    public DeveloperPanel() {
        super(10);

        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));

        Label titleLabel = new Label("Developer Panel");
        statusLabel = new Label("Select a developer action");

        Button addTestDataButton = createDummyButton("Add test data");
        Button simulateWinButton = createDummyButton("Simulate win");
        Button resetGameButton = createDummyButton("Reset game");
        Button showDebugInfoButton = createDummyButton("Show debug info");

        getChildren().addAll(
                titleLabel,
                addTestDataButton,
                simulateWinButton,
                resetGameButton,
                showDebugInfoButton,
                statusLabel
        );
    }

    private Button createDummyButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setOnAction(event -> statusLabel.setText(text + " clicked"));
        return button;
    }
}

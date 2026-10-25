import controller.TicTacToeController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Board;
import model.GameMode;
import view.TicTacToeGameView;

// DEZE WORDT UITEINDELIJK HELEMAAL VERWIJDERD DE GUI ZAL WORDEN AANGEROEPEN VIA DE UI.GUI FOLDER

public class TicTacToeGameApp extends Application {
    private TicTacToeController ticTacToeController;

    @Override
    public void start(Stage stage) {
        ticTacToeController = new TicTacToeController();
        ticTacToeController.initGoToMenu(() -> showStartScreen(stage));
        showStartScreen(stage);
    }

    private void showStartScreen(Stage stage) {


        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Tic Tac Toe");
        Label question = new Label("Welke modus wil je spelen?");

        Button playerVsPlayerButton =
                new Button("Persoon tegen Persoon");

        Button playerVsAiButton =
                new Button("Persoon tegen AI");

        playerVsPlayerButton.setPrefWidth(200);
        playerVsAiButton.setPrefWidth(200);
        playerVsPlayerButton.setFocusTraversable(false);
        playerVsAiButton.setFocusTraversable(false);

        playerVsPlayerButton.setOnAction(event -> {
            startGame(stage, GameMode.PLAYER_VS_PLAYER);
        });

        playerVsAiButton.setOnAction(event -> {
            startGame(stage, GameMode.PLAYER_VS_AI);
        });

        VBox menu = new VBox(
                20,
                title,
                question,
                playerVsPlayerButton,
                playerVsAiButton
        );

        playerVsPlayerButton.setFocusTraversable(false); //Auto Focus disabling
        playerVsAiButton.setFocusTraversable(false); // Auto focus disabling

        menu.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();
    }

    private void startGame(Stage stage, GameMode gameMode) {
        TicTacToeGameView ticTacToeGameView =
                new TicTacToeGameView();

        // The controller owns game creation and initialization; the app wires
        // the view and selects the requested mode.
        ticTacToeController.initView(ticTacToeGameView);
        ticTacToeGameView.initController(ticTacToeController);

        // De controller is verantwoordelijk voor het starten van de game.
        ticTacToeController.startGame(gameMode, Board.PLAYER_X_MARKER);

        // Speelveld tonen
        stage.setScene(
                new Scene(ticTacToeGameView, 440, 480)
        );

        stage.setTitle("Game of TicTacToe");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

import controller.TicTacToeController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.GameMode;
import model.TicTacToeGame;
import view.TicTacToeGameView;

public class TicTacToeGameApp extends Application {
     private int storeP1 = 0;
     private int storeP2 = 0;
     private TicTacToeController ticTacToeController;

    @Override
    public void start(Stage stage) {
        showStartScreen(stage);
    }

    public void showStartScreen(Stage stage) {
       

        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Tic Tac Toe");
        Label question = new Label("Welke modus wil je spelen?");

        Button playerVsPlayerButton =
                new Button("Persoon tegen Persoon");

        Button playerVsAiButton =
                new Button("Persoon tegen AI");

        playerVsPlayerButton.setPrefWidth(200);
        playerVsAiButton.setPrefWidth(200);

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

        menu.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();
        
    }
    

    private void startGame(Stage stage, GameMode gameMode) {
        

        // Game
        TicTacToeGame ticTacToeGame =
                new TicTacToeGame();
        
        // View
        TicTacToeGameView ticTacToeGameView =
                new TicTacToeGameView();

        // Controller
        // Eerst kijken of dit het eerste spel is door te vergelijken met null
        // als het niet null is stop je de huidige score van het spelletje in storep
        // dan maak je nieuwe controller aan zodat je een nieuw spel kan starten
        // nadat het nieuwe spel klaar staat wordt de oude score weer in de score van de controller gestopt
        if (ticTacToeController == null) {
            ticTacToeController = new TicTacToeController();
            }          
        else {
             storeP1 = ticTacToeController.getScoreP1(0);
             storeP2 = ticTacToeController.getScoreP2(0);
             

            ticTacToeController = new TicTacToeController();
            ticTacToeController.getScoreP1(storeP1);
            ticTacToeController.getScoreP2(storeP2);
        
            

            }
            
                
        ticTacToeController.initGoToMenu(() -> showStartScreen(stage)); // zorgt dat gotmenu (was eerst leeg) hetzelfde doet als showStartScreen(stage)
        ticTacToeController.initModel(ticTacToeGame);
        ticTacToeController.initView(ticTacToeGameView);
        ticTacToeController.initGameMode(gameMode);

        ticTacToeGameView.initController(
                ticTacToeController
        );

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
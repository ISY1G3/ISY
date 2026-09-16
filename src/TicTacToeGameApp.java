import controller.TicTacToeController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TicTacToeGame;
import view.TicTacToeGameView;

public class TicTacToeGameApp extends Application {

    @Override
    public void start(Stage arg0) throws Exception { //JavaFx runt start(...) Stage is een container met Scene(s) om GUI te laten zien.
        TicTacToeGame ticTacToeGame = new TicTacToeGame();

        TicTacToeGameView ticTacToeGameView = new TicTacToeGameView();
        
        TicTacToeController ticTacToeController = new TicTacToeController();
        ticTacToeController.initModel(ticTacToeGame);
        ticTacToeController.initView(ticTacToeGameView);

        ticTacToeGameView.initController(ticTacToeController);

        arg0.setScene(new Scene(ticTacToeGameView, 440, 480));
        arg0.setTitle("Game of TicTacToe");
        arg0.show();
    }

    public static void main(String[] args) {
        launch(args); // Starts JavaFx
    }
}

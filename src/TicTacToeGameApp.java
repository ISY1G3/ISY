import controller.TicTacToeController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.TicTacToeGame;
import view.TicTacToeGameView;

public class TicTacToeGameApp extends Application {

    @Override
    public void start(Stage arg0) throws Exception { //JavaFx runt start(...) Stage is een container met Scene(s) om GUI te laten zien.
        TicTacToeGame ticTacToeGame = new TicTacToeGame(); //Maakt het model, dit bewaart het bord, spelers, beurten en winregels.

        TicTacToeGameView ticTacToeGameView = new TicTacToeGameView(); //Maakt GUI
        
        TicTacToeController ticTacToeController = new TicTacToeController(); //Maakt controller, dus kliks van spelers.
        ticTacToeController.initModel(ticTacToeGame);
        ticTacToeController.initView(ticTacToeGameView);

        ticTacToeGameView.initController(ticTacToeController); //Deze drie inits verbiden de drie onderdelen aan elkaar.

        arg0.setScene(new Scene(ticTacToeGameView, 440, 480)); //Maakt het scherm
        arg0.setTitle("Game of TicTacToe");
        arg0.show();
    }
}

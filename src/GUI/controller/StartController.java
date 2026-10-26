package GUI.controller;
import javafx.scene.image.ImageView;
import controller.TicTacToeController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class StartController {
    private TicTacToeController ticTacToeController;
    private  SettingsController settings;
    private  GameChoiceController gamechoice;
    
    public void start(Stage stage) {
        ticTacToeController = new TicTacToeController();
        settings = new SettingsController();
        gamechoice = new GameChoiceController();
        ticTacToeController.initGoToMenu(() -> showStartScreen(stage));
        showStartScreen(stage);
    }

     
    

 
    public void showStartScreen(Stage stage) {
        System.out.println("Goed1");


        
        

        Button Localgame =
                new Button("Speel local game");

        Localgame.setPrefWidth(200);
        Localgame.setFocusTraversable(settings.Getkfocus());

        Localgame.setOnAction(event -> {
            gamechoice.start(stage);
           System.out.println("werkt");
        });

        Button Onlinegame =
                new Button("Speel online game");

        
        Onlinegame.setPrefWidth(200);
        
        
        Onlinegame.setFocusTraversable(settings.Getkfocus());

        

        Onlinegame.setOnAction(event -> {
            gamechoice.start(stage);
        });
        Image settingsfoto = new Image("https://e7.pngegg.com/pngimages/193/724/png-clipart-computer-icons-cogs-computer-desktop-wallpaper.png", 20, 20, true, true);
        
        Button Settings = 
            new Button("Settings");

        Settings.setOnAction(event -> {
            settings.start(stage);
        });
        Settings.setGraphic(new ImageView(settingsfoto));
        Settings.setFocusTraversable(settings.Getkfocus());
        

        VBox menu = new VBox(
                20,
                
                Localgame,
                Onlinegame,
                Settings
                
        );

      

        

        menu.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();
    }
    
}
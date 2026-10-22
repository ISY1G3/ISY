import java.util.random.RandomGenerator.SplittableGenerator;

import controller.TicTacToeController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.GameMode;
import view.TicTacToeGameView;
import javafx.scene.image.*;

public class TicTacToeGameApp extends Application {
     private int storeP1 = 0;
     private int storeP2 = 0;
     private TicTacToeController ticTacToeController;
     private boolean kfocus = false;
     private String taalopties = "nl";
     private boolean check = false;

    @Override
    public void start(Stage stage) {
        ticTacToeController = new TicTacToeController();
        ticTacToeController.initGoToMenu(() -> showStartScreen(stage));
        showStartScreen(stage);
    }

    private void showStartScreen(Stage stage) {


        
        

        Button Localgame =
                new Button("Speel local game");

        Localgame.setPrefWidth(200);
        Localgame.setFocusTraversable(kfocus);

        Localgame.setOnAction(event -> {
           showGamechoice(stage);
           System.out.println("werkt");
        });

        Button Onlinegame =
                new Button("Speel online game");

        
        Onlinegame.setPrefWidth(200);
        
        
        Onlinegame.setFocusTraversable(kfocus);

        

        Onlinegame.setOnAction(event -> {
            showGamechoice(stage);
        });
        Image settingsfoto = new Image("https://e7.pngegg.com/pngimages/193/724/png-clipart-computer-icons-cogs-computer-desktop-wallpaper.png", 20, 20, true, true);
        
        Button Settings = 
            new Button("Settings");

        Settings.setOnAction(event -> {
            showSettings(stage);
        });
        Settings.setGraphic(new ImageView(settingsfoto));
        Settings.setFocusTraversable(kfocus);
        

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
     public void showSettings(Stage stage) {
       

        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Settings");

        Button terug = new Button("Terug");
            terug.setOnAction(event -> {
                showStartScreen(stage);

            }

        );
        


        
       

        CheckBox keyboardFocus = new CheckBox("Keyboardfocus");
        keyboardFocus.setSelected(kfocus); // toont de huidige instelling

        keyboardFocus.setOnAction(event -> {
            kfocus = keyboardFocus.isSelected();

            System.out.println(
                kfocus ? "keyboard focus is aan" : "keyboard focus is uit"
            );
            showSettings(stage);
        });
        keyboardFocus.setFocusTraversable(kfocus);
        terug.setFocusTraversable(kfocus);



        Image nlfoto = new Image("https://i.pinimg.com/originals/8a/fd/ac/8afdacb45c5b6f4772c47fe1484caa22.png", 20, 20, true, true);
        
       
        Button nltaal = new Button("Nederlands");
        nltaal.setOnAction(event -> { 
            taalopties = "nl";
            System.out.println(taalopties);

        });

        nltaal.setGraphic(new ImageView(nlfoto));
        nltaal.setFocusTraversable(kfocus);

        Image enfoto = new Image("https://static.vecteezy.com/system/resources/previews/018/927/059/non_2x/england-flag-icons-png.png", 20, 20, true, true);
        
       
        Button entaal = new Button("English");
        entaal.setOnAction(event -> { 
            taalopties = "en";
            System.out.println(taalopties);

        });

        entaal.setGraphic(new ImageView(enfoto));
        entaal.setFocusTraversable(kfocus);

        Image frfoto = new Image("https://www.pngplay.com/wp-content/uploads/10/France-Flag-Transparent-Images.png", 20, 20, true, true);
        
       
        Button frtaal = new Button("français");
        frtaal.setOnAction(event -> { 
            taalopties = "fr";
            System.out.println(taalopties);

        });

        frtaal.setGraphic(new ImageView(frfoto));
        frtaal.setFocusTraversable(kfocus);

        HBox taalHBox = new HBox(
            20,
            nltaal,
            entaal,
            frtaal
        );
        taalHBox.setAlignment(Pos.BOTTOM_CENTER);

        VBox menu = new VBox(
            
                20,
                title,
                
                terug,
                keyboardFocus,
                taalHBox

                
                
        );
        

        

        menu.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();
        
    }

    public boolean Getkfocus() {
        return kfocus;
    }


    public void showGamechoice(Stage stage){
        Label title = 
            new Label("Tic Tac Toe");
        Label question = 
            new Label("Welk spel wil je spelen?");

        Button Tictactoe =
                new Button("Tic Tac Toe");
        Tictactoe.setFocusTraversable(kfocus);

        Tictactoe.setOnAction(event ->{
            showGameSelect(stage);
        });

        Tictactoe.setFocusTraversable(kfocus);

        Button Terug = 
            new Button("Terug");

        Terug.setOnAction(event ->{
            showStartScreen(stage);
        });
        Terug.setFocusTraversable(kfocus);

        VBox keuze = new VBox(
            title,
            question,
            Terug,
            Tictactoe
        );
        keuze.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(keuze, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();
        




    }




    public void showGameSelect(Stage stage){
        
        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Tic Tac Toe");
        Label question = new Label("Welke modus wil je spelen?");

        

        
        ChoiceBox<String> speler1Box = new ChoiceBox<>();
        speler1Box.getItems().addAll("ai", "speler");
        speler1Box.setFocusTraversable(kfocus);

        

        
        ChoiceBox<String> speler2Box = new ChoiceBox<>();
        speler2Box.getItems().addAll("ai", "speler");
        speler2Box.setFocusTraversable(kfocus);

        TextField speler1naam = new TextField("naam speler 1");

        TextField speler2naam = new TextField("naam speler 2");
        speler1naam.setFocusTraversable(kfocus);
        speler2naam.setFocusTraversable(kfocus);



        Button startbutton = new Button("start");
        startbutton.setFocusTraversable(kfocus);
        startbutton.setOnAction(event ->{
            if ("speler".equals(speler1Box.getValue()) && "ai".equals(speler2Box.getValue()) ) {
                startGame(stage, GameMode.PLAYER_VS_AI);

    
        }
        else if("speler".equals(speler1Box.getValue()) && "speler".equals(speler2Box.getValue()) ) {
                startGame(stage, GameMode.PLAYER_VS_PLAYER);

    
        }
        else{
            System.out.println("Dit is niet Mogelijk"); 


        }
        

        }

        );
         
        Button homeButton = new Button("Terug");
        homeButton.setAlignment(Pos.TOP_LEFT);
        homeButton.setFocusTraversable(kfocus);
       

        homeButton.setOnAction(event -> {
           showStartScreen(stage);
        });

        HBox p1 = new HBox(
            20,
            speler1Box,
            speler1naam 
            
        );
        p1.setAlignment(Pos.CENTER);

        HBox p2 = new HBox(
            20,
            speler2Box,
            speler2naam 
            
        );
        p2.setAlignment(Pos.CENTER);
        // Before the first game there is no controller yet, so use the stored scores. ai code stukje
        int currentScoreP1 = ticTacToeController == null
                ? storeP1
                : ticTacToeController.getScoreP1(0);
        int currentScoreP2 = ticTacToeController == null
                ? storeP2
                : ticTacToeController.getScoreP2(0);
        Label scoreplayer1 = new Label("Speler 1:" + currentScoreP1);
        Label scoreplayer2 = new Label("Speler 2: " + currentScoreP2);
        
        //
       
           
        HBox scoresHBox = new HBox(
            20,
            scoreplayer1,
            scoreplayer2
        
        );
        scoresHBox.setAlignment(Pos.CENTER);



        VBox inhoud = new VBox(
            
                20,
                
                title,
                scoresHBox,
                question,
                p1,
                p2,
                startbutton
                
        );
        

        inhoud.setAlignment(Pos.CENTER);

       
        
        
        VBox menu = new VBox(
            homeButton,
            inhoud
            
        );
        VBox.setVgrow(inhoud, Priority.ALWAYS);

       

         
        

        

        
        
        


        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();


    }
    

    private void startGame(Stage stage, GameMode gameMode) {
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
            
                
        ticTacToeController.initGoToMenu(() -> showGameSelect(stage)); // zorgt dat gotmenu (was eerst leeg) hetzelfde doet als showStartScreen(stage)
        ticTacToeController.initView(ticTacToeGameView);
        ticTacToeGameView.initController(ticTacToeController);

        // De controller is verantwoordelijk voor het starten van de game.
        ticTacToeController.startGame(gameMode);

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

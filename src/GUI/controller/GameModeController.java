package GUI.controller;


import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import controller.TicTacToeController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import model.GameMode;
import view.TicTacToeGameView;

public class Gamemodecontroller {
    private TicTacToeController TicTacToeController;
    private  Settingscontroller settings;
   
    private Startcontroller start;
    

     public void start(Stage stage) {
        TicTacToeController = new TicTacToeController();
        start = new Startcontroller();
        settings = new Settingscontroller();
      
        showGameSelect(stage);
    }

    public void showGameSelect(Stage stage){
         System.out.println("Goed2");
        
        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Tic Tac Toe");
        Label question = new Label("Welke modus wil je spelen?");

        

        
        ChoiceBox<String> speler1Box = new ChoiceBox<>();
        speler1Box.getItems().addAll("ai", "speler");
        speler1Box.setFocusTraversable(settings.Getkfocus());

        

        
        ChoiceBox<String> speler2Box = new ChoiceBox<>();
        speler2Box.getItems().addAll("ai", "speler");
        speler2Box.setFocusTraversable(settings.Getkfocus());

        TextField speler1naam = new TextField("naam speler 1");

        TextField speler2naam = new TextField("naam speler 2");
        speler1naam.setFocusTraversable(settings.Getkfocus());
        speler2naam.setFocusTraversable(settings.Getkfocus());



        Button startbutton = new Button("start");
        startbutton.setFocusTraversable(settings.Getkfocus());
        startbutton.setOnAction(event ->{
            GameMode selectedMode;
            if ("speler".equals(speler1Box.getValue()) && "ai".equals(speler2Box.getValue()) ) {
                selectedMode = GameMode.PLAYER_VS_AI;
        }
        else if("speler".equals(speler1Box.getValue()) && "speler".equals(speler2Box.getValue()) ) {
                selectedMode = GameMode.PLAYER_VS_PLAYER;
        }
        else{
            System.out.println("Dit is niet Mogelijk"); 
            return;
        }

        TicTacToeGameView gameView = new TicTacToeGameView();
        TicTacToeController.initView(gameView);
        gameView.initController(TicTacToeController);
        TicTacToeController.initGoToMenu(() -> showGameSelect(stage));
        TicTacToeController.startGame(selectedMode);
        stage.setScene(new Scene(gameView, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();

        });
         
        Button homeButton = new Button("Terug");
        homeButton.setAlignment(Pos.TOP_LEFT);
        homeButton.setFocusTraversable(settings.Getkfocus());
       

        homeButton.setOnAction(event -> {
           start.start(stage);
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
        int currentScoreP1 = TicTacToeController == null
                ? 0
                : TicTacToeController.getScoreP1(0);
        int currentScoreP2 = TicTacToeController == null
                ? 0
                : TicTacToeController.getScoreP2(0);
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
    
}

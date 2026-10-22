package GUI.controller;

import javafx.scene.control.Label;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Gamechoicecontroller {
    private  Gamemodecontroller gameselect;
    private Startcontroller start;
    private Settingscontroller settings;

    public void start(Stage stage) {
        gameselect = new Gamemodecontroller();
        start = new Startcontroller();
        settings = new Settingscontroller();

        
        showGamechoice(stage);
    }

    
     public void showGamechoice(Stage stage){
         System.out.println("Goed2");
       

        Label title = 
            new Label("Tic Tac Toe");
        Label question = 
            new Label("Welk spel wil je spelen?");

        Button Tictactoe =
                new Button("Tic Tac Toe");
        Tictactoe.setFocusTraversable(settings.Getkfocus());

        Tictactoe.setOnAction(event ->{
            gameselect.start(stage);
        });

        Tictactoe.setFocusTraversable(settings.Getkfocus());

        Button Terug = 
            new Button("Terug");

        Terug.setOnAction(event ->{
            start.start(stage);
        });
        Terug.setFocusTraversable(settings.Getkfocus());

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
    
}

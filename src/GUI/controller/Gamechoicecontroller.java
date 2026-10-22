package GUI.controller;

import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import controller.TicTacToeController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.Shadow;
import javafx.scene.layout.Priority;

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

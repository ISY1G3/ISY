package GUI.controller;

import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SettingsController {
    private boolean kfocus = false;
    private String taalopties = "nl";
      public boolean Getkfocus() {
        return kfocus;
    }
    private StartController start;
    public void start(Stage stage) {
        
        start = new StartController();
       

        
        showSettings(stage);
    }
    

  public void showSettings(Stage stage) {
        System.out.println("Goed3");
   
       

        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Settings");

        Button terug = new Button("Terug");
            terug.setOnAction(event -> {
                start.start(stage);

            }

        );
        


        
       

        CheckBox keyboardFocus = new CheckBox("Keyboardfocus");
        keyboardFocus.setSelected(kfocus); // toont de huidige instelling

        keyboardFocus.setOnAction(event -> {
            kfocus = keyboardFocus.isSelected();

            System.out.println(
                kfocus ? "keyboard focus is aan" : "keyboard focus is uit"
            );
            Getkfocus();
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
}
        

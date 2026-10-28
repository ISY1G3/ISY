package view;

import controller.TicTacToeController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.GameMode;
import javafx.scene.image.*;

public class TicTacToeGameApp extends Application {
     private static int storeP1 = 0;
     private static int storeP2 = 0;
     private static TicTacToeController ticTacToeController;
     private static boolean kfocus = false;

    @Override
    public void start(Stage stage) {
        ticTacToeController = new TicTacToeController();
        ticTacToeController.initGoToMenu(() -> showStartScreen(stage));
        showStartScreen(stage);
    }

    private static void showStartScreen(Stage stage) {
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
        Image foto = new Image("https://e7.pngegg.com/pngimages/193/724/png-clipart-computer-icons-cogs-computer-desktop-wallpaper.png", 20, 20, true, true);

        Button Settings =
            new Button("Settings");

        Settings.setOnAction(event -> {
            showSettings(stage);
        });
        Settings.setGraphic(new ImageView(foto));
        Settings.setFocusTraversable(kfocus);


        VBox menu = new VBox(20, Localgame, Onlinegame, Settings);

        menu.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();

    }
     public static void showSettings(Stage stage) {


        //simpele opmaak van het startscherm van de game je kiest hier wat voor spel je wilt spelen
        Label title = new Label("Settings");

        Button terug = new Button("Terug");
            terug.setOnAction(event -> {
                showStartScreen(stage);
            }
        );

        Button Keyboardfocus = new Button("Keyboardfocus");
            Keyboardfocus.setOnAction(event -> {
            if (kfocus == false){
            kfocus = true;
            System.out.println("keyboard focus is aan");
            }
            else{
                kfocus = false;
                System.out.println("keyboard focus is uit");
            }

        });
        Keyboardfocus.setFocusTraversable(kfocus);
        terug.setFocusTraversable(kfocus);



        VBox menu = new VBox(

                20,
                title,
                terug,
                Keyboardfocus



        );




        menu.setAlignment(Pos.CENTER);

        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();

    }

    public boolean Getkfocus() {
        return kfocus;
    }


    public static void showGamechoice(Stage stage){
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




    public static void showGameSelect(Stage stage){

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


        menu.setAlignment(Pos.CENTER);
        VBox.setVgrow(inhoud, Priority.ALWAYS);













        stage.setScene(new Scene(menu, 440, 480));
        stage.setTitle("Game of TicTacToe");
        stage.show();
    }

    public static void startGame(Stage stage, GameMode gameMode) {
        TicTacToeGameView ticTacToeGameView =
                new TicTacToeGameView();

        // The controller owns game creation and initialization; the app wires
        // the view and selects the requested mode.
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

package view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static view.TicTacToeGameApp.showGamechoice;

public class SettingsScreen {
    private boolean kfocus = false;

    public void showStartScreen(Stage stage) {





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
            TicTacToeGameApp.showSettings(stage);
        });
        Settings.setGraphic(new ImageView(foto));
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
}

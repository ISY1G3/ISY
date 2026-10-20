package view;
import controller.TicTacToeController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.Board;

public class TicTacToeGameView extends VBox { //View is een verticale container

    private class TicTacToeTile extends Button { //Maakt een tegel en een nummer wordt gegeven 0 t/m 8.
        private int boxNumber;

        public TicTacToeTile(int boxNumber) {
            this.boxNumber = boxNumber;

            setFocusTraversable(false); // Disable auto focus
            setPrefSize(120, 120);   // knopgrootte
            setFocusTraversable(false);
            setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
        }

        public void setMarker(char marker) {
            setText("" + marker);
            setDisable(true);
        }

        public int getBoxNumber() {
            return boxNumber;
        }
    }

    private Label labelMessage;
    private TicTacToeTile[][] buttons;
    private TicTacToeController ticTacToeController;
    
    public TicTacToeGameView() {
        super(10);
        setup();
    }

    public void initController(TicTacToeController ticTacToeController) {
        this.ticTacToeController = ticTacToeController;
    }

    private void setup() {
        /* Wat setup doet:
        HorizontalBox wordt aangemaakt voor de titel bovenaan (player turn)
        GridPane aangemaakt voor het 3x3 speelbord
        Voor ieder vakje wordt TicTacToeTile gemaakt.
        Ieder vakje is een klikactie, bij een klik wordt het tegelnummer naar de controller gestuurd 
        Alle knoppen worden in het grid geplaatst
        De melding en grid worden aan de ViewBox toegevoegd.*/

        //Top Box to show the label
        HBox topBox = new HBox();
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(10 ));
        labelMessage = new Label("Player X goes first"); //Text boven het bord
        topBox.getChildren().add(labelMessage);

        //Grid Box voor the board
        GridPane gridBox = new GridPane();
        gridBox.setPadding(new Insets(10));
        gridBox.setAlignment(Pos.CENTER);
        gridBox.setHgap(2);
        gridBox.setVgap(2);
        buttons = new TicTacToeTile[Board.SIZE] [Board.SIZE]; //3x3 array met vakjes
        int boxNumber = 0;
        for(int i=0; i<Board.SIZE; i++) {
            for(int j=0; j<Board.SIZE; j++) {
                buttons[i][j] = new TicTacToeTile(boxNumber++);
                buttons[i][j].setPrefSize(120, 120);
                buttons[i][j].setMaxSize(120, 120);
                final int ii = i;
                final int jj = j;
                buttons[i][j].setOnAction(new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent arg0) {
                        ticTacToeController.handleInputFromUser(buttons[ii][jj].getBoxNumber()); //Verwerkt kliks
                    }
                });
            }
        }



         Button homeButton = new Button("Terug");
         homeButton.setPrefWidth(50);
         homeButton.setFocusTraversable(false);
       

        homeButton.setOnAction(event -> {
           ticTacToeController.resetGame();
        });

         VBox home = new VBox(
                
                homeButton
                );
       
         getChildren().add(home); 

            
            
        


        //Add buttons to gridBox
        for(int i=0; i<Board.SIZE; i++) {
            for(int j=0; j<Board.SIZE; j++) {
                gridBox.add(buttons[i][j], j, i);;
            }
        }

        getChildren().add(topBox);
        getChildren().add(gridBox);
    }

    public void showMessage(String message) { //Veranderd text boven het bord
        labelMessage.setText(message);
    }

    public void setMarker(int boxNumber, char marker) { //Zet X of 0 op bepaalde tegel
        int i = boxNumber/Board.SIZE;
        int j = boxNumber%Board.SIZE;
        buttons[i][j].setMarker(marker);
    }
    
    
           
                
       



    

    public void showGameResult(String gameResult) { //Opent game over popup en maakt een nieuw potje klaar
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Game Over!");
        alert.setContentText(gameResult);
        alert.showAndWait();
        System.out.println(gameResult);
        System.out.println(ticTacToeController.getScoreP1(0) + "- " + ticTacToeController.getScoreP2(0));
        ticTacToeController.resetGame();
    }
    
    
    
}


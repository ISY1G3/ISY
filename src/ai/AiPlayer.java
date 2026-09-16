package src.ai;

import java.util.List;

public class  AiPlayer {

    //Deze AI verwacht voor nu alleen nog 3 dingen om te werken
    // - String aiSymbol; weten of AI X of O is
    // - String getCurrentPlayer(); Is de AI wel of niet aan de beurt
    // - List<Integer> getValidMoves(); welke zetten er gedaan mogen worden

    private String aiSymbol;
    private String opponentSymbol;

    public AiPlayer(String aiSymbol) {
        this.aiSymbol = aiSymbol;
        //dit doe ik nog alleen voor latere AI Want die gaat games moeten simuleren en dan moet hij weten wie X en O is
        if(aiSymbol.equals("X")){
            this.opponentSymbol = "O";
        }
        else{
            this.opponentSymbol = "X";
        }
    }

    //DummyGameState MOET NOG VERVANGEN DOOR DE DAADWERKELIJKE CLASS!!
    //|--------------------------V------------------------------------|
    public int chooseMove(DummyGameState gameState) {
        //Dit krijg ik dus van de game funtie dus de naam weet ik niet zeker maar kan veranderd
        //Wanneer ik zeker weet dat de functie alleen gebruikt wordt als de AI aan de beurt is dan kan deze funtie weg
        String currentPlayer = gameState.getCurrentPlayer();

        //dit kijkt naar of de ai wel aan de beurt is en zo niet returnen we niks
        if(!currentPlayer.equals(aiSymbol)){
            System.out.println("Niet mijn beurt");
            return -1 ;//-1 gebruiken we om niks aan te duiden dus dat ik niks terug stuur als move
        }

        List<Integer> validMoves = gameState.getValidMoves(); // list van game met geldige moves

        //dit zorgt ervoor dat als de game zegt dat er geen geldige moves zijn dat er niks wordt terug gestuurd
        if(validMoves.isEmpty()){
            System.out.println("Geen geldige zetten meer");
            return -1 ; //-1 gebruiken we om niks aan te duiden dus dat ik niks terug stuur als move
        }

        //dit geeft dus de eerste geldige zet terug in de list met nummers/posities die wordt meegestuurd
        return validMoves.getFirst();
    }
    // Deze 2 staan er alleen zodat andere funties buiten de AIPlayer class kunnen vragen  welk symbool de AI of persoon is.
    public String getAiSymbol() {
        return aiSymbol;
    }
    public String getOpponentSymbol() {
        return opponentSymbol;
    }
}

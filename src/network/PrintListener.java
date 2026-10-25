package network;

import java.util.List;

/* Print elk event naar de console, handig om te testen wat de server stuurt.
   Wordt in Client.main aangemeld bij alle drie de soorten listeners. */

public class PrintListener implements GameListener, LobbyListener, ConnectionListener {
    @Override 
    public void onLoginOk() {
        System.out.println("Login succesful!");
    }

    @Override 
    public void onGameList(List<String> games) {
        System.out.println("Gamelist " + games);
    }

    @Override 
    public void onPlayerList(List<String> players) {
        System.out.println("PlayerList " + players);
    }

    @Override 
    public void onMatch(String opponent, String gameType, String startingPlayer) {
        System.out.println("Match versus: " + opponent + ". Game: " + gameType + ", player: " + startingPlayer + " starts first!");
    }

    @Override 
    public void onYourTurn() {
        System.out.println("Your Turn!");
    }

    @Override 
    public void onMove(String player, int move) {
        System.out.println("Move by: " + player + ". Move: " + move);
    }

    @Override 
    public void onResult(ResultType type, String comment) {
        System.out.println("Result: " + type + "( " + comment + ")");
    }

    @Override 
    public void onChallenge(String challenger, String game, int nr) {
        System.out.println("Challenged by: " + challenger + ". Game: " + game + ". (Number: " + nr + ").");
    }

    @Override 
    public void onChallengeCancelled(int nr) {
        System.out.println("Challenge cancelled, challenge number: " + nr);
    }

    @Override 
    public void onError(String reason) {
        System.out.println("Error from server: " + reason);
    }

    @Override 
    public void onDisconnect() {
        System.out.println("Connection lost!");
    }
}

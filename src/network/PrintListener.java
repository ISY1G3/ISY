package network;

import java.util.List;

/*Print dingen naar de console
Later bij de spelflow kan dit worden gebruikt om schermen en spelstatus bij te werken */

public class PrintListener implements ServerListener {
    @Override 
    public void onLoginOk() {
        System.err.println("Login succesful!");
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

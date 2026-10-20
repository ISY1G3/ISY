package network;
import java.util.List;

/*
ServerListener defineert de commandos die de serverkant aan de client kan doorgeven 
Daarna kan de client deze interface implementeren en serverberichten omzetten in acties 
(dat wordt dus later gedaan in gamecontroller)*/

public interface ServerListener {
    void onLoginOk();                                                       // inloggen gelukt	            OK na login
    void onGameList(List<String> games);                                    // lijst met spellen ontvangen  SVR GAMELIST
    void onPlayerList(List<String> players);                                // lijst met spelers ontvangen	SVR PLAYERLIST
    void onMatch(String opponent, String gameType, String startingPlayer);  // match gestart	            SVR GAME MATCH
    void onYourTurn();                                                      // jij bent aan de beurt	    SVR GAME YOURTURN
    void onMove(String player, int move);                                   // zet gedaan (ook je eigen)	SVR GAME MOVE
    void onResult(ResultType type, String comment);                         // match afgelopen	            SVR GAME WIN/LOSS/DRAW
    void onChallenge(String challenger, String game, int nr);               // je bent uitgedaagd	        SVR GAME CHALLENGE
    void onChallengeCancelled(int nr);                                      // uitdaging vervallen	        SVR GAME CHALLENGE CANCELLED
    void onError(String reason);                                            // commando afgewezen	        ERR <reden>
    void onDisconnect();                                                    // verbinding weg	            geen bericht, komt van Connection
}
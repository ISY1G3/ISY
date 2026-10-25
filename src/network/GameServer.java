package network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/*  
    GameServer zet acties om naar commando's (login, move, etc.)
    De berichten die van de server komen worden naar de listeners gestuurd.

    Dit is eigenlijk de enige klaase die dit protocol kent.
    Connection kan alleen met tekst werken en de rest van de app kent die events.
    Alle nieuwe dingen moeten dus eerst hier toekomen.
*/

public class GameServer {
    private final Connection connection;

    //  Lijsten met Listeners; gewone arrayList kan exceptions geven
    private final List<GameListener> gameListeners = new CopyOnWriteArrayList<>();
    private final List<LobbyListener> lobbyListeners = new CopyOnWriteArrayList<>();
    private final List<ConnectionListener> connectionListeners = new CopyOnWriteArrayList<>();
    private String lastCommand = "";
    private boolean debug;

    public GameServer (Connection connection) {
        this.connection = connection;
    }

    //-----Listeners aanmelden-----//

    public void addGameListener(GameListener l)             { gameListeners.add(l); }
    public void addLobbyListener(LobbyListener l)           { lobbyListeners.add(l); }
    public void addConnectionListener(ConnectionListener l) { connectionListeners.add(l); }

    //Roept een event aan bij alle listeners; fireresult voor onResult
    private void fireGame(Consumer<GameListener> event) {
        for (GameListener l : gameListeners) event.accept(l);
    }
    private void fireLobby(Consumer<LobbyListener> event) {
        for (LobbyListener l : lobbyListeners) event.accept(l);
    }
    private void fireConnection(Consumer<ConnectionListener> event) {
        for (ConnectionListener l : connectionListeners) event.accept(l);
    }
    private void fireResult(ResultType type, String comment) {
        fireGame(l -> l.onResult(type, comment));
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public void start() {
        connection.startListening(this::handleLine, () -> fireConnection(l -> l.onDisconnect()));
    }

    //-----Commando's naar de server-----//

    private void sendCommand(String command) {
        lastCommand = command;
        if (debug) System.out.println(">> " + command);
        connection.send(command);
    }

    void sendRaw(String text)                 { sendCommand(text); }

    //Geen return dus geen blokkades, returnwaarde wordt in de listeners weergegeven
    public void login(String name)          {sendCommand("login " + name);}
    public void getGameList()               {sendCommand("get gamelist");}
    public void getPlayerList()             {sendCommand("get playerlist");}
    public void subscribe(String gameType)  {sendCommand("subscribe " + gameType);} 
    public void move(int move)              {sendCommand("move " + move);}
    public void forfeit()                   {sendCommand("forfeit");}
    public void acceptChallenge(int number) {sendCommand("challenge accept " + number);}

    public void challenge(String player, String game) {
        sendCommand("challenge " + player + " " + game);
    }
    
    public void logout() {
        sendCommand("logout");
        connection.close();
    }

    //-----Berichten van de server-----//
    
    /*  Hele lange if statement I know :)
        Server stuurt "OK en ERR" na elke commando, die worden naar de connectionlisteners gestuurd
        SRV gamelist en playerlist naar de lobby
        SRV game... gaat naar handlegamemessage, lege regels worden genegeerd */

    void handleLine(String line) {
        if (debug) System.out.println("<< " + line);
        if (line == null || line.isBlank()) return;

        try {
            if (line.equals("OK")) {
                if (lastCommand.startsWith("login")) {
                    fireConnection(l -> l.onLoginOk());
                }
            } else if (line.startsWith("ERR")) {
                String reason = line.substring(3).trim();
                fireConnection(l -> l.onError(reason));
            } else if (line.startsWith("SVR GAMELIST")) {
                List<String> games = parseList(line);
                fireLobby(l -> l.onGameList(games));
            } else if (line.startsWith("SVR PLAYERLIST")) {
                List<String> players = parseList(line);
                fireLobby(l -> l.onPlayerList(players));
            } else if (line.startsWith("SVR GAME ")) {
                handleGameMessage(line.substring("SVR GAME ".length()));
            }
        } catch (RuntimeException e) {
            System.out.println("Kon regel niet verwerken: " + line + " (" + e + ")");
        }
    }

    /*  Verwerkt alle game related commando's
        Elke waarde wordt met extract uit de tekst gehaald en daarna volgt het event*/

    private void handleGameMessage(String rest) {
        if (rest.startsWith("MATCH")) {
            String opponent = extract(rest, "OPPONENT");
            String gameType = extract(rest, "GAMETYPE");
            String starting = extract(rest, "PLAYERTOMOVE");
            fireGame(l -> l.onMatch(opponent, gameType, starting));
        } else if (rest.startsWith("YOURTURN")) {
            fireGame(l -> l.onYourTurn());
        } else if (rest.startsWith("MOVE")) {
            String player = extract(rest, "PLAYER");
            int move = Integer.parseInt(extract(rest, "MOVE"));
            fireGame(l -> l.onMove(player, move));
        } else if (rest.startsWith("WIN")) {
            fireResult(ResultType.WIN, extract(rest, "COMMENT"));
        } else if (rest.startsWith("LOSS")) {
            fireResult(ResultType.LOSS, extract(rest, "COMMENT"));
        } else if (rest.startsWith("DRAW")) {
            fireResult(ResultType.DRAW, extract(rest, "COMMENT"));
        } else if (rest.startsWith("CHALLENGE CANCELLED")) {  
            int nr = Integer.parseInt(extract(rest, "CHALLENGENUMBER"));
            fireLobby(l -> l.onChallengeCancelled(nr));
        } else if (rest.startsWith("CHALLENGE")) {
            String challenger = extract(rest, "CHALLENGER");
            String game = extract(rest, "GAMETYPE");
            int nr = Integer.parseInt(extract(rest, "CHALLENGENUMBER"));
            fireLobby(l -> l.onChallenge(challenger, game, nr));
        }
    }

    //-----Hulpmethodes voor het parsen-----//

    //Haalt de waarde van KEY uit een tekst als: {KEY: "waarde", ...}
    String extract(String line, String key) {
        int start = line.indexOf(key + ": \"");
        if (start == -1) return "";
        start += key.length() + 3;
        int end = line.indexOf("\"", start);
        if (end == -1) return "";
        return line.substring(start, end);
    }

    //Zet een lijst als ["a", "b"] om naar een List<String>
    private List<String> parseList(String line) {
        int open = line.indexOf('[');
        int close = line.lastIndexOf(']');
        if (open == -1 || close == -1) return new ArrayList<>();
        String inside = line.substring(open + 1, close);
        if (inside.isBlank()) return new ArrayList<>();
        return Arrays.asList(inside.replace("\"", "").split(",\\s*"));
    }
}
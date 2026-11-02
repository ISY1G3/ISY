package network;

import java.util.ArrayList;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*  
    GameServer zet acties om naar commando's (login, move, etc.)
    De berichten die van de server komen worden naar de listeners gestuurd.

    Dit is eigenlijk de enige klaase die dit protocol kent.
    Connection kan alleen met tekst werken en de rest van de app kent die events.
    Alle nieuwe dingen moeten dus eerst hier toekomen.
*/

public class GameServer {
    private static final Logger LOGGER = Logger.getLogger(GameServer.class.getName());

    private final Connection connection;

    //  Lijsten met Listeners; gewone arrayList kan exceptions geven
    private final List<GameListener> gameListeners = new CopyOnWriteArrayList<>();
    private final List<LobbyListener> lobbyListeners = new CopyOnWriteArrayList<>();
    private final List<ConnectionListener> connectionListeners = new CopyOnWriteArrayList<>();
    private final Deque<String> pendingCommands = new ArrayDeque<>();
    private boolean debug;
    private long protocolSequence;

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

    private synchronized void sendCommand(String command) {
        pendingCommands.addLast(command);
        try {
            logProtocol(Level.INFO, ">>", commandForLog(command));
            connection.send(command);
        } catch (RuntimeException e) {
            pendingCommands.removeLastOccurrence(command);
            throw e;
        }
    }

    private synchronized String takePendingCommand() {
        return pendingCommands.pollFirst();
    }

    private synchronized void logProtocol(Level level, String direction, String message) {
        String threadInfo = debug ? " [thread=" + Thread.currentThread().getName() + "]" : "";
        LOGGER.log(level, "#{0} {1} {2}{3}",
                new Object[]{++protocolSequence, direction, message, threadInfo});
    }

    private String commandForLog(String command) {
        if (command == null) return "<unknown command>";
        if (command.startsWith("login ")) return "login <redacted>";
        if (command.startsWith("message ")) return "message <redacted>";
        return command;
    }

    private String serverMessageForLog(String message) {
        if (message.startsWith("SVR GAME MESSAGE")) return "SVR GAME MESSAGE <redacted>";
        return message;
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
        try {
            if (connection.isConnected()) sendCommand("logout");
        } catch (RuntimeException e) {
            LOGGER.log(Level.FINE, "Could not send logout; closing the connection anyway", e);
        } finally {
            connection.close();
        }
    }

    public void sendMessage(String text) {
        sendCommand("message " + text);
    }

    //-----Berichten van de server-----//
    
    /*  Hele lange if statement I know :)
        Server stuurt "OK en ERR" na elke commando, die worden naar de connectionlisteners gestuurd
        SRV gamelist en playerlist naar de lobby
        SRV game... gaat naar handlegamemessage, lege regels worden genegeerd */

    void handleLine(String line) {
        if (line == null || line.isBlank()) return;

        try {
            if (line.equals("OK")) {
                logProtocol(Level.INFO, "<<", line);
                String command = takePendingCommand();
                if (command != null && command.startsWith("login ")) {
                    fireConnection(l -> l.onLoginOk());
                } else if (command != null) {
                    fireConnection(l -> l.onCommandOk(command));
                }
            } else if (line.startsWith("ERR")) {
                String reason = line.substring(3).trim();
                String command = takePendingCommand();
                String commandName = command == null || command.isBlank()
                        ? "unknown command"
                        : command.split("\\s+", 2)[0];
                String loggedReason = reason.isBlank() ? "No reason provided" : reason;
                logProtocol(Level.WARNING, "<<", "ERR for " + commandName + ": " + loggedReason);
                fireConnection(l -> l.onCommandError(command, reason));
            } else if (line.startsWith("SVR GAMELIST")) {
                logProtocol(Level.INFO, "<<", serverMessageForLog(line));
                List<String> games = parseList(line);
                fireLobby(l -> l.onGameList(games));
            } else if (line.startsWith("SVR PLAYERLIST")) {
                logProtocol(Level.INFO, "<<", serverMessageForLog(line));
                List<String> players = parseList(line);
                fireLobby(l -> l.onPlayerList(players));
            } else if (line.startsWith("SVR GAME ")) {
                logProtocol(Level.INFO, "<<", serverMessageForLog(line));
                handleGameMessage(line.substring("SVR GAME ".length()));
            } else {
                logProtocol(Level.INFO, "<<", serverMessageForLog(line));
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Failed to process server message: " + line, e);
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

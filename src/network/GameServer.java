package network;

/*
(protocol: commando's sturen, regels parsen)
│     login(name), logout(), getGameList(), getPlayerList(), subscribe(gameType),
│     challenge(player, game), acceptChallenge(nr), move(int), forfeit(),
│     handleLine(String) 

--------------- Wat het doet
Vertaalt spelacties (login, challenge, etc) naar de servercomanndo's.
Serverregels wordt ook verwerkt en daarvoor wordt ServerListener aangeroepen
*/

public class GameServer {
    private final Connection connection;
    private ServerListener listener;
    private String lastCommand = "";

    public GameServer (Connection connection) {
        this.connection = connection;
    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    public void start() {
        connection.startListening(
            this::handleLine, 
            () -> {if (listener != null) listener.onDisconnect();}
        );
    }

    // Commando's naar de server 

    private void sendCommand(String command) {
        lastCommand = command;
        if (debug) System.out.println(">> " + command);
        connection.send(command);
    }

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

    /** Haalt de waarde van KEY uit een tekst als: {KEY: "waarde", ...} */
    String extract(String line, String key) {
        int start = line.indexOf(key + ": \"");
        if (start == -1) return "";
        start += key.length() + 3;              // skip  KEY: "

        int end = line.indexOf("\"", start);
        if (end == -1) return "";
        return line.substring(start, end);
    }

    // Berichten van server

    private boolean debug;

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /** Voor tests: stuurt een zelf getypt commando. */
    void sendRaw(String text) {
        sendCommand(text);
    }

    void handleLine(String line) {
        if (debug) System.out.println("<< " + line);
        if (line == null || line.isBlank() || listener == null) return;
        if (line == null || line.isBlank()) return;

        if (line.equals("OK")) {
            if (lastCommand.startsWith("login") && listener != null) {
                listener.onLoginOk();
            }
        } else if (line.startsWith("ERR")) {
            if (listener != null) {
                listener.onError(line.substring(3).trim()); // Geef de error niet "ERR"
            }
        }
    }
}

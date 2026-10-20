package network;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Client implements ServerListener {
    private final Connection connection = new Connection();
    private final GameServer server = new GameServer(connection);
    private ServerListener listener;    // Later veranderen met gamecontroller
    private String gameType;            // Gekozen spel

    public Client() {
        server.setListener(this);
    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    // ---- Acties naar de server

    public void connect(String host, int port) throws IOException {
        connection.connect(host, port);
        server.start();                   
    }

    public void login(String name)          { server.login(name); }
    public void getGameList()               { server.getGameList(); }
    public void getPlayerList()             { server.getPlayerList(); }

    public void subscribe(String game) {
        gameType = game;
        server.subscribe(game);
    }

    public void challenge(String player)    { server.challenge(player, gameType); }
    public void accept(int nr)              { server.acceptChallenge(nr); }
    public void sendMove(int move)          { server.move(move); }
    public void forfeit()                   { server.forfeit(); }
    public void disconnect()                { server.logout(); }

    // ---- Events van de server doorgeven

    @Override public void onLoginOk() {
        if (listener != null) listener.onLoginOk();
    }
    @Override public void onGameList(List<String> games) {
        if (listener != null) listener.onGameList(games);
    }
    @Override public void onPlayerList(List<String> players) {
        if (listener != null) listener.onPlayerList(players);
    }
    @Override public void onMatch(String opponent, String gameType, String startingPlayer) {
        if (listener != null) listener.onMatch(opponent, gameType, startingPlayer);
    }
    @Override public void onYourTurn() {
        if (listener != null) listener.onYourTurn();
    }
    @Override public void onMove(String player, int move) {
        if (listener != null) listener.onMove(player, move);
    }
    @Override public void onResult(ResultType type, String comment) {
        if (listener != null) listener.onResult(type, comment);
    }
    @Override public void onChallenge(String challenger, String game, int nr) {
        if (listener != null) listener.onChallenge(challenger, game, nr);
    }
    @Override public void onChallengeCancelled(int nr) {
        if (listener != null) listener.onChallengeCancelled(nr);
    }
    @Override public void onError(String reason) {
        if (listener != null) listener.onError(reason);
    }
    @Override public void onDisconnect() {
        if (listener != null) listener.onDisconnect();
    }

        // ---------- extra: voor testen in de terminal ----------

    /** Challenge met een willekeurig spel, niet alleen het gekozen gametype. */
    public void challenge(String player, String game) {
        server.challenge(player, game);
    }

    /** Stuurt precies wat je typt naar de server. */
    public void sendRaw(String text) {
        server.sendRaw(text);
    }

    public void setDebug(boolean debug) {
        server.setDebug(debug);
    }

    public boolean isConnected() {
        return connection.isConnected();
    }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 7789;

        Client client = new Client();
        client.setListener(new PrintListener());
        client.setDebug(true);               // laat >> en << regels zien
        client.connect(host, port);

        System.out.println("Verbonden. Typ een commando, of /exit om te stoppen.");
        try (Scanner scanner = new Scanner(System.in)) {
            while (client.isConnected()) {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                if (input.equals("/exit")) break;
                client.sendRaw(input);
            }
        }
        client.disconnect();
    }
}

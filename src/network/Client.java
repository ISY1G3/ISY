package network;

import java.io.IOException;
import java.util.Scanner;

/*  Client Maakt een Connection en GameServer aan en geeft het terug aan de app
    De rest van de app praat alleen met Client  */

public class Client {
    private final Connection connection = new Connection();         // Socket naar de server
    private final GameServer server = new GameServer(connection);   // Vertaalt acties naar commando's en berichten naar events voor de listneres
    private String gameType;                                        // LAATST gekozen spel voor "challenge" zodat je het niet altijd hoeft te geven

    //------Verbinding & Acties naar de server-----//

    /*  Maakt een verbinding en begint met luisteren
        server.start() maakt een nieuwe thread aan en werkt op de achtergrond
        IOException moet worden afhandeld (door foutmelding in UI bijv) */

    public void connect(String host, int port) throws IOException {
        connection.connect(host, port);
        server.start();                   
    }

    //  SpelActies
    public void login(String name)          { server.login(name); }
    public void getGameList()               { server.getGameList(); }
    public void getPlayerList()             { server.getPlayerList(); }

    public void subscribe(String game) {
        gameType = game;
        server.subscribe(game);
    }

    public void challenge(String player)    { server.challenge(player, gameType); }
    public void challenge(String player, String game) { server.challenge(player, game); }
    public void accept(int nr)              { server.acceptChallenge(nr); }
    public void sendMove(int move)          { server.move(move); }
    public void forfeit()                   { server.forfeit(); }
    public void disconnect()                { server.logout(); }

    //------Listeners aanmelden------//

    public void addGameListener(GameListener l)                 { server.addGameListener(l); }    public void addLobbyListener(LobbyListener l)           { server.addLobbyListener(l); }
    public void addConnectionListener(ConnectionListener l) { server.addConnectionListener(l); }
    
    //------Testen in Terminal------//

    /*  Alleen voor het testen, wordt later niet in app gebruikt
        sendRaw() stuurt precies wat je typt naar de server */

    public void sendRaw(String text)    { server.sendRaw(text); }
    public void setDebug(boolean debug) { server.setDebug(debug); }
    public boolean isConnected()        { return connection.isConnected(); }

    public static void main(String[] args) throws Exception {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 7789;
        
        // Client opstarten
        Client client = new Client();
        PrintListener printer = new PrintListener();
        client.addGameListener(printer);
        client.addLobbyListener(printer);
        client.addConnectionListener(printer);
        client.setDebug(true);                          // laat >> en << regels zien
        client.connect(host, port);


        System.out.println("Verbonden. Typ een commando, of 'exit' om te stoppen.");
        try (Scanner scanner = new Scanner(System.in)) {
            while (client.isConnected()) {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) continue;
                if (input.equals("exit")) break;
                client.sendRaw(input);
            }
        }
        client.disconnect();
    }
}
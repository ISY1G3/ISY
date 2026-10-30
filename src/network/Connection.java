package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
    connect(host, port)                          Opent de socket naar de gameserver (poort 7789) en maakt de lees- en schrijfstromen aan.
    send(String)                                 Schrijft één tekstregel naar de server en flusht de stroom.
    startListening(lineHandler, onDisconnect)    Start een aparte (daemon) thread die regels van de server leest.
                                                 - lineHandler: wordt aangeroepen bij elke regel (bijv. GameServer::handleLine)
                                                 - onDisconnect: wordt aangeroepen als de verbinding onverwacht wegvalt
    close()                                      Zet done op true en sluit de socket. Veilig om meerdere keren aan te roepen.
    isConnected()                                Geeft aan of de verbinding nog open is.
---------Wat deze ook doet
Ganeserver gebruikt deze om commando's te sturen en serverregels te ontvangen.
 */

public class Connection implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(Connection.class.getName());

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean done;
    private Consumer<String> lineHandler;
    private Runnable onDisconnect;

    private static String host = "127.0.0.1"; // localhost address
    private static int port = 7789; // port nummer van de server
    
    public void connect(String host, int port) throws IOException{
        try {
            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //  Server -> Client
            out = new PrintWriter(socket.getOutputStream(), true); // true = autoflush | Client -> Server
            done = false;
            LOGGER.info("Connected to server at " + host + ":" + port);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not connect to server at " + host + ":" + port, e);
            throw e;
        }
    }

    public void send(String text) {
        if (!isConnected() || out == null) {
            IllegalStateException error = new IllegalStateException("Not connected");
            LOGGER.log(Level.WARNING, "Could not send " + commandName(text) + ": connection is closed", error);
            throw error;
        }
        out.println(text);
        if (out.checkError()) {
            IOException error = new IOException("PrintWriter reported a send failure");
            LOGGER.log(Level.WARNING, "Could not send " + commandName(text), error);
            throw new IllegalStateException("Could not send command", error);
        }
    }

    private String commandName(String text) {
        if (text == null || text.isBlank()) return "unknown command";
        return text.split("\\s+", 2)[0];
    }

    public void startListening(Consumer<String> lineHandler, Runnable onDisconnect) { 
        /* We maken een aparte thread voor berichten die van de server komen 
        Deze methode accepteerd een string en geeft niks terug*/

        this.lineHandler = lineHandler;
        this.onDisconnect = onDisconnect;
        Thread thread = new Thread(this);
        thread.setDaemon(true); // Als rest van programma klaar is stopt deze thread ook
        thread.start();
    }

    public void close() {
        if (done) return;
        done = true;

        try {
            if (socket != null) socket.close();
            LOGGER.info("Connection closed");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Failed to close server connection", e);
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected() && !socket.isClosed() && !done;
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                lineHandler.accept(line);
            }
            if (!done) LOGGER.warning("Server closed the connection");
        } catch (IOException e) {
            if (!done) {
                LOGGER.log(Level.WARNING, "Connection lost while reading from server", e);
            }
        } catch (RuntimeException e) {
            LOGGER.log(Level.SEVERE, "Error while handling a server message", e);
        }

        if (!done) {
            try {
                onDisconnect.run(); // Bij onverwacht verbroken verbinding, de rest van de programma waarschuwen
            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE, "Disconnect listener failed", e);
            }
        }
        close();
    }

    public static void main(String[] args) throws Exception{
        Connection connection = new Connection();
        connection.connect(host, port);

        connection.startListening(
            text -> System.out.println("Server: " + text),
            () -> System.out.println("Connection lost!")
        );

        Scanner scanner = new Scanner(System.in);
        
        while (connection.isConnected()) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")) {
                break;
            }
            connection.send(input);
        }

        scanner.close();
        connection.clone();
    }
}

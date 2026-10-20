package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;

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

    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private volatile boolean done;
    private Consumer<String> lineHandler;
    private Runnable onDisconnect;

    private static String host = "127.0.0.1"; // localhost address
    private static int port = 7789; // port nummer van de server
    
    public void connect(String host, int port) throws IOException{
        socket = new Socket(host, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream())); //  Server -> Client
        out = new PrintWriter(socket.getOutputStream(), true); // true = autoflush | Client -> Server
        done = false;
    }

    public void send(String text) {
        if (out == null) throw new IllegalStateException("Not connected"); // Als je niet verbonden bent krijg je een error.
        out.println(text);
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
                System.out.println("Shutting down...");
        } catch (IOException ignore) {}
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
        } catch (IOException e) {
            if (!done) {
                System.out.println("Error while running: " + e); // Kijkt of er een foutmelding moet geprint worden
            }
        } catch (RuntimeException e) {
            System.out.println("Handler error: " + e.getMessage());
        }

        if (!done) {
            onDisconnect.run(); // Bij onverwacht verbroken verbinding, de rest van de programma waarschuwen
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

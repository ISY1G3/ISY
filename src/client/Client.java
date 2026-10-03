package client;

import java.io.*;
import java.net.*;
import java.util.List;

public class Client implements Runnable {

    private String host = "127.0.0.1"; // localhost address
    private int port = 7789; // port nummer van de server

    private Socket socket; // de verbinding met de server
    private BufferedReader in; // leest wat de server stuurt
    private PrintWriter out; // stuurt tekst naar de server
    private volatile boolean done; // geeft aan of het programma moet stoppen

    // Spel logica
    private boolean placed = false;
    private List<Integer> gohitthese;

    @Override
    public void run() {
        System.out.println("Booting game...");

        try {
            /*
             * getOutputStream() / getInputStream() zijn de ruwe bytes van en naar de server.
             * PrintWriter stuurt tekst naar de server.
             * InputStreamReader vertaalt bytes naar tekens.
             * BufferedReader verzamelt die tekens en geeft ze als hele regels met readLine().
             */
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            InputHandler inputHandler = new InputHandler(); // Zie class InputHandler
            Thread thread = new Thread(inputHandler); // aparte thread voor toetsenbordinvoer
            thread.setDaemon(true); // thread stopt automatisch als het programma stopt
            thread.start();

            System.out.println("Game started, use '/quit' to exit.");

            String inputMessage;

            // Blijf berichten van de server verwerken tot de verbinding sluit
            while ((inputMessage = in.readLine()) != null) {
                System.out.println(inputMessage);

                // TODO spellogica
            }

            // Server heeft de verbinding gesloten: netjes afsluiten
            shutdown();

        } catch (IOException error) {
            if (!done) {
                System.out.println("Verbinding verbroken: " + error.getMessage());
            }
            shutdown();
        }
    }

    public void login(String name) {
        final int MIN_LENGTH = 3;
        final int MAX_LENGTH = 16;

        // Check of input leeg is
        if (name == null || name.isBlank()) {
            System.out.println("Naam mag niet leeg zijn.");

        // Check of naam spaties bevat
        } else if (name.contains(" ")) {
            System.out.println("Naam mag geen spaties bevatten.");

        // Check of naam met een cijfer begint
        } else if (Character.isDigit(name.charAt(0))) {
            System.out.println("Naam mag niet met een cijfer beginnen.");

        // Check of naam te kort of te lang is
        } else if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            System.out.println("Naam moet tussen " + MIN_LENGTH + " en " + MAX_LENGTH + " characters hebben.");

        // Als alles goed is, log in
        } else {
            System.out.println("Logging in as " + name);
            out.println("login " + name);
        }
    }

    public void shutdown() {
        done = true;

        try {
            // Het sluiten van de socket sluit ook de streams
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException error) {
            System.out.println("Fout bij afsluiten: " + error.getMessage());
        }
    }

    public void help(String command) {
        if (command == null || command.isBlank()) {
            System.out.println("Command can not be empty.");
        } else if (command.contains(" ")) {
            System.out.println("Command can not contain spaces");
        } else {
            out.println(command);
        }
    }
   
    private class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("To login: /login <yourname>");

                while (!done) {
                    String message = inputReader.readLine();

                    if (message == null) {
                        shutdown();
                        return;
                    }

                    if (message.equalsIgnoreCase("/quit")) { // kijkt of de gebruiker wil stoppen
                        shutdown();
                        return;
                    } else if (message.startsWith("/login")) {
                        String[] i = message.split(" ", 2);

                        if (i.length < 2) {
                            System.out.println("Gebruik: /login <naam>");
                        } else {
                            login(i[1].trim());
                        }
                    } else if (message.equals("/players")) { 
                        out.println("get playerlist");
                    } else if (message.equals("/games")) {
                        out.println("get gamelist");
                    } else if (message.startsWith("/help")) {
                        String[] i = message.split(" ", 2);

                        if (i.length < 2) {
                            out.println("help");
                        } else {
                            help(i[1].trim());
                        }
                        
                    } else { 
                        out.println(message);
                    }
                }
            } catch (IOException error) {
                System.out.println("Fout bij lezen invoer: " + error.getMessage());
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        client.run();
    }
}
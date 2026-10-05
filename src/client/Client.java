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

                // TODO 
            }

            // Server heeft de verbinding gesloten: netjes afsluiten
            shutdown();

        } catch (IOException error) {
            if (!done) {
                System.out.println("Connection lost: " + error.getMessage());
            }
            shutdown();
        }
    }

    public void login(String name) {
        final int MIN_LENGTH = 3;
        final int MAX_LENGTH = 16;

        // Check of input leeg is
        if (name == null || name.isBlank()) {
            System.out.println("Name can't be empty.");

        // Check of naam spaties bevat
        } else if (name.contains(" ")) {
            System.out.println("Name can't contain spaces.");

        // Check of naam met een cijfer begint
        } else if (Character.isDigit(name.charAt(0))) {
            System.out.println("Name can't start with a number.");

        // Check of naam te kort of te lang is
        } else if (name.length() < MIN_LENGTH || name.length() > MAX_LENGTH) {
            System.out.println("Name must be between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters.");

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
                System.out.println("Shutting down...");
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
            out.println("help " + command);
        }
    }
   
    public void subscribe(String command) {
        if (command == null || command.isBlank()) {
            System.out.println("Use: 'help <command>'");
            System.out.println("To see commands, use '?'");
        } else if (command.contains(" ")) {
            System.out.println("Command can not contain spaces");
        } else {
            out.println("subscribe " + command);
        }
    }
    
    private class InputHandler implements Runnable {
        @Override
        public void run() {
            try {
                BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("To login: '/login <yourname>'");
                System.out.println("Use '?' to see all available commands.");

                while (!done) {
                    String message = inputReader.readLine();

                    if (message == null) {
                        shutdown();
                        return;
                    }

                    if (message.equalsIgnoreCase("quit") || message.equalsIgnoreCase("logout") || message.equalsIgnoreCase("exit") || message.equalsIgnoreCase("bye") || message.equalsIgnoreCase("disconnect")) { // kijkt of de gebruiker wil stoppen
                        shutdown();
                        return;
                    } else if (message.startsWith("login")) {
                        String[] i = message.split(" ", 2);

                        if (i.length < 2) {
                            System.out.println("Use: 'login <name>'");
                        } else {
                            login(i[1].trim());
                        }
                    } else if (message.equals("players")) { 
                        out.println("get playerlist");
                    } else if (message.equals("games")) {
                        out.println("get gamelist");
                    } else if (message.startsWith("help")) {
                        String[] i = message.split(" ", 2);

                        if (i.length < 2) {
                            out.println("help");
                        } else {
                            help(i[1].trim());
                        }
                        
                    } else if (message.startsWith("subscribe")){
                        String[] i = message.split(" ", 2);

                        if (i.length < 2) {
                            out.println("help");
                        } else {
                            subscribe(i[1].trim());
                        }

                    } else if (message.equals("?")) {
                        System.out.println("All commands:");
                        System.out.println("To login: 'login <name>'");
                        System.out.println("To exit: 'logout | exit | quit | disconnect | bye'");
                        System.out.println("To see available players/games: 'games | players'");
                        System.out.println("To play a game: 'subscribe <game>'");
                        System.out.println("To challenge a player: TODO");
                        System.out.println("To accept challenge: TODO");
                        System.out.println("To make a move: TODO");
                        System.out.println("To forfeit: TODO");
                        System.out.println("To send a message: TODO");
                        System.out.println("For general help: 'help'");
                        System.out.println("For command help: 'help <command'");
                    } else { 
                        out.println(message);
                    }
                }
            } catch (IOException error) {
                System.out.println("Error reading input: " + error.getMessage());
                shutdown();
            }
        }
    }

    public static void main(String[] args) {
        Client client = new Client();

        client.run();
    }
}
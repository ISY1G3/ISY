package client;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception {

        // Adres en poort van gameserver
        String host = "localhost";  
        int port = 7789;

        // Maak verbinding met server
        try (
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true); // Schrijver om tekst naar server te sturen
            BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream())); // Lees berichten van de server
            BufferedReader stdIn = new BufferedReader(
                new InputStreamReader(System.in)) // Consolue berichten lezer
        ) {
            // Server berichten lezen
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) { // Blijf serverberichten lezen zolang de verbinding open is
                System.out.println("Server: " + fromServer);

                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine(); // Wacht op jouw bericht in console
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        }
    }
}
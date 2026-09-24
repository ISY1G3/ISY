package client;

import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) throws Exception {

        //Adres en poort van gameserver
        String host = "localhost";
        int port = 7789;

        // Maak verbinding
        Socket socket = new Socket(host, port);
        System.out.println("Verbonden met server op " + host + ":" + port);

        // Om berichten NAAR de server te sturen
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
 
        // Om berichten VAN de server te ontvangen
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // Test bericht naar server
        out.println("test login");

        // Lees server response
        String response = in.readLine();
        System.out.println("Server response: " + response);

        // Verbinding afsluiten
        socket.close();
    }

}
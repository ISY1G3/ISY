package network;

/*Testclient, DO NOT DELETE totdat de echte client is geimplemnteerd */

import java.util.Scanner;

public class GameServerTest {
    public static void main(String[] args) throws Exception {
        Connection connection = new Connection();
        connection.connect("127.0.0.1", 7789);

        GameServer server = new GameServer(connection);
        server.setListener(new PrintListener());
        server.setDebug(true);
        server.start();

        System.out.println("Typ een commando. Typ /exit om te stoppen.");
        Scanner scanner = new Scanner(System.in);
        while (connection.isConnected()) {
            String input = scanner.nextLine();
            if (input.equals("/exit")) break;
            server.sendRaw(input);
        }
        connection.close();
        scanner.close();
    }
}
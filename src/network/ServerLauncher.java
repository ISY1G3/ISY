/* Gemaakt door AI om server te tesen, als het goed is, is het ook mogelijk om het via de terminal te doen.
Maar ik vond dit handiger omdat de connectie krijgen problemen gaf 
Dit wordt verwijderd wanneer de server werkt, nu effe niet verwijderen aub omdat het handig is.*/

/* BELANGRIJK!!!:
Soms sluit de oude server niet goed af, om dit af te sluiten en opnieuw in een nieuwe terminal starten gebruik de volgende commands:
netstat -ano | findstr :8081
taskkill /PID <HIER_DE_PID> /F
de PID is de laatste getal dat staat recht van "LISTENING" 

To reset admin password: Open new terminal:
cd src 
cd network
cd game
java -jar newgameserver-release-V1.jar --set-password admin
java -jar newgameserver-release-V1.jar
*/

package network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Dit start de bestaande gameserver (.jar) automatisch op als los proces.
 * Dit is GEEN eigen server-implementatie -- we starten gewoon de meegeleverde
 * jar, zodat je niet meer handmatig een aparte terminal hoeft te openen.
 *
 * Let op: pas het pad naar de jar hieronder aan naar waar jouw
 * newgameserver-release-V1.jar echt staat.
 */
public class ServerLauncher {

    public static void main(String[] args) throws IOException, InterruptedException {

        // Pad naar de jar file - PAS DIT AAN naar jouw eigen locatie
        String jarPath = "src/network/game/newgameserver-release-V1.jar";

        // Bouw het commando op zoals je het ook op de command line zou typen:
        // java -jar newgameserver-release-V1.jar
        ProcessBuilder builder = new ProcessBuilder("java", "-jar", jarPath);

        // Zorg dat de output van de server (println's van de server zelf)
        // gewoon in onze eigen console verschijnt
        builder.redirectErrorStream(true);

        System.out.println("Server wordt gestart...");
        Process serverProcess = builder.start();

        // Lees en print alles wat de server naar zijn console schrijft,
        // zodat je in dit venster ziet wanneer de server klaar is met opstarten
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(serverProcess.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println("[SERVER] " + line);
        }

        // Wacht tot het serverproces stopt (dit blokkeert, net als wanneer
        // je de server los in een terminal zou draaien)
        serverProcess.waitFor();
    }
}
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
import java.util.concurrent.TimeUnit;

/**
 * Dit start de bestaande gameserver (.jar) automatisch op als los proces.
 * Dit is GEEN eigen server-implementatie -- we starten gewoon de meegeleverde
 * jar, zodat je niet meer handmatig een aparte terminal hoeft te openen.
 *
 * Let op: pas het pad naar de jar hieronder aan naar waar jouw
 * newgameserver-release-V1.jar echt staat.
 *
 * Eerst wordt het admin-wachtwoord gereset, daarna start de server.
 */
public class ServerLauncher {

    private static volatile Process activeProcess;

    public static void main(String[] args) throws IOException, InterruptedException {

        // Ctrl+C closes the child server process as well as this launcher.
        Runtime.getRuntime().addShutdownHook(new Thread(
                ServerLauncher::stopActiveProcess,
                "server-launcher-shutdown"));

        // Pad naar de jar file - PAS DIT AAN naar jouw eigen locatie
        String jarPath = "src/network/game/newgameserver-release-V1.jar";

        // Stap 1: admin-wachtwoord instellen
        // java -jar newgameserver-release-V1.jar --set-password admin
        System.out.println("Admin wachtwoord wordt ingesteld...");
        int exitCode = runAndPrint("java", "-jar", jarPath, "--set-password", "admin");
        if (exitCode != 0) {
            System.out.println("Waarschuwing: --set-password eindigde met code " + exitCode);
        }

        // Stap 2: de server zelf starten
        // Bouw het commando op zoals je het ook op de command line zou typen:
        // java -jar newgameserver-release-V1.jar
        System.out.println("Server wordt gestart...");
        runAndPrint("java", "-jar", jarPath);
    }

    /**
     * Start een proces, print alle output ervan met het label [SERVER]
     * en wacht tot het proces klaar is. Geeft de exit code terug.
     */
    private static int runAndPrint(String... command) throws IOException, InterruptedException {
        ProcessBuilder builder = new ProcessBuilder(command);

        // Zorg dat de output van de server (println's van de server zelf)
        // gewoon in onze eigen console verschijnt
        builder.redirectErrorStream(true);

        Process process = builder.start();
        activeProcess = process;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            // Lees en print alle serveruitvoer in deze terminal.
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("[SERVER] " + line);
            }

            return process.waitFor();
        } catch (InterruptedException e) {
            stopProcess(process);
            Thread.currentThread().interrupt();
            throw e;
        } finally {
            if (activeProcess == process) {
                activeProcess = null;
            }
        }
    }

    private static void stopActiveProcess() {
        stopProcess(activeProcess);
    }

    private static void stopProcess(Process process) {
        if (process == null || !process.isAlive()) return;

        process.destroy();
        try {
            if (!process.waitFor(2, TimeUnit.SECONDS)) {
                process.destroyForcibly();
                process.waitFor();
            }
        } catch (InterruptedException e) {
            process.destroyForcibly();
            Thread.currentThread().interrupt();
        }
    }
}


package ui.tui;

// Dit bewaart de instellingen voor het starten van een lokale game
public class LocalSetupData {
    private final String gameName;
    private String player1Name = "Speler 1";
    private String player2Name = "Computer";
    private boolean player1Ai = false;
    private boolean player2Ai = true;
    private int startingPlayer = 0; // 0 = willekeurig, 1 = P1, 2 = P2

public LocalSetupData(String gameName) { this.gameName = gameName; }

public String getGameName() { return gameName; }
public String getPlayer1Name() { return player1Name; }
public String getPlayer2Name() { return player2Name; }

public void setPlayer1Name(String name) { player1Name = name; }
public void setPlayer2Name(String name) { player2Name = name; }

public boolean isPlayer1Ai() { return player1Ai; }
public boolean isPlayer2Ai() { return player2Ai; }

public void togglePlayer1Ai() { player1Ai = !player1Ai; }
public void togglePlayer2Ai() { player2Ai = !player2Ai; }

public int getStartingPlayer() { return startingPlayer; }
public void nextStartingPlayer() { startingPlayer = (startingPlayer + 1) % 3; }
public String startingPlayerLabel() {
    switch (startingPlayer) {
        case 1: return "Speler 1";
        case 2: return "Speler 2";
        default: return "Willekeurig";
    }
}
}

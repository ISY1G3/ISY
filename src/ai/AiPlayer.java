package ai;
// klopt voor eerste demo. kan random moves maken op basis van moves die hij krijgt van de game
import model.TicTacToeGame;

import java.util.List;
import java.util.Random;

public class AiPlayer {

    private char aiMarker;
    private Random random;

    public AiPlayer(char aiMarker) {
        this.aiMarker = aiMarker;
        this.random = new Random();
    }

    public int chooseMove(TicTacToeGame game) {
        // Is de ai wel aan de beurt?
        if (game.getCurrentPlayer().getMarker() != aiMarker) {
            return -1;
        }
        //vragen om alle mogelijke moves
        List<Integer> validMoves = game.getValidMoves();

        // Zijn geen zetten meer als de lijst van moves leeg is
        if (validMoves.isEmpty()) {
            return -1;
        }

        // kiest random een zet uit de lijst met moves
        int randomIndex = random.nextInt(validMoves.size());

        // stuurt 1 van de mogelijke moves terug
        return validMoves.get(randomIndex);
    }
}

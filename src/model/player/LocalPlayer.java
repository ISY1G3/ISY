package model.player;

import model.MoveContext;
import java.util.Objects;
/** een gewone speler. askForMove blokkeert niet, de TUI of GUI levert later invoer via de bijbehorende controller.*/
public final class LocalPlayer extends Player {

    public LocalPlayer(String name) {
        super(name);
    }

    @Override
    public void askForMove(MoveContext context) {
        Objects.requireNonNull(context, "MoveContext mag niet null zijn.");
        // Geen zet beschikbaar: de UI levert deze later aan.
    }
}
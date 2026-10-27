package model.player;

import model.MoveContext;
import java.util.Objects;

/**
 * deze player vertegenwoordigt de tegenstander op afstand
 * de netwerklistener levert later een bevestigde zet aan de online spelverwerking
 */
public final class RemotePlayer extends Player {

    public RemotePlayer(String name) {
        super(name);
    }
    // Geen scanner en geen wachtrij hier de OnlineGameCoordinator ontvangt MOVE van de server
    @Override
    public void askForMove(MoveContext context) {
        Objects.requireNonNull(context, "MoveContext mag niet null zijn.");
        // de zet komt later vanuit een servercallback binnen
    }
}
package online;

import java.util.Locale;
import java.util.Objects;
import model.GameMode;
import model.TicTacToeGame;
import model.player.LocalPlayer;
import model.player.AIPlayer;
import network.Client;
import network.ResultType;
import service.GameService;
import service.GameSession;

// dit is geen eigen bord alleen commando's en serverevents
public final class OnlineGameCoordinator {
    private final Client client;
    private final GameService service;
    private final String localNickname;
    private boolean yourTurn;                       // komt alleen uit de serverevent YOURTURN, en niet uit het lokale deel
    private boolean awaitingConfirmation;           // een verzonden move betekent niet dat deze is goedgekeurt maar dit voorkomt dubbele aanvragen
    private boolean gaveUp;
    private boolean yourTurnBeforeForfeit;
    private ResultType result;
    private String comment;

    public OnlineGameCoordinator(Client client, GameService service, String localNickname) {
        this.client = Objects.requireNonNull(client);
        this.service = Objects.requireNonNull(service);
        this.localNickname = Objects.requireNonNull(localNickname);
        // BELANGRIJK: de TUI registreert de GameListener en dispatcht events
        // naar zijn eigen UI-thread. Deze klasse registreert zichzelf dus NIET.
    }

    /** start een nieuwe online sessie bij MATCH en reset alle beurt- en resultaatflags */
    public void onMatch(String opponent, String gameType, String startingPlayer, boolean localUsesAi) {
        if (gameType == null || !gameType.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "")
                .equals("tictactoe")) throw new IllegalArgumentException("Spel nog niet ondersteund: " + gameType);
        // Een nieuwe MATCH van de server is leidend. Bij tournament-overgangen
        // kan de vorige online sessie lokaal nog actief zijn (bijvoorbeeld als
        // de einduitslag niet bij deze client is aangekomen). Ruim die op zodat
        // de nieuwe wedstrijd niet wordt geblokkeerd door ensureNoActiveSession.
        GameSession previousSession = service.getCurrentSession();
        if (previousSession != null && previousSession.getGameMode() == GameMode.ONLINE) {
            if (previousSession.isActive()) service.finishCurrentSession();
            service.clearCurrentSession();
        }

        // maakt een lokale representatie van de MATCH, met een RemotePlayer voor de andere client
        service.startOnlineTicTacToe(localNickname, opponent, startingPlayer, localUsesAi);
        yourTurn = false;
        awaitingConfirmation = false;
        gaveUp = false;
        result = null;
        comment = null;
    }
    /** YOURTURN is de toestemming van de server. De eigen online-AI verstuurt dan automatisch een zet */
    public void onYourTurn() {
        // een herhaald YOURTURN mag geen tweede AI-zet versturen
        if (awaitingConfirmation || gaveUp) return;
        GameSession session = service.getCurrentSession();
        if (session != null && session.isActive() && session.getGameMode() == GameMode.ONLINE
                && session.getGame() instanceof TicTacToeGame game
                && game.getCurrentPlayer().getName().equals(localNickname)) {
            yourTurn = true;
            if (game.getCurrentPlayer() instanceof AIPlayer) {
                // De server bepaalt wanneer de AI mag spelen.
                int choice = service.chooseOnlineAiMove();
                if (!requestMove(choice, true))
                    throw new IllegalStateException("Kon de online AI-zet niet versturen");
            }
        }
    }

    // Alleen menselijke invoer mag deze methode aanroepen.
    public boolean requestLocalMove(int boxNumber) {
        return requestMove(boxNumber, false);
    }

    /** checkt beurt, spelertype en leeg vakje, verzendt de zet, en wacht daarna op serverbevestiging */
    private boolean requestMove(int boxNumber, boolean aiRequest) {
        GameSession session = service.getCurrentSession();
        if (!yourTurn || awaitingConfirmation || gaveUp || session == null || !session.isActive()
                || session.getGameMode() != GameMode.ONLINE
                || !(session.getGame() instanceof TicTacToeGame game)
                || !game.getCurrentPlayer().getName().equals(localNickname)
                || !game.getValidMoves().contains(boxNumber)) return false;
        if (aiRequest && !(game.getCurrentPlayer() instanceof AIPlayer)) return false;
        if (!aiRequest && !(game.getCurrentPlayer() instanceof LocalPlayer)) return false;

        yourTurn = false;
        awaitingConfirmation = true;
        // geen GameService.makeMove(): het echte bord wordt niet aangepast tot onMove
        try { client.sendMove(boxNumber); }
        catch (RuntimeException e) {
            yourTurn = true;
            awaitingConfirmation = false;
            throw e;
        }
        return true;
    }

    /**
     * Verwerkt een zet die door de server is bevestigd
     *
     * De server stuurt ook onze eigen zetten terug als MOVE-event
     * Daardoor verwerken we onze eigen zetten en die van
     * de tegenstander via dezelfde GameService-methode
     *
     * We plaatsen een eigen zet dus niet meteen na sendMove(),
     * omdat de server die zet nog kan weigeren
     *
     * Bij bevestiging van onze eigen zet wordt
     * awaitingConfirmation weer false
     */
    public void onMove(String playerName, int boxNumber) {
        service.applyConfirmedOnlineMove(playerName, boxNumber);
        if (localNickname.equals(playerName)) awaitingConfirmation = false;
        // nieuwe beurt start pas bij YOURTURN niet bij lokale beurtwisseling
    }

    /** slaat de echte serveruitslag op en stopt de wedstrijd. */
    public void onResult(ResultType type, String comment) {
        this.result = Objects.requireNonNull(type);
        this.comment = comment;
        yourTurn = false;
        awaitingConfirmation = false;
        gaveUp = false;
        GameSession session = service.getCurrentSession();
        if (session != null && session.getGameMode() == GameMode.ONLINE) service.finishCurrentSession();
    }

    /** Maakt een wachtende zet opnieuw mogelijk nadat de server deze afwijst */
    public void onCommandRejected() {
        if (awaitingConfirmation) {
            awaitingConfirmation = false;
            yourTurn = true;
        }
    }
    /** Maakt een afgewezen forfeit opnieuw mogelijk en herstelt de lokale beurtstatus. */
    public void onForfeitRejected() {
        if (gaveUp) {
            gaveUp = false;
            yourTurn = yourTurnBeforeForfeit;
        }
    }
    /** stuurt dat iemand opgeeft naar de server en blokkeert nieuwe zetten tot de uitslag */
    public void forfeit() {
        GameSession session = service.getCurrentSession();
        if (session == null || !session.isActive() || session.getGameMode() != GameMode.ONLINE || gaveUp) return;
        client.forfeit();
        yourTurnBeforeForfeit = yourTurn;
        gaveUp = true;
        yourTurn = false;
    }

    public boolean isYourTurn() { return yourTurn; }
    public boolean isAwaitingConfirmation() { return awaitingConfirmation; }
    public boolean hasGivenUp() { return gaveUp; }
    public ResultType getResult() { return result; }
    public String getComment() { return comment; }
}

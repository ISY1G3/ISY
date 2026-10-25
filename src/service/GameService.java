package service;

import ai.AiPlayer;
import model.Game;
import model.GameState;
import model.Move;
import model.MoveContext;
import model.GameMode;
import model.TicTacToeGame;
import model.Board;         // constanten van de playerX en player0 marker

import java.util.concurrent.ThreadLocalRandom;      // random functie ALLEEN gebruikt voor kiezen van een beginspeler wanneer nodig

// heeft functies voor het opzetten en beheren van game session
public class GameService implements MoveContext {
    private GameSession currentSession;                 // bewaart de huidige wedstrijd

    public void startGame() {
        // TODO: Define the generic game selection entry point; current UIs use startTicTacToe.
        throw new UnsupportedOperationException("Generic game creation is not implemented yet.");
    }

    public void startTurn() {
        if (currentSession == null) {
            throw new IllegalStateException("No game has been started.");
        }
        Game game = currentSession.getGame();
        if (!game.isFinished()) {
            game.getCurrentPlayer().askForMove(this);
        }
    }

    @Override
    public GameState getGameState() {
        if (currentSession == null) {
            throw new IllegalStateException("No game has been started.");
        }
        return currentSession.getGame().getState();
    }

    @Override
    public void submitMove(Move move) {
        throw new UnsupportedOperationException("Typed move submission is not implemented yet.");
    }

    @Override
    public void setInputEnabled(boolean enabled) {
        throw new UnsupportedOperationException("Input control is not implemented yet.");
    }

    // de startmethode voor de GUI is voor nu bij player vs AI dat speler 2 de AI is
    public GameSession startTicTacToe(GameMode mode, String player1Name, String player2Name, int startingPlayer) {
        return startTicTacToe(mode, player1Name, player2Name, startingPlayer, false, mode == GameMode.PLAYER_VS_AI);
    }

    // Je zal zien dat er 2 startTicTacToe methodes zijn
    // De bovenste ^  kan worden gebruikt voor de GUI die we nu hebben, wss halen wij deze later helemaal weg maar voor nu is dit nog een soort legacy
    // De onderste v  Deze wordt gebruikt door de huidige TUI deze alles van de bovenste + je kan duidelijk aangeven welk van de 2 spelers de AI is of uiteindelijk zelfs beide
    // Voor nu laat ik het zo voor iedergeval de komende demo want ik weet niet of de GUI afkomt voor die tijd.

    public GameSession startTicTacToe(GameMode mode, String player1Name,
                                      String player2Name, int startingPlayer,
                                      boolean player1Ai, boolean player2Ai) {
        //error handling
        if(currentSession != null && currentSession.isActive()) {
            throw new IllegalStateException("wedstrijd is al bezig");
        }
        if(mode == null || player1Name == null || player1Name.isBlank() || player2Name == null || player2Name.isBlank()){
            throw new IllegalArgumentException("niet alle velden zijn goed ingevuld");
        }
        if (player1Ai && player2Ai) {
            throw new IllegalArgumentException("twee AI spelers zijn nog niet ondersteund");
        }
        if ((mode == GameMode.PLAYER_VS_AI) != (player1Ai || player2Ai)) {
            throw new IllegalArgumentException("de spelmodus komt niet overeen met de spelertypes");
        }

        char startingMarker; // bewaart het char van de speler die begint
        switch (startingPlayer) { //bepaal wie begint op basis van de invoer
            case 0:
                // random kiezen X of 0
                // wanneer true kiezen we X anders 0                    //hier v staat eigk een verkorte if else statement
                startingMarker = ThreadLocalRandom.current().nextBoolean() ? Board.PLAYER_X_MARKER : Board.PLAYER_0_MARKER;
                break;
            case 1:
                // speler 1 start
                startingMarker = Board.PLAYER_X_MARKER;
                break;
            case 2:
                // speler 2 start
                startingMarker = Board.PLAYER_0_MARKER;
                break;
            default:
                throw new IllegalArgumentException("ongeldige beginspeler");
        }

        TicTacToeGame game = new TicTacToeGame(player1Ai, player2Ai);
        game.getPlayers().get(0).setName(player1Name);
        game.getPlayers().get(1).setName(player2Name);
        game.setStartingPlayer(startingMarker);         // begin speler wordt ingesteld voor de game begint
        GameSession session = startSession("Tic-Tac-Toe", game, mode, player1Name, player2Name,player1Ai, player2Ai);

        //wanneer de ai start wordt de eerste zet al voor het tonen van het bord gedaan
        playAiTurnIfNeeded(session);
        return session;

    }

    private GameSession startSession(String gameName, Game game, GameMode mode,
                                     String player1Name, String player2Name,
                                     boolean player1Ai, boolean player2Ai) {
        game.start();
        currentSession = new GameSession(gameName, mode, player1Name, player2Name, game, player1Ai, player2Ai);
        return currentSession;
    }

    // laat een player een nummer geven/zet doen tussen de 0 en 8 en daarna evt de AI zet*/
    public boolean makeMove(int boxNumber) {
        GameSession session = requireActiveSession();
        if (!(session.getGame() instanceof TicTacToeGame game)){
            throw new IllegalStateException("het spel omdersteunt nog geen lokale zetten");
        }
        if (isAiTurn(session, game)) {
            throw new IllegalStateException("De AI is nu aan de beurt");
        }
        if (!applyMove(game,boxNumber)) {
            return false;
        }
        playAiTurnIfNeeded(session);
        return true;
    }

    private boolean applyMove(TicTacToeGame game, int boxNumber) {
        if (!game.placeMarker(boxNumber)) {
            return false;
        }
        if(game.isGameOver()) {
            game.finish();
        }
        else{
            game.changeTurn();
        }
        return true;
    }
    private void playAiTurnIfNeeded(GameSession session){
        if (!session.isActive() || !(session.getGame() instanceof TicTacToeGame game) || !isAiTurn(session, game)) {
            return;
        }
        char aiMarker = session.isPlayer1Ai() ? Board.PLAYER_X_MARKER : Board.PLAYER_0_MARKER;
        int move = new AiPlayer(aiMarker).chooseMove(game);
        if (!applyMove(game, move)) {
            throw new IllegalStateException("De ai kan geen geldige zet doen");
        }
    }

    private boolean isAiTurn(GameSession session, TicTacToeGame game) {
        char marker = game.getMarker(game.getCurrentPlayer());
        return marker == Board.PLAYER_X_MARKER ? session.isPlayer1Ai() : session.isPlayer2Ai();
    }
    private GameSession requireActiveSession(){
        if (currentSession == null || !currentSession.isActive()) {
            throw new IllegalStateException("er is geen actieve game");
        }
        return currentSession;
    }

    // opvragen van de sessie of null als er geen sessie is
    public GameSession getCurrentSession() {
        return currentSession;
    }

    //nu moeten we een game nog kunnen sluiten dat komt hier
    public void finishCurrentSession(){
        //check of er een sessie is
        if (currentSession == null){
            throw new IllegalStateException("er bestaat helemaal geen wedstrijd om te eindigen");
        }
        // check of de sessie nog actief is en als dat zo is roepen we finish op van de onderliggende game.
        // bij ons betekent finish dat de gamestatus op FINISHED wordt gezet en het spel stopt
        if (currentSession.isActive()){
            currentSession.getGame().finish();
        }
    }

    // het zou kunnen dat je nadat je klaar bent met spelen een nieuwe game wil starten
    // dus moeten we de currentsession wel geleegd hebben

    public void clearCurrentSession(){
        //checkt of er een sessie is en of deze actief is zo raak je ook niet perongeluk een wedstrijd kwijt
        if (currentSession != null && currentSession.isActive()){
            throw new IllegalStateException("De wedstrijd moet eerst gestopt worden");
        }
        // verwijdert de verwijzing naar de huidige sessie
        currentSession = null;
    }
}

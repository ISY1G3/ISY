package network;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameServerTest {

    @Test
    void parsesLoginOkResponse() {
        GameServer server = new GameServer(new StubConnection());
        ConnectionEvents events = new ConnectionEvents();
        server.addConnectionListener(events);

        server.login("Alice");
        server.handleLine("OK");

        assertTrue(events.loginOk);
        assertNull(events.commandOk);
        assertNull(events.errorReason);
    }

    @Test
    void parsesOkResponseForCommand() {
        GameServer server = new GameServer(new StubConnection());
        ConnectionEvents events = new ConnectionEvents();
        server.addConnectionListener(events);

        server.subscribe("tic-tac-toe");
        server.handleLine("OK");

        assertEquals("subscribe tic-tac-toe", events.commandOk);
        assertNull(events.errorReason);
    }

    @Test
    void parsesErrResponseAndItsReason() {
        GameServer server = new GameServer(new StubConnection());
        ConnectionEvents events = new ConnectionEvents();
        server.addConnectionListener(events);

        server.subscribe("tic-tac-toe");
        server.handleLine("ERR game is unavailable");

        assertEquals("subscribe tic-tac-toe", events.failedCommand);
        assertEquals("game is unavailable", events.errorReason);
    }

    @Test
    void parsesMatchOpponentGameAndStartingPlayer() {
        GameServer server = new GameServer(new StubConnection());
        GameEvents events = new GameEvents();
        server.addGameListener(events);

        server.handleLine("SVR GAME MATCH {OPPONENT: \"Bob\", GAMETYPE: \"tic-tac-toe\", PLAYERTOMOVE: \"Alice\"}");

        assertEquals("Bob", events.opponent);
        assertEquals("tic-tac-toe", events.gameType);
        assertEquals("Alice", events.startingPlayer);
    }

    @Test
    void parsesYourTurnMessage() {
        GameServer server = new GameServer(new StubConnection());
        GameEvents events = new GameEvents();
        server.addGameListener(events);

        server.handleLine("SVR GAME YOURTURN");

        assertEquals(1, events.yourTurnCount);
    }

    @Test
    void parsesMovePlayerAndPosition() {
        GameServer server = new GameServer(new StubConnection());
        GameEvents events = new GameEvents();
        server.addGameListener(events);

        server.handleLine("SVR GAME MOVE {PLAYER: \"Bob\", MOVE: \"4\"}");

        assertEquals("Bob", events.movePlayer);
        assertEquals(4, events.move);
    }

    @ParameterizedTest
    @CsvSource({"WIN, WIN", "LOSS, LOSS", "DRAW, DRAW"})
    void parsesMatchResults(String messageType, ResultType expectedResult) {
        GameServer server = new GameServer(new StubConnection());
        GameEvents events = new GameEvents();
        server.addGameListener(events);

        server.handleLine("SVR GAME " + messageType + " {COMMENT: \"Game finished\"}");

        assertEquals(expectedResult, events.resultType);
        assertEquals("Game finished", events.resultComment);
    }

    @Test
    void canHandleProtocolLinesWithoutARealConnection() {
        StubConnection connection = new StubConnection();
        GameServer server = new GameServer(connection);

        server.login("Alice");
        server.getGameList();

        assertEquals(List.of("login Alice", "get gamelist"), connection.sentCommands);
    }

    private static final class StubConnection extends Connection {
        private final List<String> sentCommands = new ArrayList<>();

        @Override
        public void send(String text) {
            sentCommands.add(text);
        }
    }

    private static final class ConnectionEvents implements ConnectionListener {
        private boolean loginOk;
        private String commandOk;
        private String failedCommand;
        private String errorReason;

        @Override
        public void onLoginOk() {
            loginOk = true;
        }

        @Override
        public void onCommandOk(String command) {
            commandOk = command;
        }

        @Override
        public void onCommandError(String command, String reason) {
            failedCommand = command;
            errorReason = reason;
        }
    }

    private static final class GameEvents implements GameListener {
        private String opponent;
        private String gameType;
        private String startingPlayer;
        private int yourTurnCount;
        private String movePlayer;
        private int move = -1;
        private ResultType resultType;
        private String resultComment;

        @Override
        public void onMatch(String opponent, String gameType, String startingPlayer) {
            this.opponent = opponent;
            this.gameType = gameType;
            this.startingPlayer = startingPlayer;
        }

        @Override
        public void onYourTurn() {
            yourTurnCount++;
        }

        @Override
        public void onMove(String player, int move) {
            movePlayer = player;
            this.move = move;
        }

        @Override
        public void onResult(ResultType type, String comment) {
            resultType = type;
            resultComment = comment;
        }
    }
}

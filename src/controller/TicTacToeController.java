package controller;

import model.Board;
import model.GameMode;
import model.TicTacToeGame;
import service.GameService;
import service.GameSession;

/**
 * Verbindt beide JavaFX-views met de gedeelde GameService.
 */
public class TicTacToeController {

    public interface GameView {
        void showMessage(String message);

        void setMarker(int boxNumber, char marker);

        void showGameResult(String gameResult);
    }

    private int scoreP1;
    private int scoreP2;
    private final GameService gameService = new GameService();
    private GameView ticTacToeGameView;
    private Runnable goToMenu;
    private String playerXName = "Player X";
    private String player0Name = "Player 0";

    public void initView(GameView view) {
        this.ticTacToeGameView = view;
    }

    public void startGame(GameMode gameMode) {
        startGame(gameMode, Board.PLAYER_X_MARKER);
    }

    public void startGame(GameMode gameMode, char startingMarker) {
        startGame(gameMode, startingMarker, Board.PLAYER_0_MARKER,
                "Player X", "Player 0");
    }

    public void startGame(GameMode gameMode, char startingMarker, char aiMarker,
                          String playerXName, String player0Name) {
        if (ticTacToeGameView == null) {
            throw new IllegalStateException("View moet eerst gekoppeld worden");
        }

        int startingPlayer;
        if (startingMarker == Board.PLAYER_X_MARKER) {
            startingPlayer = 1;
        } else if (startingMarker == Board.PLAYER_0_MARKER) {
            startingPlayer = 2;
        } else {
            throw new IllegalArgumentException("geen geldige beginspeler");
        }

        if (gameMode == null || playerXName == null || player0Name == null) {
            throw new IllegalArgumentException("Game mode and player names are required");
        }
        if (gameMode == GameMode.PLAYER_VS_AI
                && aiMarker != Board.PLAYER_X_MARKER
                && aiMarker != Board.PLAYER_0_MARKER) {
            throw new IllegalArgumentException("geen geldige AI marker");
        }

        closeOldSession();
        this.playerXName = playerXName;
        this.player0Name = player0Name;

        boolean player1Ai = gameMode == GameMode.PLAYER_VS_AI
                && aiMarker == Board.PLAYER_X_MARKER;
        boolean player2Ai = gameMode == GameMode.PLAYER_VS_AI
                && aiMarker == Board.PLAYER_0_MARKER;
        gameService.startTicTacToe(
                gameMode, playerXName, player0Name, startingPlayer, player1Ai, player2Ai);
        refreshBoardAndTurn();
    }

    public TicTacToeGame getGame() {
        GameSession session = gameService.getCurrentSession();
        if (session == null || !(session.getGame() instanceof TicTacToeGame game)) {
            throw new IllegalStateException("Er is nog geen TTT-game gestart");
        }
        return game;
    }

    public void handleInputFromUser(int boxNumber) {
        try {
            if (!gameService.makeMove(boxNumber)) {
                ticTacToeGameView.showMessage("dit vakje is al bezet of is niet geldig");
                return;
            }
        } catch (IllegalStateException e) {
            ticTacToeGameView.showMessage(e.getMessage());
            return;
        }

        refreshBoardAndTurn();
        if (!gameService.getCurrentSession().isActive()) {
            showGameResult();
        }
    }

    private void refreshBoardAndTurn() {
        TicTacToeGame game = getGame();
        char[][] board = game.getBoard();
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                char marker = board[row][col];
                if (marker != Board.EMPTY_MARKER) {
                    ticTacToeGameView.setMarker(row * Board.SIZE + col, marker);
                }
            }
        }

        if (game.isActive()) {
            char marker = game.getCurrentMarker();
            String playerName = marker == Board.PLAYER_X_MARKER ? playerXName : player0Name;
            ticTacToeGameView.showMessage("turn: " + playerName + " (" + marker + ")");
        }
    }

    private void showGameResult() {
        TicTacToeGame game = getGame();
        if (game.playerXWon()) {
            scoreP1++;
            ticTacToeGameView.showGameResult(playerXName + " (X) won!");
        } else if (game.player0Won()) {
            scoreP2++;
            ticTacToeGameView.showGameResult(player0Name + " (0) won!");
        } else {
            ticTacToeGameView.showGameResult("Draw!");
        }
    }

    public int getScoreP1(int score) {
        if (score != 0) {
            scoreP1 = score;
        }
        return scoreP1;
    }

    public int getScoreP2(int score) {
        if (score != 0) {
            scoreP2 = score;
        }
        return scoreP2;
    }

    public void initGoToMenu(Runnable goToMenu) {
        this.goToMenu = goToMenu;
    }

    private void closeOldSession() {
        if (gameService.getCurrentSession() != null) {
            gameService.finishCurrentSession();
            gameService.clearCurrentSession();
        }
    }

    public void resetGame() {
        if (goToMenu == null) {
            throw new IllegalStateException("Menu callback has not been initialized");
        }
        goToMenu.run();
    }
}

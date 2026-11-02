# Reconciliation plan

## Basis for the proposal

The comparison uses the corrected `main.mmd` and `JordyStaging.mmd`, plus the older `PlayerAbstractArchitecture`, `ClientServerArchitecture`, and sequence diagrams. The diagrams were also checked against the corresponding source structures so that the proposal does not preserve accidental omissions in UML.

The target keeps the strongest part of `JordyStaging`: the event-queued TUI, real online lobby/game flow, abstract player types, and server-confirmed online moves. It keeps the usable FXML GUI from `main`, but removes both branches' duplicate and legacy JavaFX paths.

## Comparison summary

| Concern | `main` | `JordyStaging` | Old plan | Target decision |
|---|---|---|---|---|
| Player model | One marker-only `Player` | Abstract `Player` with local, remote, and AI types | Same polymorphic player hierarchy | Keep Jordy's hierarchy and finish the abstraction |
| Moves and state | Raw integer moves and mutable game access | Adds `Move`, `GameState`, `MoveContext`, and `MoveStrategy`, but wraps the live game | Typed moves and state snapshots | Keep the interfaces; add immutable Tic-Tac-Toe implementations |
| AI | One concrete minimax `ai.AiPlayer` | Generic AI player plus the old concrete AI engine | AI player delegates to a strategy | Rename the engine to `TicTacToeMinimaxStrategy`; retain one generic `AIPlayer` |
| Local game orchestration | `GameService` drives Tic-Tac-Toe directly | Keeps lifecycle generic while `GameService` explicitly coordinates Tic-Tac-Toe | `GameService` also acts as the move context | Keep Jordy's lifecycle-only `Game`; explicit `TicTacToeGame` checks belong in `GameService` |
| Online play | Demo-only TUI path | Real connection, lobby, challenges, online match, confirmation, and result flow | Remote input enters through the application service | Port Jordy's behavior, then separate it from TUI navigation |
| TUI concurrency | Blocking input loop | One queue serializes input and server callbacks | Sequence assumes controlled turn lifecycle | Keep Jordy's event queue |
| Network callbacks | Split listeners plus unused aggregate listener | Same, actively used by TUI adapter | One aggregate `ServerListener` | Keep split event roles; delete the redundant aggregate listener |
| GUI | FXML GUI plus an older hand-built JavaFX GUI | Removes FXML and keeps a smaller hand-built JavaFX path | UI-agnostic application layer | Keep the FXML GUI from `main`; delete hand-built legacy GUI after migration |
| Navigation | TUI navigator plus repeated FXML loading | TUI navigator expanded for real online screens | Dedicated navigation sequence | Keep TUI navigator; add one GUI `SceneNavigator` |
| View data | Views read sessions/games directly | Same, with more online state references | UI receives state and submits moves | Keep session-based rendering initially; add narrow read models only where they remove duplication |

## What to keep

### From `JordyStaging`

- `TuiApplication`'s single event queue for keyboard input and network callbacks.
- The online TUI screens and controllers: online game, result, lobby refresh, challenges, forfeit, and connection recovery.
- `OnlineGameCoordinator`'s rule that an online move changes the local board only after the server confirms it with `MOVE`.
- `Player` as an abstract domain type with `LocalPlayer`, `RemotePlayer`, and `AIPlayer` implementations.
- `Move`, `GameState`, `MoveContext`, and `MoveStrategy` as game-independent abstractions.
- The split network listeners rather than the old all-in-one `ServerListener`.

### From `main`

- The FXML-based GUI and its resources as the one supported GUI implementation.
- The current local setup flow and shared `GameService` entry point.
- The small, testable `Board` and Tic-Tac-Toe rules, with a defensive snapshot used only when the AI needs a `GameState`.

### From the old plan

- Players request moves through `MoveContext`; they do not read from the terminal, manipulate JavaFX, or write directly to the board.
- AI depends on a `MoveStrategy`, not on Tic-Tac-Toe implementation details.
- Local UI input and confirmed remote input both enter through Jordy's `GameService` methods.
- UI code may render the current session, but never owns game rules or mutates the board.

## Problems to remove

1. `ai.AiPlayer` and `model.player.AIPlayer` represent two different ideas under the same name. Replace the former with `TicTacToeMinimaxStrategy`; keep the latter as the generic player.
2. `TicTacToeTurnState` currently wraps the mutable `TicTacToeGame`, so an AI strategy can still reach live state. Keep Jordy's `GameState` adapter, but copy only the board, marker, and valid moves into it.
3. Keep `Game` deliberately small: `start`, `finish`, and `getGameStatus`. `GameService` may explicitly validate and cast to `TicTacToeGame` because it owns the Tic-Tac-Toe use cases.
4. `TuiOnlineClient` owns networking, mutable lobby data, listener translation, screen navigation, and error printing. Split this into `LobbyService`, `OnlineGameCoordinator`, and a `ServerClient` infrastructure adapter.
5. `GameServer` is client-side protocol code and is misleadingly named. Rename it `ServerProtocol`; rename the public network facade `NetworkClient`.
6. `ServerListener` duplicates `GameListener`, `LobbyListener`, and `ConnectionListener`. Delete it. Remove `PrintListener` or move it to test/debug sources.
7. Keep Tic-Tac-Toe move orchestration and session lifecycle in `GameService`, as Jordy designed it. Move only minimax calculation into a strategy and online protocol flags into `OnlineGameCoordinator`.
8. `main` contains two GUI stacks: FXML controllers and `view/TicTacToeGameApp`/`TicTacToeGameView`/`SettingsScreen`/`StartScreen`. Keep FXML and delete the legacy stack after feature parity.
9. `Main extends Application` while also running a console mode selector. Use separate `GuiApplication` and `TuiApplication` entry points; make `Main` a small argument-based launcher or remove it.
10. Views and controllers may read the active `GameSession`, as in Jordy's design, but must submit changes through `GameService` or `OnlineGameCoordinator` rather than mutating the model.

## Migration sequence

### Phase 1 — Safety net and branch import

1. Add tests on `main` for local PvP, PvAI with either starting player, win/draw detection, invalid moves, session cleanup, and GUI controller startup.
2. Add protocol tests around `GameServer.handleLine` behavior before renaming it.
3. Port from `JordyStaging` in coherent slices, not as a branch-wide merge:
   - event-queued `TuiApplication` and the corresponding `TuiNavigator` changes;
   - `TuiOnlineClient`, `OnlineGameCoordinator`, online controllers/views, and expanded lobby/login data;
   - player abstractions and online-aware `GameService`/`GameSession` changes.
4. Keep each slice compiling and tested before taking the next one.

Exit criterion: `main` has Jordy's working TUI and online flow without changing the GUI behavior.

### Phase 2 — Consolidate the domain model

1. Introduce `Move`, `GameState`, `MoveContext`, `MoveStrategy`, and `model.player.*` from `JordyStaging`.
2. Replace `model.Player` with the abstract `model.player.Player` hierarchy.
3. Keep `TicTacToeMove` as the typed move passed through `MoveContext` for AI selection.
4. Keep `Game` unchanged as the lifecycle contract used by `GameSession`.
5. Keep `GameService.applyMove(TicTacToeGame, int)` responsible for placing a marker, finishing the game, or changing the turn.
6. Make the AI-facing `TicTacToeTurnState` a defensive snapshot so strategies cannot mutate the live game.

Exit criterion: controllers never mutate the game directly; all local and confirmed online moves pass through the explicit Tic-Tac-Toe operations in `GameService`.

### Phase 3 — Remove duplicate AI and simplify turn handling

1. Rename/refactor `ai.AiPlayer` to `TicTacToeMinimaxStrategy implements MoveStrategy`.
2. Delete marker ownership from the strategy; derive the active marker from `TicTacToeTurnState`.
3. Let generic `AIPlayer.askForMove` submit the returned `TicTacToeMove` through `MoveContext`.
4. Use the same turn path for local AI and online local AI. The online coordinator sends the suggestion and waits for server confirmation; local games apply it immediately through the service.

Exit criterion: there is one AI player abstraction and one Tic-Tac-Toe strategy implementation.

### Phase 4 — Separate online application logic from TUI

1. Extract a `ServerClient` interface from the operations currently exposed by `network.Client`.
2. Rename `Client` to `NetworkClient` and `GameServer` to `ServerProtocol` while preserving their tested behavior.
3. Move connection and lobby lifecycle from `TuiOnlineClient` into `LobbyService`.
4. Keep match protocol state in `OnlineGameCoordinator` and expose immutable `OnlineMatchState`.
5. Translate socket-thread callbacks onto a caller-provided dispatcher. The TUI supplies its event queue; JavaFX supplies `Platform::runLater`.
6. Make TUI controllers decide navigation in response to state/events. Infrastructure must not call `TuiNavigator` or print UI messages.

Exit criterion: the online application layer can be tested with a fake `ServerClient` and used by either UI.

### Phase 5 — Bring both presentations onto the same application API

1. Retain the FXML files and controllers from `main`.
2. Add a small `SceneNavigator` so FXML controllers do not repeatedly load files and construct stages themselves.
3. Inject the shared `GameService`, `LobbyService`, and `OnlineGameCoordinator` into GUI controllers.
4. Let both GUI and TUI render the active `GameSession`; centralize repeated Tic-Tac-Toe display extraction in a small read-only helper only if needed.
5. Preserve Jordy's TUI view/controller pairs, but replace mutable `LoginData`, `LobbyData`, and result parameters with the new request/state records where practical.

Exit criterion: GUI and TUI use the same domain and application services; only rendering and input handling differ.

### Phase 6 — Delete legacy paths

Delete only after the replacement path has tests and feature parity:

- `src/view/TicTacToeGameApp.java`
- `src/view/TicTacToeGameView.java`
- `src/view/SettingsScreen.java`
- `src/view/StartScreen.java`
- the root `src/TicTacToeGameApp.java` if imported from `JordyStaging`
- `src/model/Player.java`
- `src/ai/AiPlayer.java` after `TicTacToeMinimaxStrategy` replaces it
- `src/network/ServerListener.java`
- `src/network/PrintListener.java` from production sources
- `src/ui/tui/controller/GameDemoController.java`
- `showOnlineGameDemo` and all demo-only state/constructors
- obsolete `TicTacToeController` overloads, score setters, and menu callbacks once both UIs use the services directly

Then remove dead resources, fix package names, and enforce a rule that packages under `model` and `service` import neither `ui.*` nor JavaFX.

## Recommended pull-request boundaries

1. **Port Jordy TUI event loop and online screens** — behavior-preserving.
2. **Introduce player/move/state abstractions** — no network rename yet.
3. **Create the defensive AI turn state and strategy** — delete duplicate AI.
4. **Extract online services and `ServerClient` port** — add fake-client tests.
5. **Move FXML GUI to shared services** — add `SceneNavigator`.
6. **Delete legacy GUI, demo online flow, and duplicate listeners**.

Each PR should compile with `./mvnw test` and should not mix feature porting with broad renames or deletions.

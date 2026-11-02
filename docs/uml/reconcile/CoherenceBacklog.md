# Coherence and technical-debt backlog

## Goal

Make the current merged application coherent and remove technical debt without adding product scope.

The desired result is:

- one supported FXML GUI;
- one supported online-capable TUI;
- one shared `GameService` and Tic-Tac-Toe implementation;
- the existing `Game` interface unchanged;
- one player hierarchy;
- one minimax strategy path;
- one clear launcher;
- no legacy GUI, demo code, dead screens, or duplicate listeners.

## Explicitly out of scope

- Automated testing or a test framework.
- Reversi or another game.
- New tournament-specific UI or protocol work; existing server-driven tournament matches must keep using the normal online match flow.
- Online play in the GUI.
- A generic game-engine framework.
- Expanding `Game` beyond `start`, `finish`, and `getGameStatus`.
- Replacing the existing explicit `TicTacToeGame` handling in `GameService`.
- Dependency-injection frameworks, event buses, persistence, or broad UI redesign.
- A network-protocol rewrite.
- Broad package or naming rewrites with no immediate coherence benefit.

## Verification rule

Because testing is out of scope, each ticket uses only:

- Java 21 compilation through the Maven Wrapper:
  - Windows: `mvnw.cmd package -DskipTests`;
  - Linux/macOS: `./mvnw package -DskipTests`;
  - optional NixOS shell: `nix develop -c ./mvnw package -DskipTests`;
- a short manual check limited to the paths touched by that ticket;
- source inspection for dead references and conflict markers.

## Priority order

APP-003 is the first ticket to execute. Its numeric identifier is retained only to avoid renumbering the existing backlog.

| Ticket | Story points |
|---|---:|
| APP-003 — Delete legacy GUI | 2 |
| APP-001 — Consolidate AI | 5 |
| APP-002 — Complete FXML GUI | 5 |
| APP-005 — Remove dead paths | 3 |
| APP-006 — Remove dead APIs | 2 |
| APP-007 — Final verification and documentation | 2 |
| **Total** | **19** |

```text
APP-003 Delete legacy GUI and simplify launcher
            │
            ├── APP-002 Complete FXML GUI parity
            └── APP-001 Consolidate AI
            │
            ▼
APP-005 Remove dead screens and duplicate networking APIs
            │
            ▼
APP-006 Remove dead APIs and implementation debris
            │
            ▼
APP-007 Final verification and documentation
```

---

## APP-001 — Consolidate the two AI implementations

**Priority:** P0  
**Story points:** 5  
**Dependencies:** None  
**Can run in parallel with:** APP-002, APP-003

### User story

As a developer, I want one clearly named generic AI player and one Tic-Tac-Toe minimax strategy so the duplicate `AIPlayer` classes cannot be confused or diverge.

### Work

- Keep `model.player.AIPlayer` as the generic player.
- Rename `ai.AiPlayer` to `TicTacToeMinimaxStrategy`.
- Make the minimax class implement `MoveStrategy`.
- Preserve the existing minimax algorithm.
- Keep `Move`, `GameState`, `MoveContext`, and `MoveStrategy`.
- Replace `TicTacToeTurnState(TicTacToeGame game)` with a defensive state containing only:
  - copied board cells;
  - current marker;
  - valid moves.
- Ensure local and online AI choices both use `AIPlayer.askForMove` and `MoveContext`.
- Preserve the different execution rules:
  - local AI moves apply immediately through `GameService`;
  - online AI moves wait for server confirmation.
- Do not expand `Game`.
- Keep explicit `TicTacToeGame` handling inside `GameService`.
- Preserve the TUI event queue, stale-client callback guard, and existing connection/session cleanup when touching online AI code.

### Manual check

- Start a local PvAI game with the human first.
- Start a local PvAI game with the AI first.
- Confirm the AI makes only valid moves.
- Start the online-AI path and confirm the board changes only after server confirmation.

### Definition of done

- Only `model.player.AIPlayer` is named `AIPlayer`.
- The minimax implementation is named `TicTacToeMinimaxStrategy` and implements `MoveStrategy`.
- AI state exposes no live `TicTacToeGame` or mutable board reference.
- Local and online AI behavior still works in the manual checks.
- `Game` remains unchanged.
- Java 21 package build succeeds.

---

## APP-002 — Make the FXML GUI the complete supported GUI

**Priority:** P0  
**Story points:** 5  
**Dependencies:** None  
**Can run in parallel with:** APP-001

### User story

As a user, I want the supported FXML GUI to retain all existing local-game behavior after the duplicate hand-built JavaFX GUI is removed.

### Work

- Verify and repair only the following existing behavior:
  - local PvP;
  - local PvAI;
  - choosing which side is AI;
  - choosing the starting player;
  - player names;
  - board updates;
  - win and draw results;
  - returning to the menu.
- Keep the existing `TicTacToeController` and `GameService`.
- Keep the current FXML layout unless a defect prevents use.
- Leave the repeated FXML loading code in place unless it causes a concrete defect during this work.
- Do not add online GUI support.
- Do not add another game mode.

### Manual check

- Open every FXML screen.
- Complete one PvP game.
- Complete one PvAI game with the AI as player 2.
- Start one PvAI game with the AI as player 1.
- Return to the menu from the game screen.

### Definition of done

- All three FXML files load without exceptions.
- The listed PvP and PvAI flows work.
- Configured names, AI side, and starting player reach the game correctly.
- Java 21 package build succeeds.

---

## APP-003 — Delete the legacy GUI and simplify the launcher

**Priority:** P0  
**Story points:** 2  
**Dependencies:** None  
**Can run in parallel with:** APP-001

### User story

As a maintainer, I want one GUI implementation and one understandable entry point so fixes are not duplicated across incompatible JavaFX stacks.

### Work

- Delete:
  - `src/view/TicTacToeGameApp.java`;
  - `src/view/TicTacToeGameView.java`;
  - `src/view/SettingsScreen.java`;
  - `src/view/StartScreen.java`.
- Remove GUI option 3 from `Main`.
- Keep only:
  - option 1: TUI;
  - option 2: FXML GUI.
- Update README startup instructions.

### Manual check

- Start the TUI from `Main`.
- Start the FXML GUI from `Main`.
- Start and finish one local game in each interface.

### Definition of done

- Only the FXML GUI remains.
- `Main` offers exactly TUI and FXML GUI.
- No source, resource, menu, or documentation reference points to the deleted GUI.
- The TUI and FXML GUI both launch from `Main`.
- Java 21 package build succeeds.

---

## APP-005 — Remove dead screens and duplicate network APIs

**Priority:** P1  
**Story points:** 3  
**Dependencies:** APP-003

### User story

As a user and developer, I want every visible option and production callback API to represent real behavior so the application has no misleading dead ends.

### Work

- Delete the confirmed placeholder TUI screens:
  - `SettingsController` and `SettingsView`;
  - `TournamentController` and `TournamentView`;
  - their navigator methods and menu entries.
- Preserve tournament-triggered matches handled through the existing `MATCH`, `YOURTURN`, `MOVE`, and `RESULT` flow.
- Do not add a separate tournament workflow; the normal online match flow remains authoritative.
- Delete `ServerListener` after confirming the split listeners cover every current callback.
- Retain:
  - `ConnectionListener`;
  - `LobbyListener`;
  - `GameListener`.
- Keep `PrintListener` as the existing manual protocol-debugging utility.
- Do not rewrite the protocol.
- Preserve the TUI event queue, stale-client guard, connection/session cleanup, and server-confirmed move handling.

### Manual check

- Open the TUI main menu and lobby.
- Open the FXML main menu.
- Confirm every enabled option leads to implemented behavior.
- Connect to the server and confirm connection, lobby, and game callbacks still work.

### Definition of done

- No placeholder settings or dedicated tournament screen remains.
- Tournament-triggered matches still use the normal online game and result screens.
- No enabled unavailable-game control remains.
- `ServerListener` is deleted with no references.
- Split listener callbacks still work in the manual online flow.
- Java 21 package build succeeds.

---

## APP-006 — Remove dead APIs and implementation debris

**Priority:** P2  
**Story points:** 2  
**Dependencies:** APP-001, APP-002, APP-003, APP-005

### User story

As a developer, I want the remaining code to expose only live behavior so future work starts from a small, understandable baseline.

### Work

- Remove unused `GameService` overloads after checking every caller.
- Remove unused controller methods, constructors, fields, and callbacks created or exposed by the deleted paths.
- Remove all remnants of the deleted marker-only `model.Player`.
- Keep marker assignment inside `TicTacToeGame`.
- Keep the existing explicit Tic-Tac-Toe orchestration inside `GameService`.
- Do not perform broad formatting, package movement, or speculative abstraction.
- Do not rename `Client` or `GameServer` unless requested separately.

### Manual check

- Run local PvP and PvAI in the TUI.
- Run local PvP and PvAI in the FXML GUI.
- Run the online login, lobby, match, and result path.

### Definition of done

- Every remaining public method has a production caller or protocol purpose.
- No dead field, constructor, callback, or public method from a removed path remains.
- Deleted classes have no remaining references.
- The diff contains no unrelated formatting churn.
- Java 21 package build succeeds.

---

## APP-007 — Final verification and current-state documentation

**Priority:** P2  
**Story points:** 2  
**Dependencies:** APP-001, APP-002, APP-003, APP-005, APP-006

### User story

As a maintainer, I want the final documentation and UML to match the actual cleaned implementation so the next developer does not have to reconstruct it from stale plans.

### Work

- Run the Java 21 package build.
- Perform the complete manual checklist:
  - TUI local PvP;
  - TUI local PvAI;
  - TUI online login and lobby;
  - TUI confirmed online move and result;
  - FXML local PvP;
  - FXML local PvAI;
  - return to menu and exit paths.
- Regenerate the full Mermaid class diagram.
- Update README run and architecture notes.
- Check for conflict markers and references to deleted classes.

### Definition of done

- The platform-appropriate Maven Wrapper command succeeds from a clean checkout using JDK 21.
- The complete manual checklist passes.
- Current-state UML contains every production class and no deleted class.
- README documents only supported functionality.
- No conflict marker, commented-out replacement, or stale current-state document remains.

---

## Work division

Work can be divided into two independent lanes:

| Lane | Ticket | Main files owned |
|---|---|---|
| AI/domain | APP-001 | `ai`, `model/player`, AI-related sections of `GameService` |
| GUI | APP-003, then APP-002 | legacy `view`, `Main`, then `ui/gui` and `TicTacToeController` |

After those lanes merge:

1. Complete APP-005.
2. Complete APP-006.
3. Complete APP-007 last.

Avoid simultaneous edits to:

- `Main.java` outside APP-003;
- `TuiNavigator.java` outside APP-005.

## Global definition of done

Every ticket must:

- stay inside its stated scope;
- compile with Java 21;
- complete its focused manual check;
- avoid unrelated refactors or features;
- leave no conflict markers or commented-out replacement code;
- update the current-state UML when class structure changes.

The backlog is complete when the repository has one FXML GUI, one online-capable TUI, one shared game implementation, one AI strategy path, one launcher, and no legacy or placeholder production path.

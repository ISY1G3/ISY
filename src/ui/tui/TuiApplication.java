package ui.tui;

import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import service.GameService;

/** Laat UI-input en servercallbacks door dezelfde TUI-thread verwerken. */
public class TuiApplication {
    private final Scanner scanner;
    private final BlockingQueue<Runnable> events = new LinkedBlockingQueue<>();
    private final GameService gameService = new GameService();
    private final TuiNavigator navigator = new TuiNavigator(gameService, events::offer);
    private final TuiInputHandler inputHandler = new TuiInputHandler(navigator);

    public TuiApplication(Scanner scanner) { this.scanner = Objects.requireNonNull(scanner); }
    public TuiApplication() { this(new Scanner(System.in)); }

    public void start() {
        Thread reader = new Thread(() -> {
            try {
                while (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    events.offer(() -> inputHandler.handle(input));
                }
            } catch (IllegalStateException e) {
                // Scanner gesloten tijdens afsluiten.
            } finally {
                events.offer(navigator::stop);
            }
        }, "tui-input");
        reader.setDaemon(true);
        reader.start();

        navigator.showMainMenu();
        System.out.print("> ");
        try {
            while (navigator.isRunning()) {
                Runnable event = events.take();
                try { event.run(); }
                catch (RuntimeException e) { System.out.println("Fout: " + e.getMessage()); }
                if (navigator.isRunning()) System.out.print("> ");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            navigator.getOnline().disconnect();
        }
        System.out.println("TUI gestopt");
    }
    public static void main(String[] args) { new TuiApplication().start(); }
}

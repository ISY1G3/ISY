/**
 * Gewoon Java-entrypoint voor de uitvoerbare jar.
 *
 * De Java-launcher behandelt een manifest-mainclass die rechtstreeks van
 * JavaFX Application erft speciaal en verwacht dan externe JavaFX-modules.
 * Deze wrapper laat de meegepackte JavaFX-runtime via het normale classpath
 * starten.
 */
public final class Launcher {
    private Launcher() {
    }

    public static void main(String[] args) {
        Main.main(args);
    }
}

package ui.tui.view;

/** Settings kunnen al getoond worden zonder de definitieve instellingenmodule. */
public class SettingsView extends TuiView {
    @Override
    public void draw() {
        System.out.println("\n================================");
        System.out.println("            SETTINGS");
        System.out.println("================================");
        System.out.println("Taal: Engels (instelbaar later)");
        System.out.println("Keyboard input: aan (TUI)");
        System.out.println("[0] Back");
    }
}

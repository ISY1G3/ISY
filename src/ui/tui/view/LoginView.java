package ui.tui.view;

import ui.tui.LoginData;

// deze versie is nog niet verbonden met de server
public class LoginView extends TuiView {
    private final LoginData data;
    public LoginView(LoginData data) { this.data = data; }

    @Override
    public void draw() {
        System.out.println("\n================================");
        System.out.println("        LOGIN TO SERVER");
        System.out.println("================================");
        System.out.println("[1] Server hostname/IP: " + data.getHostname());
        System.out.println("[2] Nickname: " + data.getNickname());
        System.out.println("[3] Open lobby (DEMO, geen verbinding)");
        System.out.println("[0] Back");
    }

    public void askValue(String label) {
        System.out.println("Vul " + label + " in (of 'back' om te annuleren):");
    }
}

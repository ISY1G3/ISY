package ui.tui.view;

import ui.tui.LoginData;

public class LoginView extends TuiView {
    private final LoginData data;
    public LoginView(LoginData data) { this.data = data; }
    @Override public void draw() {
        System.out.println("\n========== ONLINE LOGIN ==========");
        System.out.println("[1] Server:   " + data.getHostname());
        System.out.println("[2] Nickname: " + data.getNickname());
        System.out.println("[3] Poort:    " + data.getPort());
        System.out.println("[4] Verbinden en inloggen");
        System.out.println("[0] Terug naar hoofdmenu");
    }
    public void askValue(String label) { System.out.println("Vul " + label + " in (of back):"); }
}

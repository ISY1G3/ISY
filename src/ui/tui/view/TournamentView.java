package ui.tui.view;
// ONLINE
// toernooischerm nu nog zonder echt info
public class TournamentView extends TuiView {
    @Override
    public void draw() {
        System.out.println("\n========== TOERNOOIEN ==========");
        System.out.println("Toernooien worden gestart door een admin");
        System.out.println("Ingelogde spelers worden automatisch ingedeeld");
        System.out.println("Zodra de admin een tournooi start,");
        System.out.println("verschijnt het gamescherm automatisch.");
        System.out.println("[0] Terug naar lobby");
    }
}

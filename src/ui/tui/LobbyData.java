package ui.tui;

// dit is alleen ter demonstratie en is gegenereerd
public class LobbyData {
    private final String nickname;
    private boolean subscribedTicTacToe = true;
    public LobbyData(String nickname) { this.nickname = nickname; }
    public String getNickname() { return nickname; }
    public boolean isSubscribedTicTacToe() { return subscribedTicTacToe; }
    public void toggleSubscribedTicTacToe() { subscribedTicTacToe = !subscribedTicTacToe; }
}

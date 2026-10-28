package ui.tui;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LobbyData {
    public record Challenge(int number, String challenger, String game) {}
    private final String nickname;
    private final Map<Integer, Challenge> challenges = new LinkedHashMap<>();
    private List<String> players = new ArrayList<>();
    private List<String> games = new ArrayList<>();
    private String ticTacToeServerName;
    private boolean subscribed;
    // De speler achter DEZE client; de andere kant kiest zijn eigen instelling.
    private boolean localAi;

    public LobbyData(String nickname) { this.nickname = nickname; }
    public String getNickname() { return nickname; }
    public List<String> getPlayers() { return List.copyOf(players); }
    public List<String> getGames() { return List.copyOf(games); }
    public List<Challenge> getChallenges() { return List.copyOf(challenges.values()); }
    public String getTicTacToeServerName() { return ticTacToeServerName; }
    public boolean isSubscribedTicTacToe() { return subscribed; }
    public boolean isLocalAi() { return localAi; }
    public void toggleLocalAi() { localAi = !localAi; }
    public void setPlayers(List<String> list) { players = List.copyOf(list); }
    public void setGames(List<String> list) {
        games = List.copyOf(list);
        ticTacToeServerName = games.stream()
                .filter(n -> n.toLowerCase().replaceAll("[^a-z]", "").equals("tictactoe"))
                .findFirst().orElse(null);
    }
    public void setSubscribed(boolean value) { subscribed = value; }
    public void addChallenge(String from, String game, int nr) {
        challenges.put(nr, new Challenge(nr, from, game));
    }
    public boolean hasChallenge(int nr) { return challenges.containsKey(nr); }
    public void removeChallenge(int nr) { challenges.remove(nr); }
}

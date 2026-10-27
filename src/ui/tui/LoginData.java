package ui.tui;

public class LoginData {
    private String hostname = "127.0.0.1";
    private int port = 7789;
    private String nickname = "Speler";
    public String getHostname() { return hostname; }
    public int getPort() { return port; }
    public String getNickname() { return nickname; }
    public void setHostname(String value) { hostname = value; }
    public void setPort(int value) { port = value; }
    public void setNickname(String value) { nickname = value; }
}

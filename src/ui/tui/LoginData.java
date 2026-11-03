package ui.tui;

import network.Client;

public class LoginData {
    private String hostname = Client.DEFAULT_HOST;
    private int port = Client.DEFAULT_PORT;
    private String nickname = "Speler";
    public String getHostname() { return hostname; }
    public int getPort() { return port; }
    public String getNickname() { return nickname; }
    public void setHostname(String value) { hostname = value; }
    public void setPort(int value) { port = value; }
    public void setNickname(String value) { nickname = value; }
}

package ui.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.Client;
import network.ConnectionListener;
import service.GameService;
import ui.gui.FxmlViews;
import ui.gui.GuiOnlineClient;

import java.io.IOException;

public class ConnectionController {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private TextField usernameField;
    @FXML
    private Button connectButton;

    private volatile Client client;
    private volatile boolean disconnectRequested;

    @FXML
    private void initialize() {
        // Keep the form fields usable, but start with no field focused.
        hostField.setText(Client.DEFAULT_HOST);
        portField.setText(String.valueOf(Client.DEFAULT_PORT));
        Platform.runLater(rootPane::requestFocus);
    }

    @FXML
    private void connect(ActionEvent event) {
        String host = hostField.getText().trim();
        String username = usernameField.getText().trim();
        final int port;

        if (host.isEmpty() || username.isEmpty()) {
            showWarning("Enter a host and username.");
            return;
        }

        try {
            port = Integer.parseInt(portField.getText().trim());
            if (port < 1 || port > 65535) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showWarning("Port must be a number from 1 to 65535.");
            return;
        }

        Client previousClient = client;
        if (previousClient != null && previousClient.isConnected()) {
            previousClient.disconnect();
        }

        Client attempt = new Client();
        client = attempt;
        disconnectRequested = false;
        connectButton.setDisable(true);

        attempt.addConnectionListener(new ConnectionListener() {
            @Override
            public void onLoginOk() {
                Platform.runLater(() -> {
                    if (client == attempt) {
                        try {
                            openOnlineMenu(attempt, username);
                        } catch (IOException e) {
                            client = null;
                            attempt.disconnect();
                            connectButton.setDisable(false);
                            showWarning("Could not open OnlineMenu: " + e.getMessage());
                        }
                    }
                });
            }

            @Override
            public void onError(String reason) {
                Platform.runLater(() -> {
                    if (client == attempt) {
                        client = null;
                        attempt.disconnect();
                        connectButton.setDisable(false);
                        showWarning("Server rejected the connection: " + reason);
                    }
                });
            }

            @Override
            public void onDisconnect() {
                Platform.runLater(() -> {
                    if (client == attempt) {
                        client = null;
                        connectButton.setDisable(false);
                        showWarning("The connection to the server was lost.");
                    }
                });
            }
        });

        Thread connectionThread = new Thread(() -> connect(host, port, username, attempt), "server-connect");
        connectionThread.setDaemon(true);
        connectionThread.start();
    }

    private void connect(String host, int port, String username, Client attempt) {
        try {
            attempt.connect(host, port);
            if (disconnectRequested || client != attempt) {
                attempt.disconnect();
                return;
            }
            attempt.login(username);
        } catch (IOException | RuntimeException e) {
            Platform.runLater(() -> {
                if (client == attempt) {
                    client = null;
                    connectButton.setDisable(false);
                    showWarning("Connection failed: " + e.getMessage());
                }
            });
            if (attempt.isConnected()) {
                attempt.disconnect();
            }
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        Stage connectionWindow = (Stage) ((Node) event.getSource()).getScene().getWindow();
        connectionWindow.close();
    }

    private void openOnlineMenu(Client connectedClient, String username) throws IOException {
        FXMLLoader loader = FxmlViews.loader("OnlineMenuView.fxml");
        Parent onlineMenu = loader.load();
        OnlineMenuController onlineMenuController = loader.getController();

        GuiOnlineClient online = new GuiOnlineClient(new GameService());
        online.attach(connectedClient, username);
        onlineMenuController.setOnline(online);

        Stage connectionWindow = (Stage) rootPane.getScene().getWindow();
        Stage mainWindow = (Stage) connectionWindow.getOwner();
        mainWindow.setScene(new Scene(onlineMenu));
        mainWindow.setTitle("OnlineMenu");
        mainWindow.setOnHidden(windowEvent -> online.disconnect());
        mainWindow.show();

        // Transfer ownership of the live client to OnlineMenu before closing this popup.
        client = null;
        connectionWindow.close();
    }

    private void showWarning(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Connection");
        alert.setHeaderText("Unable to connect");
        alert.setContentText(message);
        if (rootPane.getScene() != null && rootPane.getScene().getWindow() != null) {
            alert.initOwner(rootPane.getScene().getWindow());
        }
        alert.showAndWait();
    }

    public void disconnect() {
        disconnectRequested = true;
        Client connectedClient = client;
        client = null;
        if (connectedClient != null && connectedClient.isConnected()) {
            connectedClient.disconnect();
        }
    }
}

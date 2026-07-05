package org.connect4;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Client client;
    private static final String DEFAULT_SERVER_HOST = "cn4-container.gentlewater-ac614102.centralus.azurecontainerapps.io";
    private static final int DEFAULT_SERVER_PORT = 5555;

    @Override
    public void start(Stage primaryStage) {
        client = new Client(getServerHost(), getServerPort());

        SceneManager.setStage(primaryStage);

        SceneManager.switchToLogin();
//        Client.gameboard = new GameBoard();
//        SceneManager.switchScene(Client.gameboard.getScene());
//        SceneManager.switchSceneWithSplit(LobbyUI.getLobbyDetailScene());

        primaryStage.setTitle("Connect4 Game");
        primaryStage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(Client::end));
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static String getServerHost() {
        String host = System.getProperty("connect4.server.host");
        if (host == null || host.isBlank()) {
            host = System.getenv("CONNECT4_SERVER_HOST");
        }
        if (host == null || host.isBlank()) {
            return DEFAULT_SERVER_HOST;
        }
        return host.trim();
    }

    private static int getServerPort() {
        String port = System.getProperty("connect4.server.port");
        if (port == null || port.isBlank()) {
            port = System.getenv("CONNECT4_SERVER_PORT");
        }
        if (port == null || port.isBlank()) {
            return DEFAULT_SERVER_PORT;
        }

        try {
            return Integer.parseInt(port.trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid server port '" + port + "'. Using " + DEFAULT_SERVER_PORT + ".");
            return DEFAULT_SERVER_PORT;
        }
    }
}

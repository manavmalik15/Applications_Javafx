package org.connect4;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    public static Client client;

    @Override
    public void start(Stage primaryStage) {
        client = new Client("localhost", 5555);

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
}
package org.connect4;

import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SceneManager {

    private static Stage primaryStage;

    // Initialize the stage (must be called in MainApp.start())
    public static void setStage(Stage stage) {
        primaryStage = stage;
        primaryStage.setResizable(false);
    }

    // Regular scene switch with no animation
    public static void switchScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    // Switch scene with split-screen animation
    public static void switchSceneWithSplit(Scene newScene) {
        //primaryStage.setScene(newScene);
        Scene currentScene = primaryStage.getScene();

        // Fallback if scene is not set
        if (currentScene == null || currentScene.getRoot() == null) {
            primaryStage.setScene(newScene);
            return;
        }

        Parent currentRoot = currentScene.getRoot();

        // Wrap in StackPane if not already
        StackPane animatedRoot;
        if (currentRoot instanceof StackPane) {
            animatedRoot = (StackPane) currentRoot;
        } else {
            animatedRoot = new StackPane(currentRoot);
            currentScene.setRoot(animatedRoot);
        }

        double width = primaryStage.getWidth();
        double height = primaryStage.getHeight();

        // Create two black rectangles
        Rectangle leftRect = new Rectangle(width / 2, height, Color.MEDIUMPURPLE);
        Rectangle rightRect = new Rectangle(width / 2, height, Color.MEDIUMPURPLE);

        HBox overlay = new HBox(leftRect, rightRect);
        animatedRoot.getChildren().add(overlay);

        // Animate them outward
        TranslateTransition leftAnim = new TranslateTransition(Duration.millis(800), leftRect);
        leftAnim.setToX(-width / 2);

        TranslateTransition rightAnim = new TranslateTransition(Duration.millis(800), rightRect);
        rightAnim.setToX(width / 2);

        ParallelTransition transition = new ParallelTransition(leftAnim, rightAnim);
        transition.setOnFinished(e -> primaryStage.setScene(newScene));
//        primaryStage.setScene(newScene);
        transition.play();
    }

    // Go to Login screen with transition
    public static void switchToLogin() {
        Scene loginScene = LoginUI.getLoginScene();
        switchSceneWithSplit(loginScene);
    }

    // Go to Lobby Home with transition
    public static void switchToLobby() {
        Scene lobbyScene = LobbyUI.getLobbyHomeScene();
        switchSceneWithSplit(lobbyScene);
    }

    // Add more routes here as needed
}
